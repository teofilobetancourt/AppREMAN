/*package com.appreman.app.ViewModel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.appreman.app.Database.DBHelper;
import com.appreman.app.Models.Pregunta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieChartView extends View {

    private DBHelper dbHelper;
    private List<String> empresaNames;
    private List<Float> data;
    private int[] colors = {Color.rgb(55, 166, 237), Color.rgb(255, 185, 104)};
    private Paint paint;
    private RectF rectF;
    private String selectedEmpresa = "";
    private Map<String, Float> porcentajes;

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
        empresaNames = dbHelper.getAllEmpresaNames();
        data = new ArrayList<>();
        data.add(0f);
        data.add(0f);
        paint = new Paint();
        paint.setAntiAlias(true);
        rectF = new RectF();
        porcentajes = new HashMap<>();
    }

    public void setSelectedEmpresa(String empresa) {
        selectedEmpresa = empresa;
        updateChartData();
        invalidate();
    }

    private void updateChartData() {
        // Obtener todas las preguntas de la base de datos
        List<Pregunta> preguntas = dbHelper.getAllPreguntas();

        // Obtener las preguntas respondidas para la empresa seleccionada
        int preguntasRespondidas = dbHelper.getRespuestasCount(selectedEmpresa);

        // Calcular el porcentaje de preguntas respondidas
        float porcentajeRespondido = (preguntasRespondidas / (float) preguntas.size()) * 100;

        // Actualizar el mapa de porcentajes
        porcentajes.put(selectedEmpresa, porcentajeRespondido);

        // Actualizar los datos para el gráfico
        data.clear();
        data.add(100 - porcentajeRespondido); // Porcentaje de preguntas no respondidas
        data.add(porcentajeRespondido); // Porcentaje de preguntas respondidas
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2;
        float centerY = getMeasuredHeight() / 2 - 30; // Subir un poco el gráfico
        float radius = Math.min(getWidth(), getMeasuredHeight()) / 2 * 0.7f; // Ajustar el radio

        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        float startAngle = 0;
        for (int i = 0; i < data.size(); i++) {
            paint.setColor(colors[i % colors.length]);

            float sweepAngle = 360 * (data.get(i) / getTotal(data));

            paint.setShadowLayer(10, 0, 0, Color.BLACK);
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
            paint.setShadowLayer(0, 0, 0, 0);

            startAngle += sweepAngle;
        }

        // Dibujar leyenda de colores debajo del PieChart
        drawLegend(canvas, centerX, centerY + radius + 50, radius); // Ajustar posición de leyenda

        if (!selectedEmpresa.isEmpty()) {
            // Mostrar el porcentaje de preguntas respondidas
            paint.setColor(Color.DKGRAY);
            paint.setTextSize(22); // Ajustar el tamaño de texto
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD)); // Hacer el texto en negrita
            float porcentaje = porcentajes.get(selectedEmpresa);
            String porcentajeFormateado = String.format("%.2f", porcentaje);

            // Calcular la posición ajustada para centrar el texto
            float textWidth = paint.measureText(porcentajeFormateado + "%");
            float adjustedX = centerX - textWidth / 2;

            canvas.drawText(porcentajeFormateado + "%", adjustedX, centerY + radius + 70, paint); // Ajuste vertical
            canvas.drawText("Total respondidas: " + dbHelper.getRespuestasCount(selectedEmpresa), adjustedX, centerY + radius + 100, paint); // Ajuste vertical
            canvas.drawText("Faltando por responder: " + (377 - dbHelper.getRespuestasCount(selectedEmpresa)), adjustedX, centerY + radius + 130, paint); // Ajuste vertical
        }
    }

    private void drawLegend(Canvas canvas, float centerX, float top, float radius) {
        float legendSize = 40; // Tamaño mayor para los cuadros de la leyenda
        float legendSpacing = 15; // Espacio entre los cuadros y el texto
        float textSize = 24; // Tamaño de fuente mayor para el texto
        float textOffset = 10; // Desplazamiento del texto desde el centro del cuadro

        paint.setTextSize(textSize);
        paint.setColor(Color.DKGRAY);

        // Posición inicial para la leyenda
        float legendX = centerX - radius;
        float legendY = top;

        // Dibujar cuadro para "Preguntas"
        paint.setColor(colors[0]);
        canvas.drawRoundRect(legendX, legendY, legendX + legendSize, legendY + legendSize, 10, 10, paint);

        // Dibujar texto para "Preguntas"
        paint.setColor(Color.BLACK);
        canvas.drawText("Preguntas", legendX + legendSize + legendSpacing, legendY + legendSize / 2 + textOffset, paint);

        // Actualizar posición para el siguiente cuadro
        legendY += legendSize + legendSpacing;

        // Dibujar cuadro para "Respuestas"
        paint.setColor(colors[1]);
        canvas.drawRoundRect(legendX, legendY, legendX + legendSize, legendY + legendSize, 10, 10, paint);

        // Dibujar texto para "Respuestas"
        paint.setColor(Color.BLACK);
        canvas.drawText("Respuestas", legendX + legendSize + legendSpacing, legendY + legendSize / 2 + textOffset, paint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
*/