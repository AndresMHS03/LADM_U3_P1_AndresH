package com.example.ladm_u3_practica1_andresh

import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main2.descripcion

class Main2Activity : AppCompatActivity() {
    var listaID =ArrayList<String>()
    var id = ""
    val nombreBaseDatos = "Practica1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        var extra = intent.extras

        descripcion.setText(extra!!.getString("descripcion"))
        feCa.setText(extra!!.getString("fechaCaptura"))
        feEn.setText(extra!!.getString("fechaEntrega"))

        id = extra!!.getString("id").toString()

        eliminar.setOnClickListener {
            var actividadEliminar = Actividad("","","")
            actividadEliminar.id = id.toInt()
            actividadEliminar.asignarPuntero(this)
            if(actividadEliminar.eliminar()){
                dialogo("Se eliminó")
            } else {
                dialogo("Error, no se pudo eliminar")
            }
            finish()
        }
    }

    private fun cargarLista() {
        var relleno = ByteArray(0)
        try {
            var conexion = Evidencia(0,relleno)
            conexion.asignarPuntero(this)
            var data = conexion.mostrarTodos()

            if(data.size==0){
                if(conexion.error==3){
                    dialogo("No se pudo realizar consulta o tabla vacia")
                }
                return
            }

            var total = data.size-1
            var vector = Array<ByteArray>(data.size,{relleno})
            listaID = ArrayList<String>()
            (0..total).forEach {
                var evidencia = data [it]
                var item = evidencia.imagen
                vector[it] = item
                listaID.add(evidencia.id.toString())
            }
            lista.adapter= ArrayAdapter<ByteArray>(this,android.R.layout.simple_list_item_1,vector)
            lista.setOnItemClickListener { parent, view, position, id ->
                var con=Evidencia(0,relleno)
                con.asignarPuntero(this)
                var evidenciaEncontrada = con.buscar(listaID[position])

                if (con.error==4){
                    dialogo("Error, no se encontró ID")
                    return@setOnItemClickListener
                }
                AlertDialog.Builder(this)
                    .setTitle("¿Que quieres hacer?")
                    .setMessage("Descripción: ${evidenciaEncontrada.imagen}")
                    .setPositiveButton("Eliminar"){d,i-> if (evidenciaEncontrada.eliminar()){
                        mensaje("Eliminado correctamente")
                    }
                    else{
                        mensaje("No se pudo eliminar")
                    }}
                    .setNeutralButton("Cancelar"){d,i->}
                    .show()
            }
        } catch (e: SQLiteException){
            dialogo(e.message.toString())
        }
    }

    fun mensaje(s:String){
        Toast.makeText(this,s, Toast.LENGTH_LONG)
            .show()
    }

    fun dialogo(s:String){
        AlertDialog.Builder(this)
            .setTitle("Atención")
            .setMessage(s)
            .setPositiveButton("Ok"){d,i->}
            .show()
    }
}
