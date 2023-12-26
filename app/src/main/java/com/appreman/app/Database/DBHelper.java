package com.appreman.app.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.appreman.app.Models.Elemento;
import com.appreman.app.Models.Empresa;
import com.appreman.app.Models.Grupo;
import com.appreman.app.Models.Opcion;
import com.appreman.app.Models.Pregunta;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteAssetHelper {

    private static final String TAG = "REMAN";
    private static final String DATABASE_NAME = "reman.db";
    private static final int DATABASE_VERSION = 1;

    private Context mContext;
    private String DB_PATH;

    public static final String APPLICATION_ID = "com.appreman.app";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @SuppressLint("SdCardPath")
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;

        //ContextWrapper cw =new ContextWrapper(context);
        DB_PATH = "/data/data/" + APPLICATION_ID + "/databases/"; //cw.getFilesDir().getAbsolutePath()+ "/databases/";



    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        android.util.Log.v("BBDD",
                "BD actualizándose. Se perderán los datos antiguos");

        onCreate(db);
    }

    public void deleteDatabase(){
        //File data = Environment.getDataDirectory();
        String currentDBPath = DB_PATH + DATABASE_NAME;
        Log.e(TAG,"RUTA:"+currentDBPath);
        File currentDB = new File(currentDBPath);

        SQLiteDatabase.deleteDatabase(currentDB);

    }


    public void copyDatabase()
    {
        Log.e(TAG,"New database is being copied to device!");
        byte[] buffer = new byte[1024];
        OutputStream myOutput;
        int length;
        // Open your local db as the input stream
        InputStream myInput;
        try
        {
            myInput =mContext.getAssets().open("databases/"+DATABASE_NAME);
            // transfer bytes from the inputfile to the
            // outputfile
            myOutput =new FileOutputStream(DB_PATH + DATABASE_NAME);
            while((length = myInput.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.e(TAG,
                    "New database has been copied to device!");


        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }


    /* -TODO .- Grupos ------------------------------------------------------------------------------------------- */

    public List<Grupo> getAllGrupos(){

        List<Grupo> v_grupos = new ArrayList<>();

        SQLiteDatabase v_db = this.getReadableDatabase();

        try {

            Cursor v_cursor = v_db.rawQuery("select numero, nombre from grupo order by numero" , null);

            if (null != v_cursor) {
                while (v_cursor.moveToNext()) {

                    Grupo v_grupo = new Grupo();

                    v_grupo.setNumero(v_cursor.getInt(0));
                    v_grupo.setNombre(v_cursor.getString(1));


                    v_grupos.add(v_grupo);


                }
                v_cursor.close();


            }


        } catch (SQLException e)
        {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }finally {
            v_db.close();
        }

        return v_grupos;
    }



    /* -TODO .- Elementos ------------------------------------------------------------------------------------------- */

    public List<Elemento> getAllElementos(){

        List<Elemento> v_elementos = new ArrayList<>();

        SQLiteDatabase v_db = this.getReadableDatabase();

        try {

            Cursor v_cursor = v_db.rawQuery("select numero, nombre, grupo from elemento order by numero desc" , null);

            if (null != v_cursor) {
                while (v_cursor.moveToNext()) {

                    Elemento v_elemento = new Elemento();

                    v_elemento.setNumero(v_cursor.getString(0));
                    v_elemento.setNombre(v_cursor.getString(1));
                    v_elemento.setGrupo(v_cursor.getInt(2));

                    v_elementos.add(v_elemento);


                }
                v_cursor.close();


            }


        } catch (SQLException e)
        {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }finally {
            v_db.close();
        }

        return v_elementos;
    }


    public List<Elemento> getElementosGrupo(int grupo){

        List<Elemento> v_elementos = new ArrayList<>();

        SQLiteDatabase v_db = this.getReadableDatabase();

        try {

            Cursor v_cursor = v_db.rawQuery("select numero, nombre, grupo from elemento where grupo="+ grupo +" order by numero" , null);

            if (null != v_cursor) {
                while (v_cursor.moveToNext()) {

                    Elemento v_elemento = new Elemento();

                    v_elemento.setNumero(v_cursor.getString(0));
                    v_elemento.setNombre(v_cursor.getString(1));
                    v_elemento.setGrupo(v_cursor.getInt(2));

                    v_elementos.add(v_elemento);


                }
                v_cursor.close();


            }


        } catch (SQLException e)
        {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }finally {
            v_db.close();
        }

        return v_elementos;
    }





    /* -TODO .- Preguntas ------------------------------------------------------------------------------------------- */

    public List<Pregunta> getAllPreguntas(){

        List<Pregunta> v_preguntas = new ArrayList<>();

        SQLiteDatabase v_db = this.getReadableDatabase();

        try {

            Cursor v_cursor = v_db.rawQuery("select numero, descripcion, elemento from pregunta order by numero desc" , null);

            if (null != v_cursor) {
                while (v_cursor.moveToNext()) {

                    Pregunta v_pregunta = new Pregunta();

                    v_pregunta.setNumero(v_cursor.getString(0));
                    v_pregunta.setDescripcion(v_cursor.getString(1));
                    v_pregunta.setElemento(v_cursor.getString(2));

                    v_preguntas.add(v_pregunta);


                }
                v_cursor.close();


            }


        } catch (SQLException e)
        {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }finally {
            v_db.close();
        }

        return v_preguntas;
    }


    public List<Pregunta> getPreguntasElemento(String elemento){

        List<Pregunta> v_preguntas = new ArrayList<>();

        SQLiteDatabase v_db = this.getReadableDatabase();

        try {

            Cursor v_cursor = v_db.rawQuery("select numero, descripcion, elemento from pregunta where elemento='"+ elemento +"' order by numero" , null);

            if (null != v_cursor) {
                while (v_cursor.moveToNext()) {

                    Pregunta v_pregunta = new Pregunta();

                    v_pregunta.setNumero(v_cursor.getString(0));
                    v_pregunta.setDescripcion(v_cursor.getString(1));
                    v_pregunta.setElemento(v_cursor.getString(2));

                    v_preguntas.add(v_pregunta);


                }
                v_cursor.close();


            }


        } catch (SQLException e)
        {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }finally {
            v_db.close();
        }

        return v_preguntas;
    }




    /* -TODO .- Opciones ------------------------------------------------------------------------------------------- */

    public List<Opcion> getAllOpciones(){

        List<Opcion> v_opciones = new ArrayList<>();

        SQLiteDatabase v_db = this.getReadableDatabase();

        try {

            Cursor v_cursor = v_db.rawQuery("select numero, nombre, pregunta from opcion order by numero desc" , null);

            if (null != v_cursor) {
                while (v_cursor.moveToNext()) {

                    Opcion v_opcion = new Opcion();

                    v_opcion.setNumero(v_cursor.getString(0));
                    v_opcion.setNombre(v_cursor.getString(1));
                    v_opcion.setPregunta(v_cursor.getString(2));

                    v_opciones.add(v_opcion);
                }
                v_cursor.close();
            }

        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } finally {
            v_db.close();
        }

        return v_opciones;
    }


    public List<Opcion> getOpcionesPregunta(String pregunta){

        List<Opcion> v_opciones = new ArrayList<>();

        SQLiteDatabase v_db = this.getReadableDatabase();

        try {

            Cursor v_cursor = v_db.rawQuery("select numero, nombre, pregunta from opcion where pregunta='"+ pregunta +"' order by numero" , null);

            if (null != v_cursor) {
                while (v_cursor.moveToNext()) {

                    Opcion v_opcion = new Opcion();

                    v_opcion.setNumero(v_cursor.getString(0));
                    v_opcion.setNombre(v_cursor.getString(1));
                    v_opcion.setPregunta(v_cursor.getString(2));

                    v_opciones.add(v_opcion);
                }
                v_cursor.close();
            }

        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } finally {
            v_db.close();
        }

        return v_opciones;
    }

    public void insertEmpresa(String nombre, String pais, String region, String sitio, String sector, String planta, String representante, String telefono, String email, String clienteAct, String numeroDePlant, String numeroDePlantIm, String fecha, String hora) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre", nombre);
        contentValues.put("pais", pais);
        contentValues.put("region", region);
        contentValues.put("sitio", sitio);
        contentValues.put("sector_industrial", sector);
        contentValues.put("tipo_planta", planta);
        contentValues.put("representante_empresa", representante);
        contentValues.put("telefono", telefono);
        contentValues.put("email", email);
        contentValues.put("cliente_actual", clienteAct);
        contentValues.put("numero_plantas", numeroDePlant);
        contentValues.put("plantas_implementar", numeroDePlantIm);
        contentValues.put("fecha_registro", fecha);
        contentValues.put("hora_registro", hora);

        // Insertar la empresa con los valores pasados como parámetros
        db.insert("empresa", null, contentValues);

        db.close();
    }


    @SuppressLint("Range")
    public List<Empresa> getAllEmpresas() {
        List<Empresa> empresas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM empresa";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Empresa empresa = new Empresa();
                empresa.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                empresa.setPais(cursor.getString(cursor.getColumnIndex("pais")));
                empresa.setRegion(cursor.getString(cursor.getColumnIndex("region")));
                empresa.setSitio(cursor.getString(cursor.getColumnIndex("sitio")));
                empresa.setSector(cursor.getString(cursor.getColumnIndex("sector_industrial")));
                empresa.setPlanta(cursor.getString(cursor.getColumnIndex("tipo_planta")));
                empresa.setRepresentante(cursor.getString(cursor.getColumnIndex("representante_empresa")));
                empresa.setTelefono(cursor.getString(cursor.getColumnIndex("telefono")));
                empresa.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                empresa.setClienteAct(cursor.getString(cursor.getColumnIndex("cliente_actual")));
                empresa.setNumeroDePlant(cursor.getString(cursor.getColumnIndex("numero_plantas")));
                empresa.setNumeroDePlantIm(cursor.getString(cursor.getColumnIndex("plantas_implementar")));

                // Agregar recuperación de fecha y hora
                empresa.setFechaRegistro(cursor.getString(cursor.getColumnIndex("fecha_registro")));
                empresa.setHoraRegistro(cursor.getString(cursor.getColumnIndex("hora_registro")));

                empresas.add(empresa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return empresas;
    }


}

