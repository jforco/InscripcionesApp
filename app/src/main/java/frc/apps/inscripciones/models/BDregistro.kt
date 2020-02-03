package frc.apps.inscripciones.models

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class BDregistro(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NOMBRE + " TEXT," +
                GENERO + " TEXT," +
                EDAD + " INTEGER," +
                IGLESIA + " TEXT," +
                PAGO + " INTEGER," +
                COLOR + " TEXT);"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    companion object {

        private val DB_VERSION = 2
        private val DB_NAME = "BDregistro"
        private val TABLE_NAME = "registro"
        private val ID = "id"
        private val NOMBRE = "nombre"
        private val GENERO = "genero"
        private val EDAD = "edad"
        private val IGLESIA = "iglesia"
        private val PAGO = "pago"
        private val COLOR = "color"
    }

    fun insertInscrito(i: Inscrito): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NOMBRE, i.nombre)
        values.put(GENERO, i.genero)
        values.put(EDAD, i.edad)
        values.put(IGLESIA, i.iglesia)
        values.put(PAGO, i.pago)
        values.put(COLOR, i.color)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    fun getIncrito(_id: Int): Inscrito {
        val inscrito = Inscrito()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                inscrito.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                inscrito.edad = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EDAD)))
                inscrito.pago = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PAGO)))
                inscrito.nombre = cursor.getString(cursor.getColumnIndex(NOMBRE))
                inscrito.iglesia = cursor.getString(cursor.getColumnIndex(IGLESIA))
                inscrito.color = cursor.getString(cursor.getColumnIndex(COLOR))
                inscrito.genero = cursor.getString(cursor.getColumnIndex(GENERO))
            }
        }
        cursor.close()
        return inscrito
    }

    fun getInscritos(_color:String, _iglesia:String): List<Inscrito>{
        val lista = ArrayList<Inscrito>()
        val db = writableDatabase
        var selectQuery = ""
        if(_color == "Todos" && _iglesia == "Todos"){
            selectQuery = "SELECT  * FROM $TABLE_NAME  ORDER BY $EDAD ASC"
        } else if (_color == "Todos" && _iglesia != "Todos") {
            selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $IGLESIA = '$_iglesia' ORDER BY $EDAD ASC"
        } else if (_color != "Todos" && _iglesia == "Todos") {
            selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $COLOR = '$_color' ORDER BY $EDAD ASC"
        } else {
            selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $COLOR = '$_color' AND $IGLESIA = '$_iglesia' ORDER BY $EDAD ASC"
        }

        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val inscrito = Inscrito()
                inscrito.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                inscrito.edad = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EDAD)))
                inscrito.pago = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PAGO)))
                inscrito.nombre = cursor.getString(cursor.getColumnIndex(NOMBRE))
                inscrito.iglesia = cursor.getString(cursor.getColumnIndex(IGLESIA))
                inscrito.color = cursor.getString(cursor.getColumnIndex(COLOR))
                inscrito.genero = cursor.getString(cursor.getColumnIndex(GENERO))
                lista.add(inscrito)
            }
        }
        cursor.close()
        return lista
    }



    fun updateInscrito(i: Inscrito): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NOMBRE, i.nombre)
        values.put(GENERO, i.genero)
        values.put(EDAD, i.edad)
        values.put(IGLESIA, i.iglesia)
        values.put(PAGO, i.pago)
        values.put(COLOR, i.color)
        val _success = db.update(TABLE_NAME, values, ID + "=?", arrayOf(i.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteInscrito(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    private fun cantColor(_color:String, _genero:String, edad:Int): Int{
        val db = writableDatabase
        val selectQuery = "SELECT COUNT(*) AS cantidad FROM $TABLE_NAME WHERE $COLOR = $_color AND $GENERO = $_genero AND $EDAD = $edad"
        val cursor = db.rawQuery(selectQuery, null)
        var cant = 0
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.isAfterLast) {
                cant = Integer.parseInt(cursor.getString(cursor.getColumnIndex("cantidad")))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return cant
    }

    fun coloresSugeridos(genero:String, edad:Int): List<ColorGrupo>{
        var lista = arrayListOf<ColorGrupo>()
        var colores = arrayListOf<String>()
        if(edad%2 == 0){
            colores.add("Rojo")
            colores.add("Amarillo")
            colores.add("Verde")
            colores.add("Azul")
            colores.add("Celeste")
            colores.add("Naranja")
        } else {
            colores.add("Azul")
            colores.add("Celeste")
            colores.add("Naranja")
            colores.add("Rojo")
            colores.add("Amarillo")
            colores.add("Verde")
        }
        for(color in colores){
            var cant = cantColor(color, genero, edad)
            lista.add(ColorGrupo(color, cant))
            return lista.sortedWith(compareBy({ it.cantidad }))
        }

        return lista
    }

    fun cantIglesias(): List<ColorGrupo>{
        val lista = ArrayList<ColorGrupo>()
        val db = writableDatabase
        var selectQuery = "SELECT $IGLESIA, COUNT($NOMBRE) AS cantidad FROM $TABLE_NAME GROUP BY $IGLESIA ORDER BY cantidad DESC"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val cant = Integer.parseInt(cursor.getString(cursor.getColumnIndex("cantidad")))
                val nombre = cursor.getString(cursor.getColumnIndex(IGLESIA))
                Log.d("nombre:", nombre)
                lista.add(ColorGrupo(nombre, cant))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return lista
    }
    fun cantEdades(): List<ColorGrupo>{
        val lista = ArrayList<ColorGrupo>()
        val db = writableDatabase
        var selectQuery = "SELECT $EDAD, COUNT($EDAD) AS cantidad FROM $TABLE_NAME GROUP BY $EDAD"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val cant = Integer.parseInt(cursor.getString(cursor.getColumnIndex("cantidad")))
                val nombre = cursor.getString(cursor.getColumnIndex(EDAD))
                lista.add(ColorGrupo(nombre, cant))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return lista
    }

    fun cantGenero(): List<ColorGrupo>{
        val lista = ArrayList<ColorGrupo>()
        val db = writableDatabase
        var selectQuery = "SELECT $GENERO, COUNT($GENERO) AS cantidad FROM $TABLE_NAME GROUP BY $GENERO"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val cant = Integer.parseInt(cursor.getString(cursor.getColumnIndex("cantidad")))
                val nombre = cursor.getString(cursor.getColumnIndex(GENERO))
                lista.add(ColorGrupo(nombre, cant))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return lista
    }

    fun cantColor(): List<ColorGrupo>{
        val lista = ArrayList<ColorGrupo>()
        val db = writableDatabase
        var selectQuery = "SELECT $COLOR, COUNT($COLOR) AS cantidad FROM $TABLE_NAME GROUP BY $COLOR"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val cant = Integer.parseInt(cursor.getString(cursor.getColumnIndex("cantidad")))
                val nombre = cursor.getString(cursor.getColumnIndex(COLOR))
                lista.add(ColorGrupo(nombre, cant))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return lista
    }
}