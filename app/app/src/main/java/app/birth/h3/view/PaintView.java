package app.birth.h3.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.birth.h3.R;
import app.birth.h3.model.Color;
import app.birth.h3.repository.ColorRepository;
import app.birth.h3.repository.ColorRepositoryImpl;
import app.birth.h3.repository.SharePreferenceRepository;
import app.birth.h3.repository.SharePreferenceRepositoryImpl;
import app.birth.h3.util.ScreenUtil;
import app.birth.h3.util.UtilCommon;


/**
 * Created by k-kobayashi on 2018/05/29.
 */

public class PaintView extends View {
    private Context context;
    private Paint current_paint;
    private Path current_path;
    private List<Path> listPath = new ArrayList<Path>();
    private List<Paint> listPaint = new ArrayList<Paint>();
    private ColorRepository color = new ColorRepositoryImpl();
    private boolean flg = false;
    private boolean eraser = false;
    private Path eraserPath;
    private Paint eraserPaint;
    private List<Path> listEraserPath = new ArrayList<Path>();
    private List<Paint> listEraserPaint = new ArrayList<Paint>();
    private List<Path> zIndexPath = new ArrayList<Path>();
    private List<Paint> zIndexPaint = new ArrayList<Paint>();
    private SharePreferenceRepository spf;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        spf = new SharePreferenceRepositoryImpl(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Pen
        for (int i = 0; i < zIndexPath.size(); i++) {
            Path pt = zIndexPath.get(i);
            Paint paint = zIndexPaint.get(i);
            canvas.drawPath(pt, paint);
        }
        if(eraser) {
            if (eraserPath != null && eraserPaint != null) {
                canvas.drawPath(eraserPath, eraserPaint);
            }
        } else {
            if (current_path != null && current_paint != null) {
                canvas.drawPath(current_path, current_paint);
            }
        }
    }

    private void initilize() {
        if(eraser) {
            eraserPath = new Path();
            eraserPaint = new Paint();
        } else {
            current_path = new Path();
            current_paint = new Paint();
        }
    }

    private void moveTo(float x, float y) {
        if(eraser) {
            eraserPath.moveTo(x, y);
        } else {
            current_path.moveTo(x, y);
        }
    }

    private void lineTo(float x, float y) {
        if(eraser) {
            eraserPath.lineTo(x, y);
        } else {
            current_path.lineTo(x, y);
        }
    }

    private void addList() {
        if(eraser) {
            listEraserPath.add(eraserPath);
            listEraserPaint.add(eraserPaint);
            zIndexPath.add(eraserPath);
            zIndexPaint.add(eraserPaint);
        } else {
            listPath.add(current_path);
            listPaint.add(current_paint);
            zIndexPath.add(current_path);
            zIndexPaint.add(current_paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initilize();
                setPen();
                moveTo(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                lineTo(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                lineTo(x, y);
                addList();
                invalidate();
                break;
        }
        return true;
    }

    public void setWeight(){
        if(eraser) {
            eraserPaint.setStrokeWidth(spf.getPenWeight());
        } else {
            current_paint.setStrokeWidth(spf.getPenWeight());
        }
    }

    public void setPenColor(){
        Color current_color = color.getColorById(spf.getPenColor());
        if(current_color == null) {
            current_color = color.getBlack();
        }
        current_paint.setColor(android.graphics.Color.parseColor(current_color.getCode()));
    }

    public void setPen(){
        setColor();
        if(eraser) {
            eraserPaint.setStyle(Paint.Style.STROKE);
            eraserPaint.setStrokeJoin(Paint.Join.ROUND);
            eraserPaint.setStrokeCap(Paint.Cap.ROUND);
        } else {
            current_paint.setStyle(Paint.Style.STROKE);
            current_paint.setStrokeJoin(Paint.Join.ROUND);
            current_paint.setStrokeCap(Paint.Cap.ROUND);
        }
        setWeight();
    }

    public void clear(){
        for (int i = 0; i < listPath.size(); i++) {
            Path pt = listPath.get(i);
            pt.reset();
        }
        for (int i = 0; i < listEraserPath.size(); i++) {
            Path pt = listEraserPath.get(i);
            pt.reset();
        }
        listPath.clear();
        listPaint.clear();
        listEraserPath.clear();
        listEraserPaint.clear();
        invalidate();
    }

    public void setEraser(boolean isEraser) {
        eraser = isEraser;
    }

    public void setEraserColor() {
        Color current_color = color.getColorById(spf.getBackgroundColor());
        if(current_color == null) {
            current_color = color.getWhite();
        }
        eraserPaint.setColor(android.graphics.Color.parseColor(current_color.getCode()));
    }

    public void setColor() {
        if(eraser) {
            setEraserColor();
        } else {
            setPenColor();
        }
    }

    public void changeEraserColor(String colorCode) {
        // 消しゴム
        for (int i = 0; i < listEraserPaint.size(); i++) {
            Paint paint = listEraserPaint.get(i);
            paint.setColor(android.graphics.Color.parseColor(colorCode));
        }
//        invalidate();
    }
}
