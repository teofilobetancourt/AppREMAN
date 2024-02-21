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

import java.util.List;

public class DonutChartView extends View {

    private List<Float> data;
    private int[] colors = {Color.rgb(55, 166, 237), Color.rgb(255, 185, 104)};
    private Paint paint;
    private RectF rectF;
    private DBHelper dbHelper;

    private String selectedSectorName = "";

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
        float radius = Math.min(getWidth(), getHeight()) / 2 * 0.9f; // Ajuste el factor para hacer el donut más grande

        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        float startAngle = 0;

        for (int i = 0; i < data.size(); i++) {
            paint.setColor(colors[i % colors.length]);

            float sweepAngle = 360 * (data.get(i) / getTotal(data));

            paint.setShadowLayer(10, 0, 0, Color.BLACK);
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
            paint.setShadowLayer(0, 0, 0, 0);

            // Verificar si se tocó el área del sector actual
            if (isTouchInSector(centerX, centerY, radius, startAngle, sweepAngle)) {
                selectedSectorName = (i == 0) ? "Empresas Agregadas" : "Empresas Encuestadas";
            }

            startAngle += sweepAngle;
        }

        // Dibujar un círculo blanco en el centro para hacer un agujero
        paint.setColor(Color.WHITE);
        canvas.drawCircle(centerX, centerY, radius / 2, paint);

        // Dibujar el nombre del sector seleccionado
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        float textX = centerX - paint.measureText(selectedSectorName) / 2;
        float textY = centerY;
        canvas.drawText(selectedSectorName, textX, textY, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            invalidate(); // Redibujar para actualizar el nombre del sector seleccionado
        }
        return true;
    }

    private boolean isTouchInSector(float centerX, float centerY, float radius, float startAngle, float sweepAngle) {
        float touchX = getXFromAngle(centerX, radius, startAngle + sweepAngle / 2);
        float touchY = getYFromAngle(centerY, radius, startAngle + sweepAngle / 2);

        return rectF.contains(touchX, touchY);
    }

    private float getXFromAngle(float centerX, float radius, float angle) {
        return centerX + (float) (radius * Math.cos(Math.toRadians(angle)));
    }

    private float getYFromAngle(float centerY, float radius, float angle) {
        return centerY + (float) (radius * Math.sin(Math.toRadians(angle)));
    }

    private float getTotal(List<Float> array) {
        float total = 0;
        for (float value : array) {
            total += value;
        }
        return total;
    }
}
