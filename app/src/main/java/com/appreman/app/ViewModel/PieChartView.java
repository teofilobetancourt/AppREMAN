package com.appreman.app.ViewModel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.appreman.app.Database.DBHelper;
import com.appreman.app.Models.Elemento;
import com.appreman.app.Models.Empresa;
import com.appreman.app.Models.Grupo;
import com.appreman.app.Models.Opcion;
import com.appreman.app.Models.Pregunta;

import java.util.ArrayList;
import java.util.List;

public class PieChartView extends View {

    private DBHelper dbHelper;
    private ArrayList<Float> data = new ArrayList<>();
    private int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN}; // Colores para cada porción
    private Paint paint;
    private RectF rectF;
    private String[] descriptions = {"Grupos", "Elementos", "Preguntas", "Opciones", "Empresas"};
    private String currentDescription = "";
    private List<List<String>> detailedData; // Lista de listas para almacenar los detalles de cada sección

    public PieChartView(Context context) {
        super(context);
        init(context);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        dbHelper = new DBHelper(context);
        // Obtener datos de la base de datos
        List<Grupo> grupos = dbHelper.getAllGrupos();
        List<Elemento> elementos = dbHelper.getAllElementos();
        List<Pregunta> preguntas = dbHelper.getAllPreguntas();
        List<Opcion> opciones = dbHelper.getAllOpciones();
        List<Empresa> empresas = dbHelper.getAllEmpresas(); // Obtener las empresas

        // Calcular los valores para el gráfico
        data.add((float) grupos.size());
        data.add((float) elementos.size());
        data.add((float) preguntas.size());
        data.add((float) opciones.size());
        data.add((float) empresas.size()); // Agregar el número de empresas

        // Obtener detalles para cada sección y almacenarlos en la lista de detalles
        detailedData = new ArrayList<>();
        detailedData.add(getDetailedInfo(dbHelper.getAllGrupos())); // Detalles de Grupos
        detailedData.add(getDetailedInfo(dbHelper.getAllElementos())); // Detalles de Elementos
        detailedData.add(getDetailedInfo(dbHelper.getAllPreguntas())); // Detalles de Preguntas
        detailedData.add(getDetailedInfo(dbHelper.getAllOpciones())); // Detalles de Opciones
        detailedData.add(getDetailedInfo(dbHelper.getAllEmpresas())); // Detalles de Empresas

        paint = new Paint();
        paint.setAntiAlias(true);
        rectF = new RectF();
    }

    private List<String> getDetailedInfo(List<?> items) {
        List<String> details = new ArrayList<>();
        int count = 0;
        for (Object item : items) {
            if (count >= 5) {
                break;
            }

            if (item instanceof Grupo) {
                Grupo grupo = (Grupo) item;
                details.add("Grupo: " + grupo.getNombre());
            } else if (item instanceof Elemento) {
                Elemento elemento = (Elemento) item;
                details.add("Elemento: " + elemento.getNombre());
                // Agrega aquí los campos relevantes de la clase Elemento
            } else if (item instanceof Pregunta) {
                Pregunta pregunta = (Pregunta) item;
                details.add("Pregunta: " + pregunta.getDescripcion());
                // Agrega aquí los campos relevantes de la clase Pregunta
            } else if (item instanceof Opcion) {
                Opcion opcion = (Opcion) item;
                details.add("Opción: " + opcion.getNombre());
                // Agrega aquí los campos relevantes de la clase Opcion
            } else if (item instanceof Empresa) {
                Empresa empresa = (Empresa) item;
                details.add("Empresa: " + empresa.getNombre());
                // Agrega aquí los campos relevantes de la clase Empresa
            }

            count++;
        }
        return details;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        float radius = Math.min(getWidth(), getHeight()) / 2 * 0.8f;

        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        float startAngle = 0;
        for (int i = 0; i < data.size(); i++) {
            paint.setColor(colors[i % colors.length]);
            float sweepAngle = 360 * (data.get(i) / getTotal(data));

            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);

            startAngle += sweepAngle;
        }

        // Dibujar leyenda
        float legendTextSize = 40;
        float legendX = centerX + radius * 1.5f; // Posición X para la leyenda
        float legendY = centerY - radius; // Posición Y inicial para la leyenda
        float lineHeight = legendTextSize * 1.5f;

        paint.setColor(Color.BLACK);
        paint.setTextSize(legendTextSize);

        for (int i = 0; i < descriptions.length; i++) {
            // Dibujar el cuadro de color para cada sección junto con el texto
            paint.setColor(colors[i % colors.length]);
            canvas.drawRect(legendX, legendY, legendX + lineHeight, legendY + lineHeight, paint);
            paint.setColor(Color.BLACK);
            canvas.drawText(descriptions[i], legendX + lineHeight * 1.5f, legendY + legendTextSize, paint);
            legendY += lineHeight * 1.5f; // Incrementar la posición Y para la próxima línea
        }

        // Resto del código para mostrar la información detallada según la selección, si es necesario
        if (!currentDescription.isEmpty()) {
            paint.setColor(Color.BLACK);
            paint.setTextSize(40);
            canvas.drawText(currentDescription, centerX - 150, centerY - radius - 40, paint);

            int selectedIndex = -1;
            for (int i = 0; i < descriptions.length; i++) {
                if (descriptions[i].equals(currentDescription)) {
                    selectedIndex = i;
                    break;
                }
            }
            if (selectedIndex != -1) {
                paint.setColor(Color.DKGRAY);
                paint.setTextSize(25);

                float detailY = centerY - radius + 100; // Posición inicial Y para los detalles
                for (String detail : detailedData.get(selectedIndex)) {
                    canvas.drawText(detail, centerX - 150, detailY, paint);
                    detailY += 30; // Incrementar la posición Y para la próxima línea
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (rectF.contains(x, y)) {
                    float centerX = getWidth() / 2;
                    float centerY = getHeight() / 2;
                    float angle = (float) Math.toDegrees(Math.atan2(y - centerY, x - centerX));
                    angle = (angle < 0) ? (angle + 360) : angle;

                    float currentAngle = 0;
                    for (int i = 0; i < data.size(); i++) {
                        float sweepAngle = 360 * (data.get(i) / getTotal(data));
                        if (angle >= currentAngle && angle < (currentAngle + sweepAngle)) {
                            currentDescription = descriptions[i];
                            invalidate();
                            break;
                        }
                        currentAngle += sweepAngle;
                    }
                } else {
                    currentDescription = "";
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    private float getTotal(List<Float> array) {
        float total = 0;
        for (float value : array) {
            total += value;
        }
        return total;
    }
}
