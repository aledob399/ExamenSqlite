package com.example.examensqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dbHelper=DatabaseHelper(this)
        val articulo=Articulo(Articulo.TipoArticulo.ARMA,Articulo.Nombre.BASTON,3,12)
        val articulo1=Articulo(Articulo.TipoArticulo.PROTECCION,Articulo.Nombre.ARMADURA,1,123)
        val articulo2=Articulo(Articulo.TipoArticulo.ARMA,Articulo.Nombre.BASTON,3,12)
        dbHelper.insertarArticulo(articulo)
        dbHelper.insertarArticulo(articulo2)
        dbHelper.insertarArticulo(articulo1)
        val mochila=Mochila(2)
        mochila.setContenido(dbHelper.getArticulos())

        val personaje=Personaje("Pedro",Personaje.Raza.Humano,Personaje.Clase.Mago,Personaje.EstadoVital.Joven)

        personaje.getMochila().setContenido(dbHelper.getArticulos())
        val mochilaTxt=findViewById<TextView>(R.id.mochila)


    }


}
 class DatabaseHelper(context: Context): SQLiteOpenHelper(context,DATABASE,null,DATABASE_VERSION){
     companion object{
         private const val DATABASE_VERSION=1
         private const val DATABASE="articulos.db"
         private const val TABLA_ARTICULOS="articulos"
         private const val KEY_ID ="_id"
         private const val COLUMN_TIPO_ARTICULO="tipoArticulo"
         private const val COLUMN_NOMBRE="nombre"
         private const val COLUMN_PESO="peso"
         private const val COLUMN_PRECIO="precio"

     }
     override fun onCreate(db:SQLiteDatabase){
         val createTable="CREATE TABLE $TABLA_ARTICULOS ($KEY_ID INTEGER PRIMARY KEY,"+
                 "$COLUMN_NOMBRE TEXT, $COLUMN_TIPO_ARTICULO TEXT, $COLUMN_PRECIO INTEGER,"+
                 "$COLUMN_PESO INTEGER)"
         db.execSQL(createTable)
     }
     override fun onUpgrade(db:SQLiteDatabase,oldVersion:Int,newVersion:Int){
         db.execSQL("DROP TABLE IF EXISTS $TABLA_ARTICULOS")
         onCreate(db)
     }
     fun insertarArticulo(articulo: Articulo){
         val db=this.writableDatabase
         var values=ContentValues()
         when (articulo.getTipoArticulo()) {
             Articulo.TipoArticulo.ARMA -> {
                 when (articulo.getNombre()) {
                     Articulo.Nombre.BASTON, Articulo.Nombre.ESPADA, Articulo.Nombre.DAGA,
                     Articulo.Nombre.MARTILLO, Articulo.Nombre.GARRAS -> {
                          values= ContentValues().apply{
                             put(COLUMN_NOMBRE,articulo.getNombre().name)
                             put(COLUMN_TIPO_ARTICULO,articulo.getTipoArticulo().name)
                             put(COLUMN_PESO,articulo.getPeso())
                             put(COLUMN_PRECIO,articulo.getPrecio())
                         }
                     }
                     else -> println("Nombre del artículo no válido para el tipo ARMA.")
                 }
             }
             Articulo.TipoArticulo.OBJETO -> {
                 when (articulo.getNombre()) {
                     Articulo.Nombre.POCION, Articulo.Nombre.IRA -> {
                          values= ContentValues().apply{
                             put(COLUMN_NOMBRE,articulo.getNombre().name)
                             put(COLUMN_TIPO_ARTICULO,articulo.getTipoArticulo().name)
                             put(COLUMN_PESO,articulo.getPeso())
                             put(COLUMN_PRECIO,articulo.getPrecio())
                         }
                     }
                     else -> println("Nombre del artículo no válido para el tipo OBJETO.")
                 }
             }
             Articulo.TipoArticulo.PROTECCION -> {
                 when (articulo.getNombre()) {
                     Articulo.Nombre.ESCUDO, Articulo.Nombre.ARMADURA -> {
                          values= ContentValues().apply{
                             put(COLUMN_NOMBRE,articulo.getNombre().name)
                             put(COLUMN_TIPO_ARTICULO,articulo.getTipoArticulo().name)
                             put(COLUMN_PESO,articulo.getPeso())
                             put(COLUMN_PRECIO,articulo.getPrecio())
                         }
                     }
                     else -> println("Nombre del artículo no válido para el tipo PROTECCION.")
                 }
             }
         }

         db.insert(TABLA_ARTICULOS,null,values)
         db.close()
     }
    @SuppressLint("Range")
    fun getArticulos():ArrayList<Articulo>{
        val articulos=ArrayList<Articulo>()
        val selectQuery = "SELECT * FROM $TABLA_ARTICULOS"
        val db=this.readableDatabase
        val cursor=db.rawQuery(selectQuery,null)
        if(cursor.moveToFirst()){
            do{
                val id=cursor.getInt(cursor.getColumnIndex(KEY_ID))
                var nom=Articulo.Nombre.ARMADURA
                nom = when(cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE))){
                    "BASTON"-> {
                        Articulo.Nombre.BASTON
                    }
                    "ESPADA"-> {
                        Articulo.Nombre.ESPADA
                    }
                    "DAGA"-> {
                        Articulo.Nombre.DAGA
                    }
                    "MARTILLO"-> {
                        Articulo.Nombre.MARTILLO
                    }
                    "GARRAS"-> {
                        Articulo.Nombre.GARRAS
                    }
                    "POCION"-> {
                        Articulo.Nombre.POCION
                    }
                    "IRA"-> {
                        Articulo.Nombre.IRA
                    }
                    "ESCUDO"-> {
                        Articulo.Nombre.ESCUDO
                    }
                    "ARMADURA"-> {
                        Articulo.Nombre.ARMADURA
                    }

                    else -> {Articulo.Nombre.IRA}
                     }
                var tipoArticulo=Articulo.TipoArticulo.ARMA

                tipoArticulo=when(cursor.getString(cursor.getColumnIndex(COLUMN_TIPO_ARTICULO))){
                    "ARMA"-> {
                        Articulo.TipoArticulo.ARMA
                    }
                    "OBJETO"-> {
                        Articulo.TipoArticulo.OBJETO
                    }
                    "PROTECCION"-> {
                        Articulo.TipoArticulo.PROTECCION
                    }

                    else -> {Articulo.TipoArticulo.PROTECCION}
                }
                val peso=cursor.getInt(cursor.getColumnIndex(COLUMN_PESO))
                val precio=cursor.getInt(cursor.getColumnIndex(COLUMN_PESO))

                articulos.add(Articulo(tipoArticulo,nom,peso,precio))

                }while (cursor.moveToNext())




        }
        cursor.close()
        db.close()
        return articulos
    }
     fun getNumeroArticulos(): Int {
         val articulos = ArrayList<Articulo>()
         val selectQuery = "SELECT * FROM $TABLA_ARTICULOS"
         val db = this.readableDatabase
         val cursor = db.rawQuery(selectQuery, null)
         var num = 0
         if (cursor.moveToFirst()) {
             do {
                 num++;
             } while (cursor.moveToNext())


         }
         return num
     }
     @SuppressLint("Range")
     fun findObjeto(nombre: Articulo.Nombre):Boolean {

         val selectQuery = "SELECT * FROM $TABLA_ARTICULOS"
         val db = this.readableDatabase
         val cursor = db.rawQuery(selectQuery, null)
         var flag = false
         if (cursor.moveToFirst()) {
             do {

                 var nom = Articulo.Nombre.ARMADURA
                 nom = when (cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE))) {
                     "BASTON" -> {
                         Articulo.Nombre.BASTON
                     }

                     "ESPADA" -> {
                         Articulo.Nombre.ESPADA
                     }

                     "DAGA" -> {
                         Articulo.Nombre.DAGA
                     }

                     "MARTILLO" -> {
                         Articulo.Nombre.MARTILLO
                     }

                     "GARRAS" -> {
                         Articulo.Nombre.GARRAS
                     }

                     "POCION" -> {
                         Articulo.Nombre.POCION
                     }

                     "IRA" -> {
                         Articulo.Nombre.IRA
                     }

                     "ESCUDO" -> {
                         Articulo.Nombre.ESCUDO
                     }

                     "ARMADURA" -> {
                         Articulo.Nombre.ARMADURA
                     }

                     else -> {
                         Articulo.Nombre.IRA
                     }
                 }

                 if (nom == nombre) {
                     flag = true

                 }
             } while (cursor.moveToNext() && !flag)
         }
         return flag
     }
 }