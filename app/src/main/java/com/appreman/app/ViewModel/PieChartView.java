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
    private int preguntasRespondidas;  // Declarar la variable en la clase

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
        // Obtener las preguntas respondidas para la empresa seleccionada
        int preguntasRespondidas = dbHelper.getRespuestasCount(selectedEmpresa);

        // Calcular el porcentaje de preguntas respondidas
        float porcentajeRespondido = ((float) preguntasRespondidas / 377) * 100;

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

            // Calcular la posición ajustada para centrar el texto
            float textWidth = paint.measureText(porcentajeFormateado + "%");
            float adjustedX = centerX - textWidth / 2;

            canvas.drawText(porcentajeFormateado + "%", adjustedX, centerY + radius + 70, paint);

            // Mostrar cantidad de preguntas respondidas y las que faltan en líneas separadas
            int totalPreguntas = 377;
            int preguntasRespondidas = dbHelper.getRespuestasCount(selectedEmpresa);
            int preguntasRestantes = totalPreguntas - preguntasRespondidas;

            // Mensaje para total respondidas
            String totalRespondidas = "Total respondidas: " + preguntasRespondidas;
            float totalRespondidasWidth = paint.measureText(totalRespondidas);
            float adjustedTotalX = centerX - totalRespondidasWidth / 2;
            canvas.drawText(totalRespondidas, adjustedTotalX, centerY + radius + 100, paint);

            // Mensaje para por responder
            String porResponder = "Faltando por responder: " + preguntasRestantes;
            float porResponderWidth = paint.measureText(porResponder);
            float adjustedPorResponderX = centerX - porResponderWidth / 2;
            canvas.drawText(porResponder, adjustedPorResponderX, centerY + radius + 130, paint);
        }
    }


    private void drawLegend(Canvas canvas, float centerX, float top, float radius) {
        float legendSize = 30;
        float legendSpacing = 10;
        float legendX = centerX - radius;

        paint.setColor(colors[0]);
        canvas.drawRect(legendX, top, legendX + legendSize, top + legendSize, paint);
        paint.setTextSize(20);
        paint.setColor(Color.DKGRAY);
        canvas.drawText("Preguntas", legendX + legendSize + legendSpacing, top + legendSize, paint);

        paint.setColor(colors[1]);
        canvas.drawRect(legendX, top + legendSize + legendSpacing, legendX + legendSize, top + 2 * legendSize + legendSpacing, paint);
        paint.setTextSize(20);
        paint.setColor(Color.DKGRAY);
        canvas.drawText("Respuestas", legendX + legendSize + legendSpacing, top + 2 * legendSize + legendSpacing, paint);
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
