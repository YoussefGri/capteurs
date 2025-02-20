package com.example.capteurs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BouleView extends View {

    private Paint paint;
    private float x, y; // Position de la boule
    private int radius = 50; // Rayon de la boule
    private int color = Color.GREEN; // Couleur par défaut

    public BouleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Initialiser la position de la boule au centre de l'écran
        x = w / 2f;
        y = h / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(x, y, radius, paint);
    }

    public void updatePosition(float deltaX, float deltaY) {
        // Mettre à jour la position de la boule de manière progressive
        x += deltaX;
        y += deltaY;

        // Limiter la position de la boule pour qu'elle reste dans les limites de l'écran
        x = Math.max(radius, Math.min(x, getWidth() - radius));
        y = Math.max(radius, Math.min(y, getHeight() - radius));

        invalidate(); // Redessiner la vue
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
        invalidate(); // Redessiner la vue
    }
}