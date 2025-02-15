package com.appreman.app.Database;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.appreman.app.Models.Asignar;
import com.appreman.app.Models.Elemento;
import com.appreman.app.Models.Empresa;
import com.appreman.app.Models.Grupo;
import com.appreman.app.Models.Opcion;
import com.appreman.app.Models.Operador;
import com.appreman.app.Models.Pregunta;
import com.appreman.app.Models.Respuesta;
import com.appreman.app.Sync.SyncAdapter;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DBHelper extends SQLiteAssetHelper {

    private static final String TAG = "REMAN";
    private static final String DATABASE_NAME = "reman.db";
    private static final int DATABASE_VERSION = 1;
    private SyncAdapter syncAdapter;



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


    public List<Elemento> getElementosGrupo(String grupo){

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


    public void insertEmpresa(String nombre, String pais, String region, String sitio, String sector, String planta, String representante, String telefono, String email, String clienteAct, String numeroDePlant, String numeroDePlantIm, String fecha, String operadorId) {
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
        contentValues.put("id_operador", operadorId); // Agregar el campo id_operador

        // Log para ver los valores que se están guardando
        Log.d("DBHelper", "Insertando empresa con los siguientes valores: " +
                "nombre=" + nombre + ", " +
                "pais=" + pais + ", " +
                "region=" + region + ", " +
                "sitio=" + sitio + ", " +
                "sector_industrial=" + sector + ", " +
                "tipo_planta=" + planta + ", " +
                "representante_empresa=" + representante + ", " +
                "telefono=" + telefono + ", " +
                "email=" + email + ", " +
                "cliente_actual=" + clienteAct + ", " +
                "numero_plantas=" + numeroDePlant + ", " +
                "plantas_implementar=" + numeroDePlantIm + ", " +
                "fecha_registro=" + fecha + ", " +
                "id_operador=" + operadorId);

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

                empresas.add(empresa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return empresas;
    }

    public void insertarOpcionesEnRespuestas(String nombreEmpresa, String numeroPregunta, String opcionActual, String opcionPotencial, String fechaRespuesta, String comentario, String nombreEncuestado, String cargoEncuestado) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Recuperar el elemento correspondiente a la pregunta
        String elemento = getElementoDePregunta(numeroPregunta);

        if (elemento == null) {
            Log.e(TAG, "No se pudo encontrar el elemento para la pregunta número: " + numeroPregunta);
            return;
        }

        // Crear una instancia del modelo Respuesta
        Respuesta respuesta = new Respuesta(
                nombreEmpresa != null ? nombreEmpresa : "",
                numeroPregunta != null ? numeroPregunta : "",
                opcionActual != null ? opcionActual : "",
                opcionPotencial != null ? opcionPotencial : "",
                elemento,
                fechaRespuesta != null ? fechaRespuesta : "",
                comentario != null ? comentario : "",
                nombreEncuestado != null ? nombreEncuestado : "",
                cargoEncuestado != null ? cargoEncuestado : ""
        );

        // Insertar en la base de datos
        ContentValues values = new ContentValues();
        values.put("empresa", respuesta.getNombreEmpresa());
        values.put("pregunta", respuesta.getPregunta());
        values.put("opcion_actual", respuesta.getOpcionActual());  // Ahora nunca será null
        values.put("opcion_potencial", respuesta.getOpcionPotencial());  // Ahora nunca será null
        values.put("elemento", respuesta.getElemento());
        values.put("fecha_respuesta", respuesta.getFechaRespuesta());
        values.put("comentario", respuesta.getComentario());
        values.put("nombre_encuestado", respuesta.getNombreEncuestado());
        values.put("cargo_encuestado", respuesta.getCargoEncuestado());

        // Log de los valores que se van a insertar
        Log.d("DBHelper", "Insertando respuesta con valores: " + values.toString());

        long insertResult = db.insert("respuesta", null, values);

        if (insertResult != -1) {
            Log.d("DBHelper", "Inserción exitosa en la tabla respuestas");
        } else {
            Log.e("DBHelper", "Error al insertar en la tabla respuestas");
        }

        db.close();
    }

    public void updateAnswerInDatabase(String nombreEmpresa, String numeroPregunta, String opcionActual, String opcionPotencial, String comentario, String nombreEncuestado, String cargoEncuestado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Reemplazar valores nulos por cadenas vacías
        opcionActual = opcionActual != null ? opcionActual : "";
        opcionPotencial = opcionPotencial != null ? opcionPotencial : "";
        comentario = comentario != null ? comentario : "";
        nombreEncuestado = nombreEncuestado != null ? nombreEncuestado : "";
        cargoEncuestado = cargoEncuestado != null ? cargoEncuestado : "";

        // Verificar si las opciones están vacías
        if (!opcionActual.equals("Comentario")) {
            values.put("opcion_actual", opcionActual);
        } else {
            values.put("opcion_actual", "");
        }

        if (!opcionPotencial.equals("Comentario")) {
            values.put("opcion_potencial", opcionPotencial);
        } else {
            values.put("opcion_potencial", "");
        }

        values.put("comentario", comentario);
        values.put("nombre_encuestado", nombreEncuestado);
        values.put("cargo_encuestado", cargoEncuestado);

        // Log de los valores que se van a actualizar
        Log.d("DBHelper", "Actualizando respuesta con valores: " + values.toString());

        // Actualizar la respuesta en la base de datos
        int rowsAffected = db.update(
                "respuesta",
                values,
                "empresa=? AND pregunta=?",
                new String[]{nombreEmpresa != null ? nombreEmpresa : "", numeroPregunta != null ? numeroPregunta : ""});

        if (rowsAffected > 0) {
            Log.d("DBHelper", "Respuesta actualizada correctamente en la tabla respuestas");
        } else {
            Log.e("DBHelper", "Error al actualizar la respuesta en la tabla respuestas");
        }

        db.close();
    }


    // Método para obtener el elemento de una pregunta dada
    @SuppressLint("Range")
    private String getElementoDePregunta(String numeroPregunta) {
        String elemento = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT elemento FROM pregunta WHERE numero = ?", new String[]{numeroPregunta});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                elemento = cursor.getString(cursor.getColumnIndex("elemento"));
            }
            cursor.close();
        }

        return elemento;
    }


    public List<Opcion> getOpcionesPregunta(String pregunta, String nombreEmpresa) {
        List<Opcion> v_opciones = new ArrayList<>();

        try (SQLiteDatabase v_db = this.getReadableDatabase()) {
            // Obtener las opciones de la tabla Opcion
            Cursor v_cursor = v_db.rawQuery("SELECT numero, nombre, pregunta FROM opcion WHERE pregunta = ? ORDER BY numero", new String[]{pregunta});

            if (v_cursor != null) {
                while (v_cursor.moveToNext()) {
                    Opcion v_opcion = new Opcion();
                    v_opcion.setNumero(v_cursor.getString(0));
                    v_opcion.setNombre(v_cursor.getString(1));
                    v_opcion.setPregunta(v_cursor.getString(2));

                    // Verificar si la opción está respondida
                    v_opcion.setRespondida(isOpcionRespondida(v_db, v_opcion.getNumero(), pregunta, nombreEmpresa));

                    v_opciones.add(v_opcion);
                }
                v_cursor.close();
            }

            // Ahora obtenemos las respuestas para esta pregunta y empresa
            Cursor respuestaCursor = v_db.rawQuery(
                    "SELECT opcion_actual, opcion_potencial FROM respuesta WHERE pregunta = ? AND empresa = ?",
                    new String[]{pregunta, nombreEmpresa}
            );

            if (respuestaCursor != null && respuestaCursor.moveToFirst()) {
                String opcionActualNumero = respuestaCursor.getString(0);  // opción actual
                String opcionPotencialNumero = respuestaCursor.getString(1);  // opción potencial

                // Asignar la opción actual y potencial a las opciones correspondientes
                for (Opcion opcion : v_opciones) {
                    if (opcion.getNumero().equals(opcionActualNumero)) {
                        opcion.setSeleccionada(true);
                        if (opcionActualNumero.equals(opcionPotencialNumero)) {
                            opcion.setNombreOpcion("Actual&Potencial");
                        } else {
                            opcion.setNombreOpcion("Actual");
                        }
                    } else if (opcion.getNumero().equals(opcionPotencialNumero)) {
                        opcion.setSeleccionada(true);
                        if (!opcionActualNumero.equals(opcionPotencialNumero)) {
                            opcion.setNombreOpcion("Potencial");
                        }
                    }
                }
                respuestaCursor.close();
            }
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return v_opciones;
    }



    private boolean isOpcionRespondida(SQLiteDatabase db, String opcionNumero, String preguntaNumero, String nombreEmpresa) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM respuesta WHERE empresa='" + nombreEmpresa + "' AND pregunta='" + preguntaNumero +
                        "' AND (opcion_actual='" + opcionNumero + "' OR opcion_potencial='" + opcionNumero + "')",
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
            Cursor cursorRespondidas = db.rawQuery("SELECT COUNT(DISTINCT pregunta) FROM respuesta WHERE empresa='" + nombreEmpresa + "'", null);
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

        Cursor cursor = db.rawQuery("SELECT * FROM empresa WHERE nombre IN (SELECT DISTINCT TRIM(Empresa) AS Empresa FROM respuesta)", null);

        if (cursor.moveToFirst()) {
            do {
                // Obtener detalles de la empresa desde el cursor
                String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                String pais = cursor.getString(cursor.getColumnIndex("pais"));
                String region = cursor.getString(cursor.getColumnIndex("region"));
                String sitio = cursor.getString(cursor.getColumnIndex("sitio"));
                String representante = cursor.getString(cursor.getColumnIndex("representante_empresa"));
                String tipo_planta = cursor.getString(cursor.getColumnIndex("tipo_planta"));
                String id_operador = cursor.getString(cursor.getColumnIndex("id_operador"));



                // Crear un objeto Empresa y agrégalo a la lista
                Empresa empresa = new Empresa(nombre, pais, region, sitio, representante, tipo_planta, id_operador);
                empresas.add(empresa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return empresas;
    }

    public boolean isQuestionInDatabase(String nombreEmpresa, String numeroPregunta) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM respuesta WHERE empresa = ? AND pregunta = ?";
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

    public int getEmpresasCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;

        try {
            String query = "SELECT COUNT(*) FROM empresa";
            Cursor cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return count;
    }


    public int getRespuestasEmpresasCount() {
        int count = 0;

        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor cursor = db.rawQuery("SELECT COUNT(DISTINCT empresa) FROM respuesta", null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return count;
    }

    @SuppressLint("Range")
    public File guardarRespuestasEnArchivo(String selectedEmpresa, Context context) {
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            String[] selectionArgs = {selectedEmpresa};
            Cursor cursor = db.rawQuery(
                    "SELECT empresa, pregunta, opcion_actual, opcion_potencial, elemento FROM respuesta WHERE empresa = ?",
                    selectionArgs
            );

            // Log the content of the 'respuestas' table
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    StringBuilder rowContent = new StringBuilder();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        rowContent.append(cursor.getColumnName(i)).append(": ").append(cursor.getString(i)).append(", ");
                    }
                    Log.d(TAG, "Row content: " + rowContent.toString());
                } while (cursor.moveToNext());
            }

            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String fileName = selectedEmpresa + ".csv";
            File file = new File(downloadsDir, fileName);

            try (FileWriter writer = new FileWriter(file)) {
                if (cursor != null && cursor.moveToFirst()) {
                    // Write column names, including the new 'elemento' column
                    writer.append("empresa,pregunta,opcion_actual,opcion_potencial,elemento\n");

                    do {
                        writer.append(cursor.getString(cursor.getColumnIndex("empresa"))).append(",");
                        writer.append(cursor.getString(cursor.getColumnIndex("pregunta"))).append(",");
                        writer.append(cursor.getString(cursor.getColumnIndex("opcion_actual"))).append(",");
                        writer.append(cursor.getString(cursor.getColumnIndex("opcion_potencial"))).append(",");
                        writer.append(cursor.getString(cursor.getColumnIndex("elemento"))).append("\n");
                    } while (cursor.moveToNext());
                }
                Log.d(TAG, "Guardado exitoso en el archivo: " + file.getAbsolutePath());
                return file;
            } catch (IOException e) {
                Log.e(TAG, "Error al guardar en el archivo: " + e.getMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al guardar en el archivo: " + e.getMessage());
        }
        return null;
    }

    public void descargarArchivo(File archivo, Context context) {
        if (archivo != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", archivo);
            intent.setData(uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No hay aplicación para abrir el archivo", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "No se pudo encontrar una actividad para manejar el intent");
            }
        } else {
            Toast.makeText(context, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error al guardar en el archivo");
        }
    }

    public int getTotalPreguntasCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM pregunta", null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return count;
    }
    // Rename the method to avoid ambiguity
    public String getDescripcionPregunta(String numeroPregunta) {
        String descripcion = "";
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor cursor = db.rawQuery("SELECT descripcion FROM pregunta WHERE numero = ?", new String[]{numeroPregunta});
            if (cursor.moveToFirst()) {
                descripcion = cursor.getString(0);
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        return descripcion;
    }


    public Map<String, Double> obtenerContadoresYPorcentajes(String nombreEmpresa) {
        SQLiteDatabase db = this.getReadableDatabase();

        int contadorActual = 0;
        int contadorPotencial = 0;
        int contadorAmbas = 0;
        int totalOpciones = 0;

        Cursor cursorRespuestas = db.rawQuery(
                "SELECT pregunta, opcion_actual, opcion_potencial FROM respuesta WHERE empresa = ?",
                new String[]{nombreEmpresa}
        );

        Map<String, Integer> mapaOpcionesActual = new HashMap<>();
        Map<String, Integer> mapaOpcionesPotencial = new HashMap<>();
        Map<String, Integer> mapaOpcionesAmbas = new HashMap<>();

        if (cursorRespuestas.moveToFirst()) {
            do {
                String pregunta = cursorRespuestas.getString(0);
                String opcionActual = cursorRespuestas.getString(1);
                String opcionPotencial = cursorRespuestas.getString(2);

                // Evitar NullPointerException
                if (opcionActual == null) opcionActual = "";
                if (opcionPotencial == null) opcionPotencial = "";

                // Comparación corregida
                if (opcionActual.equals(opcionPotencial)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mapaOpcionesAmbas.put(pregunta, mapaOpcionesAmbas.getOrDefault(pregunta, 0) + 1);
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mapaOpcionesActual.put(pregunta, mapaOpcionesActual.getOrDefault(pregunta, 0) + 1);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mapaOpcionesPotencial.put(pregunta, mapaOpcionesPotencial.getOrDefault(pregunta, 0) + 1);
                    }
                }
            } while (cursorRespuestas.moveToNext());
        }
        cursorRespuestas.close();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contadorActual = mapaOpcionesActual.values().stream().mapToInt(Integer::intValue).sum();
            contadorPotencial = mapaOpcionesPotencial.values().stream().mapToInt(Integer::intValue).sum();
            contadorAmbas = mapaOpcionesAmbas.values().stream().mapToInt(Integer::intValue).sum();
        }

        totalOpciones = contadorActual + contadorPotencial + contadorAmbas;

        Map<String, Double> resultados = new HashMap<>();
        resultados.put("Actual", (double) contadorActual);
        resultados.put("Potencial", (double) contadorPotencial);
        resultados.put("Ambas", (double) contadorAmbas);
        resultados.put("Total", (double) totalOpciones);

        if (totalOpciones > 0) {
            resultados.put("ActualPorcentaje", (contadorActual * 100.0) / totalOpciones);
            resultados.put("PotencialPorcentaje", (contadorPotencial * 100.0) / totalOpciones);
            resultados.put("AmbasPorcentaje", (contadorAmbas * 100.0) / totalOpciones);
        } else {
            resultados.put("ActualPorcentaje", 0.0);
            resultados.put("PotencialPorcentaje", 0.0);
            resultados.put("AmbasPorcentaje", 0.0);
        }

        db.close();
        return resultados;
    }

    // Devuelve el número total de preguntas en un grupo específico.
    @SuppressLint("Range")
    public int getTotalQuestionsForGrupo(int grupoNumero, String nombreEmpresa) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Consulta SQL para contar el número total de preguntas en el grupo especificado
        String query = "SELECT COUNT(*) AS total FROM pregunta p " +
                "INNER JOIN elemento e ON p.elemento = e.numero " +
                "WHERE e.grupo = ?";
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(grupoNumero) });

        int total = 0;
        // Verifica si el cursor tiene datos y obtiene el conteo total de preguntas
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return total;
    }

    /// Devuelve el número total de elementos en un grupo específico.
    @SuppressLint("Range")
    public int getTotalElementsForGrupo(int grupoNumero) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Consulta SQL para contar el número total de elementos en el grupo especificado
        String query = "SELECT COUNT(*) AS total FROM elemento e " +
                "WHERE e.grupo = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(grupoNumero)});

        int totalElements = 0;
        // Verifica si el cursor tiene datos y obtiene el conteo total de elementos
        if (cursor.moveToFirst()) {
            totalElements = cursor.getInt(cursor.getColumnIndex("total"));
        }
        cursor.close();
        db.close();

        return totalElements;
    }


    // Devuelve el número total de preguntas respondidas en un grupo específico.
    @SuppressLint("Range")
    public int getAnsweredQuestionsForGrupo(int grupoNumero, String nombreEmpresa) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Consulta SQL para contar el número de preguntas respondidas en el grupo especificado
        String query = "SELECT COUNT(*) AS answered FROM pregunta p " +
                "INNER JOIN elemento e ON p.elemento = e.numero " +
                "INNER JOIN respuesta r ON p.numero = r.pregunta " +
                "WHERE e.grupo = ? AND r.empresa = ?";
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(grupoNumero), nombreEmpresa });

        int answered = 0;
        // Verifica si el cursor tiene datos y obtiene el conteo de preguntas respondidas
        if (cursor.moveToFirst()) {
            answered = cursor.getInt(cursor.getColumnIndex("answered"));
        }
        cursor.close();
        return answered;
    }

    // Devuelve el grupo correspondiente a una posición específica en la lista de grupos.
    @SuppressLint("Range")
    public Grupo getGrupoByPosition(int position) {
        Grupo grupo = null;
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL para obtener el número y nombre del grupo basado en la posición
        String query = "SELECT g.numero, g.nombre FROM grupo g " +
                "ORDER BY g.numero LIMIT 1 OFFSET ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(position)});

        // Verifica si el cursor tiene datos y crea un objeto Grupo con el número y nombre del grupo
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                grupo = new Grupo();
                grupo.setNumero(cursor.getInt(cursor.getColumnIndex("numero")));
                grupo.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
            }
            cursor.close();
        }
        db.close();

        return grupo;
    }

    public int getEmpresasCompletamenteEncuestadasCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            // Query to count the number of companies that have completed all their surveys
            String query = "SELECT COUNT(DISTINCT r.empresa) FROM respuesta r " +
                    "JOIN (SELECT empresa, COUNT(DISTINCT pregunta) as total_preguntas " +
                    "FROM respuesta GROUP BY empresa) t " +
                    "ON r.empresa = t.empresa " +
                    "WHERE t.total_preguntas = (SELECT COUNT(*) FROM pregunta)";
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return count;
    }

    public String insertarTiempo(String nombreEmpresa, boolean esInicio) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("DBHelper", "Base de datos abierta para escritura");

        // Obtener la fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha = sdf.format(new Date());

        ContentValues values = new ContentValues();
        values.put("nombreEmpresa", nombreEmpresa);
        if (esInicio) {
            values.put("fechaInicio", fecha);
            long result = db.insert("fecha", null, values);
            if (result == -1) {
                Log.e("DBHelper", "Error al insertar tiempo de inicio");
            } else {
                Log.d("DBHelper", "Tiempo de inicio insertado correctamente: " + fecha);
            }
        } else {
            values.put("fechaFin", fecha);
            int result = db.update("fecha", values, "nombreEmpresa = ? AND fechaFin IS NULL", new String[]{nombreEmpresa});
            if (result == 0) {
                Log.e("DBHelper", "Error al actualizar tiempo de fin");
            } else {
                Log.d("DBHelper", "Tiempo de fin actualizado correctamente: " + fecha);
            }
        }
        db.close();

        return fecha;
    }

    public Map<String, Long> getTiempoPorDia() {
        SQLiteDatabase db = this.getReadableDatabase();
        Map<String, Long> tiempoPorDia = new HashMap<>();

        String query = "SELECT fechaInicio, fechaFin FROM fecha";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String fechaInicio = cursor.getString(cursor.getColumnIndex("fechaInicio"));
                @SuppressLint("Range") String fechaFin = cursor.getString(cursor.getColumnIndex("fechaFin"));


                if (fechaFin != null) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        Date inicio = format.parse(fechaInicio);
                        Date fin = format.parse(fechaFin);

                        long diff = fin.getTime() - inicio.getTime();
                        long diffHours = TimeUnit.MILLISECONDS.toHours(diff);

                        String dia = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(inicio);

                        if (tiempoPorDia.containsKey(dia)) {
                            tiempoPorDia.put(dia, tiempoPorDia.get(dia) + diffHours);
                        } else {
                            tiempoPorDia.put(dia, diffHours);
                        }
                    } catch (ParseException e) {
                        Log.e("DBHelper", "Error al parsear fechas", e);
                    }
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return tiempoPorDia;
    }

    public Map<String, Integer> obtenerRespuestasPorDia(String empresa) {
        SQLiteDatabase db = this.getReadableDatabase();
        Map<String, Integer> respuestasPorDia = new HashMap<>();

        // Obtener el día de la semana actual
        Calendar calendar = Calendar.getInstance();
        int diaSemanaActual = calendar.get(Calendar.DAY_OF_WEEK);

        // Inicializar el mapa con todos los días de la semana en 0
        String[] diasSemana = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        for (String dia : diasSemana) {
            respuestasPorDia.put(dia, 0);
        }

        // Consulta para obtener las respuestas del día actual
        String query = "SELECT fecha_respuesta, COUNT(*) as cantidad FROM respuesta WHERE empresa = ? AND strftime('%w', fecha_respuesta) = ? GROUP BY fecha_respuesta";
        Cursor cursor = db.rawQuery(query, new String[]{empresa, String.valueOf(diaSemanaActual - 1)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String fecha = cursor.getString(cursor.getColumnIndex("fecha_respuesta"));
                @SuppressLint("Range") int cantidad = cursor.getInt(cursor.getColumnIndex("cantidad"));
                respuestasPorDia.put(diasSemana[diaSemanaActual - 1], cantidad);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return respuestasPorDia;
    }

    public boolean verificarOperador(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM operador WHERE email = ? AND password = ?", new String[]{email, password});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return existe;
    }

    @SuppressLint("Range")
    public Map<String, String> getNombreApellidoPorEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("El valor del email no puede ser nulo");
        }

        Map<String, String> nombreApellido = null;
        String query = "SELECT nombre, apellido FROM operador WHERE email = ?";

        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(query, new String[]{email})) {

            if (cursor != null && cursor.moveToFirst()) {
                nombreApellido = new HashMap<>();
                nombreApellido.put("nombre", cursor.getString(cursor.getColumnIndex("nombre")));
                nombreApellido.put("apellido", cursor.getString(cursor.getColumnIndex("apellido")));
            }
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return nombreApellido;
    }

    @SuppressLint("Range")
    public Map<String, String> getNombreApellidoPorIdOperador(String idOperador) {
        if (idOperador == null) {
            throw new IllegalArgumentException("El valor del idOperador no puede ser nulo");
        }

        Map<String, String> nombreApellido = null;
        String query = "SELECT nombre, apellido FROM operador WHERE id = ?";

        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(query, new String[]{idOperador})) {

            if (cursor != null && cursor.moveToFirst()) {
                nombreApellido = new HashMap<>();
                nombreApellido.put("nombre", cursor.getString(cursor.getColumnIndex("nombre")));
                nombreApellido.put("apellido", cursor.getString(cursor.getColumnIndex("apellido")));
            }
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return nombreApellido;
    }

    @SuppressLint("Range")
    public int getOperadorIdPorEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("El valor del email no puede ser nulo");
        }

        String query = "SELECT id FROM operador WHERE email = ?";
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(query, new String[]{email})) {

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                return id;
            }
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return -1; // o cualquier valor que indique que no se encontró el operador
    }

    public String getOperadorEmailPorId(int idOperador) {
        SQLiteDatabase db = this.getReadableDatabase();
        String email = null;
        Cursor cursor = db.rawQuery("SELECT email FROM operador WHERE id = ?", new String[]{String.valueOf(idOperador)});
        if (cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
        }
        cursor.close();
        return email;
    }

    public String getRepresentanteEmpresa(String nombreEmpresa) {
        if (nombreEmpresa == null) {
            return null; // O cualquier valor predeterminado que consideres adecuado
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT representante_empresa FROM empresa WHERE nombre = ?", new String[]{nombreEmpresa});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String representante = cursor.getString(cursor.getColumnIndex("representante_empresa"));
                cursor.close();
                return representante;
            }
            cursor.close();
        }
        return null;
    }

    public List<String> getAllOperadorNames() {
        List<String> operadorNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre, apellido FROM operador", null);
        if (cursor.moveToFirst()) {
            do {
                String fullName = cursor.getString(0) + " " + cursor.getString(1);
                operadorNames.add(fullName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return operadorNames;
    }

    @SuppressLint("Range")
    public int obtenerIdOperadorPorNombre(String nombreOperador) {
        // Obtén una instancia de la base de datos en modo lectura
        SQLiteDatabase db = this.getReadableDatabase();

        // Define la consulta SQL
        String query = "SELECT id FROM operador WHERE nombre = ?";

        // Ejecuta la consulta
        Cursor cursor = db.rawQuery(query, new String[]{nombreOperador});

        int idOperador = -1;
        if (cursor.moveToFirst()) {
            // Obtén el ID del operador
            idOperador = cursor.getInt(cursor.getColumnIndex("id"));
        }

        // Cierra el cursor y la base de datos
        cursor.close();
        db.close();

        return idOperador;
    }

    public List<Operador> getAllOperadores() {

        List<Operador> v_operadores = new ArrayList<>();

        try (SQLiteDatabase v_db = this.getReadableDatabase()) {

            Cursor v_cursor = v_db.rawQuery("select id, nombre, apellido, cedula, email, password from operador order by id", null);

            if (null != v_cursor) {
                while (v_cursor.moveToNext()) {

                    Operador v_operador = new Operador(
                            v_cursor.getString(1), // nombre
                            v_cursor.getString(2), // apellido
                            v_cursor.getString(3), // cedula
                            v_cursor.getString(4), // email
                            v_cursor.getString(5)  // password
                    );

                    v_operador.setId(v_cursor.getInt(0)); // id

                    v_operadores.add(v_operador);
                }
                v_cursor.close();
            }

        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        return v_operadores;
    }

    @SuppressLint("Range")
    public String getNombreEmpresa(int idEmpresa) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String nombreEmpresa = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT nombre FROM empresa WHERE id = ?", new String[]{String.valueOf(idEmpresa)});
            if (cursor != null && cursor.moveToFirst()) {
                nombreEmpresa = cursor.getString(cursor.getColumnIndex("nombre"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return nombreEmpresa;
    }

    public long[] guardarAsignar(String idEmpresa, String nombreEmpresa, String idOperador, List<String> numerosElementos) {
        // Obtén una instancia de la base de datos en modo escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // Array para almacenar los IDs de las filas insertadas
        long[] newRowIds = new long[numerosElementos.size()];

        // Itera sobre cada número de elemento y realiza la inserción
        for (int i = 0; i < numerosElementos.size(); i++) {
            // Crea un objeto ContentValues para almacenar los valores a insertar
            ContentValues values = new ContentValues();
            values.put("id_operador", idOperador); // Inserta el id del operador
            values.put("id_elemento", String.valueOf(numerosElementos.get(i))); // Inserta el número completo del elemento (con decimales si es necesario)
            values.put("id_empresa", idEmpresa); // Inserta el id de la empresa
            values.put("nombre_empresa", nombreEmpresa); // Inserta el nombre de la empresa

            // Inserta la fila en la tabla y almacena el ID de la fila insertada
            newRowIds[i] = db.insert("asignar", null, values);
        }

        // Cierra la base de datos
        db.close();

        return newRowIds;
    }

    /* -TODO Elementos Asignados sin SyncAdapter .-  ------------------------------------------------------------------------------------------- */

    /*@SuppressLint("Range")
    public List<Elemento> getElementosAsignados(int idOperador, int idEmpresa) {
        List<Elemento> elementosAsignados = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Log.d("DBHelper", "Consultando elementos asignados para idOperador: " + idOperador + ", idEmpresa: " + idEmpresa);

        try {
            // Consulta para obtener los elementos asignados
            Cursor cursor = db.rawQuery(
                    "SELECT e.numero, e.nombre FROM elemento e " +
                            "INNER JOIN asignar a ON e.numero = a.id_elemento " +
                            "WHERE a.id_operador = ? AND a.id_empresa = ?",
                    new String[]{String.valueOf(idOperador), String.valueOf(idEmpresa)}
            );

            if (cursor.moveToFirst()) {
                do {
                    Elemento elemento = new Elemento();
                    elemento.setNumero(cursor.getString(cursor.getColumnIndex("numero")));
                    elemento.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));

                    Log.d("DBHelper", "Elemento obtenido: Número = " + elemento.getNumero() + ", Nombre = " + elemento.getNombre());

                    elementosAsignados.add(elemento);
                } while (cursor.moveToNext());
            } else {
                Log.d("DBHelper", "No se encontraron elementos asignados.");
            }

            cursor.close();
        } catch (Exception e) {
            Log.e("DBHelper", "Error al consultar elementos asignados: ", e);
        } finally {
            db.close();
        }

        Log.d("DBHelper", "Total de elementos asignados encontrados: " + elementosAsignados.size());
        return elementosAsignados;
    }*/

    @SuppressLint("Range")
    public List<Elemento> getElementosDisponibles() {
        List<Elemento> elementosDisponibles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta para obtener los elementos que no están asignados a ningún operador
        String query = "SELECT e.numero, e.nombre FROM elemento e " +
                "LEFT JOIN asignar a ON e.numero = a.id_elemento " +
                "WHERE a.id_elemento IS NULL";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Elemento elemento = new Elemento();
                elemento.setNumero(cursor.getString(cursor.getColumnIndex("numero")));
                elemento.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                elementosDisponibles.add(elemento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return elementosDisponibles;
    }

    /* -TODO Elementos Asignados con SyncAdapter .-  ------------------------------------------------------------------------------------------- */

    @SuppressLint("Range")
    public List<Elemento> getElementosAsignados(int idOperador, int idEmpresa) {
        // Primero, actualiza las asignaciones
        if (syncAdapter != null) {
            syncAdapter.actualizarAsignaciones();
        } else {
            Log.e(TAG, "SyncAdapter es null");
        }

        List<Elemento> elementosAsignados = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Log.d("DBHelper", "Consultando elementos asignados para idOperador: " + idOperador + ", idEmpresa: " + idEmpresa);

        // Verificar registros en la tabla 'asignar' antes de hacer la consulta
        Cursor testCursor = db.rawQuery("SELECT * FROM asignar", null);
        if (testCursor.moveToFirst()) {
            Log.d("DBHelper", "Registros en la tabla asignar:");
            do {
                @SuppressLint("Range") int idOperadorDb = testCursor.getInt(testCursor.getColumnIndex("id_operador"));
                @SuppressLint("Range") int idEmpresaDb = testCursor.getInt(testCursor.getColumnIndex("id_empresa"));
                @SuppressLint("Range") String idElementoDb = testCursor.getString(testCursor.getColumnIndex("id_elemento"));

                Log.d("DBHelper", "idOperador: " + idOperadorDb + ", idEmpresa: " + idEmpresaDb + ", idElemento: " + idElementoDb);
            } while (testCursor.moveToNext());
        } else {
            Log.d("DBHelper", "La tabla asignar está vacía.");
        }
        testCursor.close();

        // Consulta corregida asegurando el orden correcto
        Cursor cursor = db.rawQuery(
                "SELECT e.numero, e.nombre FROM elemento e " +
                        "INNER JOIN asignar a ON e.numero = a.id_elemento " +
                        "WHERE a.id_operador = ? AND a.id_empresa = ?",
                new String[]{String.valueOf(idOperador), String.valueOf(idEmpresa)}
        );

        if (cursor.moveToFirst()) {
            do {
                Elemento elemento = new Elemento();
                elemento.setNumero(cursor.getString(cursor.getColumnIndex("numero")));
                elemento.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));

                Log.d("DBHelper", "Elemento obtenido: Número = " + elemento.getNumero() + ", Nombre = " + elemento.getNombre());

                elementosAsignados.add(elemento);
            } while (cursor.moveToNext());
        } else {
            Log.d("DBHelper", "No se encontraron elementos asignados.");
        }

        cursor.close();
        db.close();

        Log.d("DBHelper", "Total de elementos asignados encontrados: " + elementosAsignados.size());
        return elementosAsignados;
    }

    public int getTotalQuestionsForElemento(String numeroElemento, String nombreEmpresa) {
        int totalQuestions = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta para contar las preguntas de un elemento específico
        String query = "SELECT COUNT(*) FROM pregunta WHERE elemento = ?";
        Cursor cursor = db.rawQuery(query, new String[]{numeroElemento});

        if (cursor.moveToFirst()) {
            totalQuestions = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return totalQuestions;
    }

    public int getAnsweredQuestionsForElemento(String numeroElemento, String nombreEmpresa) {
        if (numeroElemento == null || nombreEmpresa == null) {
            Log.e("DBHelper", "Error: numeroElemento o nombreEmpresa es null");
            return 0; // Evita la consulta si hay valores nulos
        }

        int answeredQuestions = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT COUNT(*) FROM respuesta WHERE elemento = ? AND empresa = ? AND opcion_actual IS NOT NULL";
        Cursor cursor = db.rawQuery(query, new String[]{numeroElemento, nombreEmpresa});

        if (cursor.moveToFirst()) {
            answeredQuestions = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return answeredQuestions;
    }

    @SuppressLint("Range")
    public Elemento getElementoById(String idElemento) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("elemento", new String[]{"numero", "nombre", "grupo"}, "numero = ?", new String[]{idElemento}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Elemento elemento = new Elemento();
            elemento.setNumero(cursor.getString(cursor.getColumnIndex("numero")));
            elemento.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
            elemento.setGrupo(cursor.getInt(cursor.getColumnIndex("grupo")));
            cursor.close();
            return elemento;
        } else {
            return null;
        }
    }

}