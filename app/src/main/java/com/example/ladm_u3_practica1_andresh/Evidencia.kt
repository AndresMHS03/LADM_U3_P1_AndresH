package com.example.ladm_u3_practica1_andresh

import android.content.Context
import android.database.sqlite.SQLiteException
import java.sql.Blob

class Evidencia(idA:Int,i:ByteArray) {
    var idActividad = idA
    var imagen = i
    var id=0
    var error = -1
    /*
    * */

    val nombreBaseDatos = "Practica1"
    var puntero : Context? = null

    fun asignarPuntero(p:Context){
        puntero=p
    }

    fun mostrarTodos():ArrayList<Evidencia>{
        var data = ArrayList<Evidencia>()
        error = -1

        try {
            var base = BaseDatos (puntero!!,nombreBaseDatos,null,1)
            var select = base.readableDatabase
            var columnas = arrayOf("*")
            var parametros = arrayOf(id.toString())
            var cursor = select.query("EVIDENCIA",columnas,"WHERE ID_ACTIVIDAD =?",parametros,null,null,null)
            if(cursor.moveToFirst()){
                do{
                    var evidenciaTemporal = Evidencia(cursor.getString(1).toInt(),cursor.getBlob(2))
                    evidenciaTemporal.id = cursor.getInt(0)
                    data.add(evidenciaTemporal)
                } while (cursor.moveToNext())
            } else {
                error = 3
            }
        } catch (e: SQLiteException){
            error=1
        }
        return data
    }

    fun eliminar():Boolean{
        try{
            error = -1
            var base = BaseDatos(puntero!!,nombreBaseDatos,null,1)
            var eliminar = base.writableDatabase
            var idEliminar = arrayOf(id.toString())

            var respuesta = eliminar.delete("EVIDENCIA","IDEVIDENCIA =?",idEliminar)

            if(respuesta.toInt()==0){
                error=6
                return false
            }
        } catch (e:SQLiteException){
            error=1
            return false
        }
        return true
    }

    fun buscar(id:String):Evidencia{
        var relleno = ByteArray(0)
        var evidenciaEncontrada =Evidencia(0,relleno)
        error=-1
        try {
            var base = BaseDatos(puntero!!,nombreBaseDatos,null,1)
            var select =base.readableDatabase
            var columnas = arrayOf("*")
            var idBuscar = arrayOf(id)

            var cursor =select.query("EVIDENCIA",columnas,"IDEVIDENCIA =?",idBuscar,null,null,null)

            if(cursor.moveToFirst()){
                evidenciaEncontrada.id=id.toInt()
                evidenciaEncontrada.idActividad = cursor.getInt(1)
                evidenciaEncontrada.imagen = cursor.getBlob(2)
            } else{
                error = 4
            }
        } catch (e:SQLiteException){
            error=1
        }
        return evidenciaEncontrada
    }
}