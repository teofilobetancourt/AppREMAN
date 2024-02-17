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
import com.appreman.app.Models.Empresa;
import com.appreman.app.Models.Pregunta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieChartView extends View {

    private DBHelper dbHelper;
    private List<String> empresaNames;
    private List<Float> data;
    private int[] colors = {Color.rgb(55, 166, 237), Color.rgb(255, 185, 104)}; // Colores para cada porción
    private Paint paint;
    private RectF rectF;
    private String selectedEmpresa = "";
    private Map<String, Float> porcentajes; // Mapa para almacenar los porcentajes de preguntas respondidas por empresa

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
        empresaNames = getAllEmpresaNames();
        data = new ArrayList<>();
        data.add(0f); // Placeholder para el gráfico
        data.add(0f); // Placeholder para el gráfico
        paint = new Paint();
        paint.setAntiAlias(true);
        rectF = new RectF();
        porcentajes = new HashMap<>();
    }

    // Método para obtener nombres de empresas (reemplaza con tu lógica)
    private List<String> getAllEmpresaNames() {
        List<String> empresaNames = new ArrayList<>();
        List<Empresa> empresas = dbHelper.getAllEmpresas();
        for (Empresa empresa : empresas) {
            empresaNames.add(empresa.getNombre());
        }
        return empresaNames;
    }

    // Método para configurar la empresa seleccionada desde el Spinner
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
        float centerY = getHeight() / 3; // Ajusta esta línea para cambiar la posición vertical
        float radius = Math.min(getWidth(), getHeight()) / 2 * 0.8f;

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
        drawLegend(canvas, centerX, centerY + radius + 30, radius);

        if (!selectedEmpresa.isEmpty()) {
            // Mostrar el porcentaje de preguntas respondidas
            paint.setColor(Color.DKGRAY);
            paint.setTextSize(20);
            float porcentaje = porcentajes.get(selectedEmpresa);
            String porcentajeFormateado = String.format("%.2f", porcentaje);
            canvas.drawText("Porcentaje Respondido: " + porcentajeFormateado + "%", centerX - 150, centerY + radius + 70, paint);

            // Mostrar cantidad de preguntas respondidas y las que faltan
            int preguntasRespondidas = dbHelper.getRespuestasCount(selectedEmpresa);
            int totalPreguntas = dbHelper.getAllPreguntas().size();
            int preguntasRestantes = totalPreguntas - preguntasRespondidas;

            String mensaje = "Respondidas: " + preguntasRespondidas +
                    "\nFaltan: " + preguntasRestantes;

            // Ajusta la posición X para centrar el mensaje
            float textWidth = paint.measureText(mensaje);
            canvas.drawText(mensaje, centerX - textWidth / 2, centerY + radius + 100, paint);
        }
    }


    private void drawLegend(Canvas canvas, float centerX, float top, float radius) {
        float legendSize = 30; // Tamaño del cuadrado de la leyenda
        float legendSpacing = 10; // Espaciado entre cuadrados de leyenda
        float legendX = centerX - radius; // Posición X de la leyenda

        // Dibujar cuadrado para respuestas no respondidas
        paint.setColor(colors[0]);
        canvas.drawRect(legendX, top, legendX + legendSize, top + legendSize, paint);
        paint.setTextSize(20);
        paint.setColor(Color.DKGRAY);
        canvas.drawText("Sin Responder", legendX + legendSize + legendSpacing, top + legendSize, paint);

        // Dibujar cuadrado para respuestas respondidas
        paint.setColor(colors[1]);
        canvas.drawRect(legendX, top + legendSize + legendSpacing, legendX + legendSize, top + 2 * legendSize + legendSpacing, paint);
        paint.setTextSize(20);
        paint.setColor(Color.DKGRAY);
        canvas.drawText("Respondido", legendX + legendSize + legendSpacing, top + 2 * legendSize + legendSpacing, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Implementa la lógica del onTouchEvent según tus necesidades
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
