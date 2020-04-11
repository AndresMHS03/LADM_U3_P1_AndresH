package com.example.ladm_u3_practica1_andresh

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos (
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE ACTIVIDAD (ID_ACTIVIDAD INTEGER PRIMARY KEY AUTOINCREMENT, DESCRIPCION VARCHAR (200), FECHACAPTURA DATE, FECHAENTREGA DATE)")
        db?.execSQL("CREATE TABLE EVIDENCIA (IDEVIDENCIA INTEGER PRIMARY KEY AUTOINCREMENT, ID_ACTIVIDAD INTEGER, FOTO BLOB, FOREIGN KEY (ID_ACTIVIDAD) REFERENCES ACTIVIDAD (ID_ACTIVIDAD))")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}