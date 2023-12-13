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
import com.appreman.app.Models.Grupo;
import com.appreman.app.Models.Opcion;
import com.appreman.app.Models.Pregunta;
import com.appreman.app.Models.Empresa;

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
        List<Opcion> opciones = dbHelper.getAllOpcines();
        List<Empresa> empresas = dbHelper.getAllEmpresas(); // Obtener las empresas

        // Calcular los valores para el gráfico
        data.add((float) grupos.size());
        data.add((float) elementos.size());
        data.add((float) preguntas.size());
        data.add((float) opciones.size());
        data.add((float) empresas.size()); // Agregar el número de empresas

        paint = new Paint();
        paint.setAntiAlias(true);
        rectF = new RectF();
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

        if (!currentDescription.isEmpty()) {
            paint.setColor(Color.BLACK);
            paint.setTextSize(40);
            canvas.drawText(currentDescription, centerX - 150, centerY - radius - 40, paint);
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
