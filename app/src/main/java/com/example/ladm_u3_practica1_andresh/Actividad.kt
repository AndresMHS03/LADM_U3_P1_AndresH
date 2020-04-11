package com.example.ladm_u3_practica1_andresh

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException

class Actividad (d:String,fc:String,fe:String) {
    var descripcion = d
    var fechaCaptura = fc
    var fechaEntrega = fe
    var id=0
    var error = -1
    /*
    * */

    val nombreBaseDatos ="Practica1"
    var puntero : Context?= null

    fun asignarPuntero(p:Context){
        puntero=p
    }

    fun insertar():Boolean{
        try {
            error = -1
            var base =BaseDatos(puntero!!,nombreBaseDatos,null,1)
            var insertar =base.writableDatabase
            var datos = ContentValues()

            datos.put("DESCRIPCION",descripcion)
            datos.put("FECHACAPTURA",fechaCaptura)
            datos.put("FECHAENTREGA",fechaEntrega)

            var respuesta = insertar.insert("ACTIVIDAD","ID_ACTIVIDAD",datos)

            if (respuesta.toInt()==-1){
                error=2
                return false
            }
        } catch (e:SQLiteException){
            error=1
            return false
        }
        return true
    }

    fun mostrarTodos():ArrayList<Actividad>{
        var data = ArrayList<Actividad>()
        error = -1

        try {
            var base = BaseDatos (puntero!!,nombreBaseDatos,null,1)
            var select = base.readableDatabase
            var columnas = arrayOf("*")

            var cursor = select.query("ACTIVIDAD",columnas,null,null,null,null,null)
            if(cursor.moveToFirst()){
                do{
                    var actividadTemporal = Actividad(cursor.getString(1),cursor.getString(2),cursor.getString(3))
                    actividadTemporal.id = cursor.getInt(0)
                    data.add(actividadTemporal)
                } while (cursor.moveToNext())
            } else {
                error = 3
            }
        } catch (e:SQLiteException){
            error=1
        }
        return data
    }

    fun buscar(id:String):Actividad{
        var actividadEncontrada =Actividad("-1","-1","-1")
        error=-1
        try {
            var base = BaseDatos(puntero!!,nombreBaseDatos,null,1)
            var select =base.readableDatabase
            var columnas = arrayOf("*")
            var idBuscar = arrayOf(id)

            var cursor =select.query("ACTIVIDAD",columnas,"ID_ACTIVIDAD =?",idBuscar,null,null,null)

            if(cursor.moveToFirst()){
                actividadEncontrada.id=id.toInt()
                actividadEncontrada.descripcion = cursor.getString(1)
                actividadEncontrada.fechaCaptura = cursor.getString(2)
                actividadEncontrada.fechaEntrega = cursor.getString(3)
            } else{
                error = 4
            }
        } catch (e:SQLiteException){
            error=1
        }
        return actividadEncontrada
    }

    fun eliminar():Boolean{
        try{
            error = -1
            var base = BaseDatos(puntero!!,nombreBaseDatos,null,1)
            var eliminar = base.writableDatabase
            var idEliminar = arrayOf(id.toString())

            var respuesta = eliminar.delete("ACTIVIDAD","ID_ACTIVIDAD =?",idEliminar)

            if(respuesta==0){
                error=6
                return false
            }
        } catch (e:SQLiteException){
            error=1
            return false
        }
        return true
    }

}