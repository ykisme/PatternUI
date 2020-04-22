package yk.pattern_ui.wx_options_menu;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.graphics.PixelFormat.OPAQUE;

class WXOptionsMenuDrawable extends Drawable {
    private int color;
    private Paint paint;
    private float triangleHeight;
    private float roundRectRadius;
    private float triangleLength;
    private float triangleLeft;

    public void setColor(@ColorInt int color) {
        this.color = color;
        paint.setColor(color);
        invalidateSelf();
    }

    void setTriangleHeight(float triangleHeight) {
        this.triangleHeight = triangleHeight;
        triangleLength = (float) (triangleHeight / Math.tan(Math.PI / 5));
        invalidateSelf();
    }

    void setTriangleLeft(float triangleLeft) {
        this.triangleLeft = triangleLeft;
        invalidateSelf();
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        int width = right - left;
        if (triangleLeft - 0 < .00000001f) {
            triangleLeft = width * 0.8f;
            setTriangleLeft(triangleLeft);
        }
    }

    void setRoundRectRadius(float roundRectRadius) {
        this.roundRectRadius = roundRectRadius;
        invalidateSelf();
    }

    WXOptionsMenuDrawable() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        setColor(color);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        canvas.drawRoundRect(bounds.left, triangleHeight, bounds.right
                , bounds.bottom, roundRectRadius, roundRectRadius, paint);

        //draw triangle
        PointF triCenter = new PointF(this.triangleLeft, 0);
        PointF triLeft = new PointF(triCenter.x - triangleLength / 2, triangleHeight);
        drawRoundTriangle(canvas, triLeft, triCenter);
    }

    private PointF getPointOnLine(PointF linePoint1, PointF linePoint2, float percent) {
        PointF ret = new PointF();
        ret.x = linePoint1.x + percent * (linePoint2.x - linePoint1.x);
        ret.y = linePoint1.y + percent * (linePoint2.y - linePoint1.y);
        return ret;
    }

    private PointF getSymmetryPointH(PointF center, PointF point) {
        PointF p = new PointF();
        p.x = center.x + (center.x - point.x);
        p.y = point.y;
        return p;
    }

    private void drawRoundTriangle(Canvas canvas, PointF left, PointF center) {
        canvas.translate(0, this.triangleHeight * 0.1f);
        Path path = new Path();
        float A = (center.x - left.x) / 2f;
        PointF p1 = new PointF(left.x - A * 0.1F, left.y);
        PointF p2 = new PointF(left.x, left.y);
        PointF p3 = getPointOnLine(left, center, 0.15f);
        PointF p4 = getPointOnLine(left, center, 0.80f);
        PointF p5 = new PointF(center.x, center.y + A * 0.1f);
        PointF p6 = getSymmetryPointH(center, p4);
        PointF p7 = getSymmetryPointH(center, p3);
        PointF p8 = getSymmetryPointH(center, p2);
        PointF p9 = getSymmetryPointH(center, p1);
        path.moveTo(p1.x, p1.y);
        path.quadTo(p2.x, p2.y, p3.x, p3.y);
        path.lineTo(p4.x, p4.y);
        path.quadTo(p5.x, p5.y, p6.x, p6.y);
        path.lineTo(p7.x, p7.y);
        path.quadTo(p8.x, p8.y, p9.x, p9.y);
        path.close();
        canvas.drawPath(path, paint);
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return OPAQUE;
    }
}
