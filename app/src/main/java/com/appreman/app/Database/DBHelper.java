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

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteAssetHelper {

    private static final String TAG = "REMAN";
    private static final String DATABASE_NAME = "reman.db";
    private static final int DATABASE_VERSION = 1;


    @SuppressLint("SdCardPath")
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        android.util.Log.v("BBDD",
                "BD actualizándose. Se perderán los datos antiguos");

        onCreate(db);
    }


    /* -TODO .- Grupos ------------------------------------------------------------------------------------------- */

    public List<Grupo> getAllGrupos(){

        List<Grupo> v_grupos = new ArrayList<>();

        try (SQLiteDatabase v_db = this.getReadableDatabase()) {

            Cursor v_cursor = v_db.rawQuery("select numero, nombre from grupo order by numero", null);

            if (null != v_cursor) {
                while (v_cursor.moveToNext()) {

                    Grupo v_grupo = new Grupo();

                    v_grupo.setNumero(v_cursor.getInt(0));
                    v_grupo.setNombre(v_cursor.getString(1));


                    v_grupos.add(v_grupo);


                }
                v_cursor.close();


            }


        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return v_grupos;
    }



    /* -TODO .- Elementos ------------------------------------------------------------------------------------------- */

    public List<Elemento> getAllElementos(){

        List<Elemento> v_elementos = new ArrayList<>();

        try (SQLiteDatabase v_db = this.getReadableDatabase()) {

            Cursor v_cursor = v_db.rawQuery("select numero, nombre, grupo from elemento order by numero desc", null);

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


        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return v_elementos;
    }


    public List<Elemento> getElementosGrupo(int grupo){

        List<Elemento> v_elementos = new ArrayList<>();

        try (SQLiteDatabase v_db = this.getReadableDatabase()) {

            Cursor v_cursor = v_db.rawQuery("select numero, nombre, grupo from elemento where grupo=" + grupo + " order by numero", null);

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


        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return v_elementos;
    }





    /* -TODO .- Preguntas ------------------------------------------------------------------------------------------- */

    public List<Pregunta> getAllPreguntas() {
        List<Pregunta> v_preguntas = new ArrayList<>();

        try (SQLiteDatabase v_db = this.getReadableDatabase()) {
            Cursor v_cursor = v_db.rawQuery("SELECT numero, descripcion, elemento FROM pregunta ORDER BY numero ASC", null);

            if (v_cursor != null && v_cursor.moveToFirst()) {
                do {
                    Pregunta v_pregunta = new Pregunta();
                    v_pregunta.setNumero(v_cursor.getString(0));
                    v_pregunta.setDescripcion(v_cursor.getString(1));
                    v_pregunta.setElemento(v_cursor.getString(2));

                    v_preguntas.add(v_pregunta);
                } while (v_cursor.moveToNext());

                v_cursor.close();
            }
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return v_preguntas;
    }

    public List<Pregunta> getPreguntasElemento(String elemento){

        List<Pregunta> v_preguntas = new ArrayList<>();

        try (SQLiteDatabase v_db = this.getReadableDatabase()) {

            Cursor v_cursor = v_db.rawQuery("select numero, descripcion, elemento from pregunta where elemento='" + elemento + "' order by numero", null);

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


        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return v_preguntas;
    }




    /* -TODO .- Opciones ------------------------------------------------------------------------------------------- */

    public List<Opcion> getAllOpciones(){

        List<Opcion> v_opciones = new ArrayList<>();

        try (SQLiteDatabase v_db = this.getReadableDatabase()) {

            Cursor v_cursor = v_db.rawQuery("select numero, nombre, pregunta from opcion order by numero desc", null);

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
        }

        return v_opciones;
    }


    public List<Opcion> getOpcionesPregunta(String pregunta){

        List<Opcion> v_opciones = new ArrayList<>();

        try (SQLiteDatabase v_db = this.getReadableDatabase()) {

            Cursor v_cursor = v_db.rawQuery("select numero, nombre, pregunta from opcion where pregunta='" + pregunta + "' order by numero", null);

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
                empresa.setId((int) cursor.getLong(cursor.getColumnIndex("id"))); // Asegúrate de tener una columna "id" en tu tabla "empresa"
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

    public void insertarNombreEmpresaEnRespuestas(String nombreEmpresa) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (!existeNombreEmpresaEnRespuestas(db, nombreEmpresa)) {
            ContentValues values = new ContentValues();
            values.put("empresa", nombreEmpresa);

            long result = db.insert("respuestas", null, values);

            if (result != -1) {
                Log.d("DBHelper", "Nombre de Empresa insertado en respuestas: " + nombreEmpresa);
            } else {
                Log.e("DBHelper", "Error al almacenar Nombre de Empresa en respuestas");
            }
        } else {
            Log.d("DBHelper", "Nombre de Empresa ya existe en respuestas: " + nombreEmpresa);
        }

        db.close();
    }

    private boolean existeNombreEmpresaEnRespuestas(SQLiteDatabase db, String nombreEmpresa) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM respuestas WHERE empresa = ?", new String[]{nombreEmpresa});
        boolean existe = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                existe = cursor.getInt(0) > 0;
            }
            cursor.close();
        }
        return existe;
    }


    public void insertarPreguntaEnRespuestas(String nombreEmpresa, String numeroPregunta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("empresa", nombreEmpresa);
        values.put("pregunta", numeroPregunta);
        db.insert("respuestas", null, values);
        db.close();
    }

    public void insertarOpcionesEnRespuestas(String nombreEmpresa, String numeroPregunta, String opcionActual, String opcionPotencial) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Empresa", nombreEmpresa);
        values.put("pregunta", numeroPregunta);
        values.put("opcionActual", opcionActual);
        values.put("opcionPotencial", opcionPotencial);

        Log.d(TAG, "Opcion Actual: " + opcionActual);
        Log.d(TAG, "Opcion Potencial: " + opcionPotencial);

        // Insertar fila
        long insertResult = db.insert("respuestas", null, values);



        if (insertResult != -1) {
            Log.d("DBHelper", "Inserción exitosa en la tabla respuestas");
        } else {
            Log.e("DBHelper", "Error al insertar en la tabla respuestas");
        }

        db.close();
    }










}
