package com.appreman.app.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.appreman.app.Models.Elemento;
import com.appreman.app.Models.Empresa;
import com.appreman.app.Models.Grupo;
import com.appreman.app.Models.Opcion;
import com.appreman.app.Models.Pregunta;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

        long result = db.insert("empresa", null, contentValues);

        if (result != -1) {
            Log.d("DBHelper", "Empresa insertada correctamente: " + nombre);
        } else {
            Log.e("DBHelper", "Error al insertar la empresa: " + nombre);
        }

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
                empresa.setId((int) cursor.getLong(cursor.getColumnIndex("id")));
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

                empresa.setFechaRegistro(cursor.getString(cursor.getColumnIndex("fecha_registro")));
                empresa.setHoraRegistro(cursor.getString(cursor.getColumnIndex("hora_registro")));

                empresas.add(empresa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return empresas;
    }

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

    public void insertarOpcionesEnRespuestas(String nombreEmpresa, String numeroPregunta, String opcionActual, String opcionPotencial) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Empresa", nombreEmpresa);
        values.put("pregunta", numeroPregunta);
        values.put("opcionActual", opcionActual);
        values.put("opcionPotencial", opcionPotencial);

        Log.d(TAG, "Número de Pregunta: " + numeroPregunta);
        Log.d(TAG, "Opcion Actual: " + opcionActual);
        Log.d(TAG, "Opcion Potencial: " + opcionPotencial);

        long insertResult = db.insert("respuestas", null, values);

        if (insertResult != -1) {
            Log.d("DBHelper", "Inserción exitosa en la tabla respuestas");
        } else {
            Log.e("DBHelper", "Error al insertar en la tabla respuestas");
        }

        db.close();
    }


    public List<Opcion> getOpcionesPregunta(String pregunta, String nombreEmpresa) {
        List<Opcion> v_opciones = new ArrayList<>();

        try (SQLiteDatabase v_db = this.getReadableDatabase()) {

            Cursor v_cursor = v_db.rawQuery("select numero, nombre, pregunta from opcion where pregunta='" + pregunta + "' order by numero", null);

            if (null != v_cursor) {
                while (v_cursor.moveToNext()) {

                    Opcion v_opcion = new Opcion();

                    v_opcion.setNumero(v_cursor.getString(0));
                    v_opcion.setNombre(v_cursor.getString(1));
                    v_opcion.setPregunta(v_cursor.getString(2));

                    v_opcion.setRespondida(isOpcionRespondida(v_db, v_opcion.getNumero(), pregunta, nombreEmpresa));

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

    private boolean isOpcionRespondida(SQLiteDatabase db, String opcionNumero, String preguntaNumero, String nombreEmpresa) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM respuestas WHERE empresa='" + nombreEmpresa + "' AND pregunta='" + preguntaNumero +
                        "' AND (opcionActual='" + opcionNumero + "' OR opcionPotencial='" + opcionNumero + "')",
                null
        );
        boolean respondida = cursor.getCount() > 0;
        cursor.close();
        return respondida;
    }

    @SuppressLint("Range")
    public List<String> getAllEmpresaNames() {
        List<String> empresaNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT nombre FROM empresa";
        Cursor cursor = db.rawQuery(query, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    String empresaName = cursor.getString(cursor.getColumnIndex("nombre"));
                    empresaNames.add(empresaName);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }

        return empresaNames;
    }


    public int getRespuestasCount(String nombreEmpresa) {
        SQLiteDatabase db = this.getReadableDatabase();
        int preguntasRespondidas = 0;

        try {
            // Obtener la cantidad de preguntas respondidas para la empresa
            Cursor cursorRespondidas = db.rawQuery("SELECT COUNT(DISTINCT pregunta) FROM respuestas WHERE empresa='" + nombreEmpresa + "'", null);
            if (cursorRespondidas.moveToFirst()) {
                preguntasRespondidas = cursorRespondidas.getInt(0);
            }
            cursorRespondidas.close();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return preguntasRespondidas;
    }



    @SuppressLint("Range")
    public List<Empresa> getEmpresasEnRespuestas() {
        List<Empresa> empresas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM empresa WHERE nombre IN (SELECT DISTINCT TRIM(Empresa) AS Empresa FROM respuestas)", null);

        if (cursor.moveToFirst()) {
            do {
                // Obtener detalles de la empresa desde el cursor
                String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                String pais = cursor.getString(cursor.getColumnIndex("pais"));
                String region = cursor.getString(cursor.getColumnIndex("region"));
                String sitio = cursor.getString(cursor.getColumnIndex("sitio"));

                // Crear un objeto Empresa y agrégalo a la lista
                Empresa empresa = new Empresa(nombre, pais, region, sitio);
                empresas.add(empresa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return empresas;
    }
    public void updateAnswerInDatabase(String nombreEmpresa, String numeroPregunta, String opcionActual, String opcionPotencial) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("opcionActual", opcionActual);
        values.put("opcionPotencial", opcionPotencial);

        // Actualizar la respuesta en la base de datos
        int rowsAffected = db.update(
                "respuestas",
                values,
                "empresa=? AND pregunta=?",
                new String[]{nombreEmpresa, numeroPregunta});

        if (rowsAffected > 0) {
            Log.d("DBHelper", "Respuesta actualizada correctamente en la tabla respuestas");
        } else {
            Log.e("DBHelper", "Error al actualizar la respuesta en la tabla respuestas");
        }

        db.close();
    }

    public boolean isQuestionInDatabase(String nombreEmpresa, String numeroPregunta) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM respuestas WHERE empresa = ? AND pregunta = ?";
            cursor = db.rawQuery(query, new String[]{nombreEmpresa, numeroPregunta});

            // Si hay al menos una fila en el cursor, significa que la pregunta ya está en la base de datos
            return cursor != null && cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    public boolean exportRespuestasToExcel(String filePath, String selectedEmpresa) {
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            String[] selectionArgs = {selectedEmpresa};
            Cursor cursor = db.rawQuery("SELECT * FROM respuestas WHERE empresa = ?", selectionArgs);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("respuestas");

            // ... Tu código existente para crear la hoja de trabajo y la fila de encabezados

            // Obtén el directorio de descargas del dispositivo Android
            File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            // Crea la carpeta "Documents" dentro del directorio de descargas si no existe
            File documentsDirectory = new File(downloadsDirectory, "Documents");
            if (!documentsDirectory.exists()) {
                documentsDirectory.mkdirs();
            }

            // Genera el nombre de archivo basado en el nombre de la empresa
            String nombreArchivo = "datos_" + selectedEmpresa + ".xls";

            // Crea el archivo en la carpeta "Documents" dentro del directorio de descargas
            File file = new File(documentsDirectory, nombreArchivo);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
                Log.d("DBHelper", "Exportación exitosa a Excel: " + file.getAbsolutePath());
            } catch (IOException e) {
                Log.e("DBHelper", "Error al exportar a Excel: " + e.getMessage());
            }

            workbook.close();
        } catch (Exception e) {
            Log.e("DBHelper", "Error al exportar a Excel: " + e.getMessage());
        }
        return false;
    }


}

