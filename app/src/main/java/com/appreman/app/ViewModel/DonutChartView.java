package com.appreman.app.ViewModel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.appreman.app.Database.DBHelper;

import java.util.List;

public class DonutChartView extends View {

    private List<Float> data;
    private int[] colors = {Color.rgb(55, 166, 237), Color.rgb(255, 185, 104)};
    private Paint paint;
    private RectF rectF;
    private DBHelper dbHelper;

    public DonutChartView(Context context) {
        super(context);
        init(context);
    }

    public DonutChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DonutChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        rectF = new RectF();
        dbHelper = new DBHelper(context);
    }

    public void setData(List<Float> data) {
        this.data = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (data == null || data.size() == 0) {
            return;
        }

        // Ajustar el tamaño del dashboard en relación con el ancho y alto de la vista
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        float radius = Math.min(getWidth(), getHeight()) / 2 * 0.8f; // Ajuste el factor para hacer el donut más grande

        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        float startAngle = 0;

        // Obtener la cantidad de empresas y respuestas de empresas desde DBHelper
        int empresasCount = dbHelper.getEmpresasCount();
        int respuestasEmpresasCount = dbHelper.getRespuestasEmpresasCount();

        float totalEmpresas = empresasCount + respuestasEmpresasCount;
        float porcentajeEmpresas = (empresasCount / totalEmpresas) * 100;
        float porcentajeRespuestasEmpresas = (respuestasEmpresasCount / totalEmpresas) * 100;

        float[] newData = {porcentajeEmpresas, porcentajeRespuestasEmpresas};

        for (int i = 0; i < newData.length; i++) {
            paint.setColor(colors[i % colors.length]);

            float sweepAngle = 360 * (newData[i] / getTotal(newData));

            paint.setShadowLayer(10, 0, 0, Color.BLACK);
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
            paint.setShadowLayer(0, 0, 0, 0);

            startAngle += sweepAngle;
        }

        // Dibujar un círculo blanco en el centro para hacer un agujero
        paint.setColor(Color.WHITE);
        canvas.drawCircle(centerX, centerY, radius / 2, paint);

        // Dibujar leyenda
        drawLegend(canvas, centerX, centerY, radius);
    }

    private void drawLegend(Canvas canvas, float centerX, float centerY, float radius) {
        float legendSize = 50; // Ajuste el tamaño de la leyenda
        float legendSpacing = 20; // Ajuste el espacio entre leyendas

        float totalLegendHeight = colors.length * (legendSize + legendSpacing) - legendSpacing;
        float legendX = centerX - radius;
        float legendY = centerY + radius + legendSpacing; // Agrega un espacio entre el círculo y la leyenda

        // Dibujar cuadrados de colores y etiquetas
        for (int i = 0; i < colors.length; i++) {
            paint.setColor(colors[i]);

            float legendBottom = legendY + legendSize;

            // Ajustar la posición vertical según la altura del texto
            float textHeight = paint.getFontMetrics().bottom;
            float textAdjustment = (legendSize - textHeight) / 2;

            canvas.drawRect(legendX, legendY, legendX + legendSize, legendBottom, paint);

            paint.setColor(Color.DKGRAY);
            paint.setTextSize(30); // Ajuste el tamaño del texto de la leyenda
            canvas.drawText((i == 0 ? "Empresas Agregadas" : "Empresas Encuestadas"),
                    legendX + legendSize + legendSpacing,
                    legendBottom - textAdjustment, // Ajustar la posición vertical del texto
                    paint);

            // Incrementar la posición Y para el próximo conjunto de leyendas
            legendY = legendBottom + legendSpacing;
        }
    }



    private float getTotal(float[] array) {
        float total = 0;
        for (float value : array) {
            total += value;
        }
        return total;
    }
}
