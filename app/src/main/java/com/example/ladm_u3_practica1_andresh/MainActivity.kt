package com.example.ladm_u3_practica1_andresh

import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var listaID =ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        insertar.setOnClickListener {
            var actividad = Actividad(descripcion.text.toString(),fechaCaptura.text.toString(),fechaEntrega.text.toString())

            actividad.asignarPuntero(this)

            var resultado = actividad.insertar()

            if (resultado){
                mensaje("Se capturo nueva actividad")
                descripcion.setText("")
                fechaCaptura.setText("")
                fechaEntrega.setText("")
            } else{
                when(actividad.error){
                    1->{dialogo("Error en tabla, no se creo o no se conecto a la base de datos")}
                    2->{dialogo("Error, no se pudo insertar")}
                }
            }
            cargarLista()
        }

        buscar.setOnClickListener {
            var actividad = Actividad(descripcion.text.toString(),fechaCaptura.text.toString(),fechaEntrega.text.toString())

            actividad.asignarPuntero(this)

            var resultado = actividad.buscar(editText4.text.toString())

            if(resultado!=null){
                cargarUno(resultado)
            } else {
                when(actividad.error){
                    1 -> {dialogo("Error en tabla, no se creó o no se conectó a la base de datos")}
                    4 -> {dialogo("No se encontró ID")}
                }
            }
        }

        cargarLista()
    }

    private fun cargarUno(a:Actividad) {
        var vector = Array<String>(1,{""})
        var item = a.descripcion+"\n"+a.fechaCaptura+" - "+a.fechaEntrega
        vector[0] = item
        lista.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vector)
        lista.setOnItemClickListener { parent, view, position, id ->
            var con=Actividad("","","")
            con.asignarPuntero(this)
            var actividadEncontrada = con.buscar(listaID[position])

            if (con.error==4){
                dialogo("Error, no se encontró ID")
                return@setOnItemClickListener
            }
            AlertDialog.Builder(this)
                .setTitle("¿Quieres ver a detalle?")
                .setMessage("Descripción: ${actividadEncontrada.descripcion}")
                .setPositiveButton("Si"){d,i-> cargarEnOtroActivity(actividadEncontrada)}
                .setNeutralButton("Cancelar"){d,i->}
                .show()
        }
    }

    private fun cargarLista() {
        try {
            var conexion = Actividad("","","")
            conexion.asignarPuntero(this)
            var data = conexion.mostrarTodos()

            if(data.size==0){
                if(conexion.error==3){
                    dialogo("No se pudo realizar consulta o tabla vacia")
                }
                return
            }

            var total = data.size-1
            var vector = Array<String>(data.size,{""})
            listaID = ArrayList<String>()
            (0..total).forEach {
                var actividad = data [it]
                var item = actividad.descripcion+"\n"+actividad.fechaCaptura+" - "+actividad.fechaEntrega
                vector[it] = item
                listaID.add(actividad.id.toString())
            }
            lista.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vector)
            lista.setOnItemClickListener { parent, view, position, id ->
                var con=Actividad("","","")
                con.asignarPuntero(this)
                var actividadEncontrada = con.buscar(listaID[position])

                if (con.error==4){
                    dialogo("Error, no se encontró ID")
                    return@setOnItemClickListener
                }
                AlertDialog.Builder(this)
                    .setTitle("¿Quieres ver a detalle?")
                    .setMessage("Descripción: ${actividadEncontrada.descripcion}")
                    .setPositiveButton("Si"){d,i-> cargarEnOtroActivity(actividadEncontrada)}
                    .setNeutralButton("Cancelar"){d,i->}
                    .show()
            }
        } catch (e:SQLiteException){
            dialogo(e.message.toString())
        }
    }

    private fun cargarEnOtroActivity(a: Actividad){
        var otroActivity = Intent(this,Main2Activity::class.java)

        otroActivity.putExtra("descripcion",a.descripcion)
        otroActivity.putExtra("fechaCaptura",a.fechaCaptura)
        otroActivity.putExtra("fechaEntrega",a.fechaEntrega)
        otroActivity.putExtra("id",a.id)

        startActivityForResult(otroActivity,0)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cargarLista()
    }
}
