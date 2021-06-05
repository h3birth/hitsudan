package app.birth.h3.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
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
    private List<Path> eraserPath = new ArrayList<Path>();
    private SharePreferenceRepository spf;
    Map<Integer, Integer> matchNumberCountMap = new HashMap<>();
    TextView textViewResultNumber;

    /** スレッドUI操作用ハンドラ */
    private Handler mHandler = new Handler();

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
        // TODO 自動生成されたメソッド・スタブ
        for (int i = 0; i < listPath.size(); i++) {
            Path pt = listPath.get(i);
            Paint paint = listPaint.get(i);
            canvas.drawPath(pt, paint);
        }
        // current
        if (current_path != null && current_paint != null) {
            canvas.drawPath(current_path, current_paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                current_path = new Path();
                current_paint = new Paint();
                setPen();
                current_path.moveTo(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                current_path.lineTo(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                current_path.lineTo(x, y);
                listPath.add(current_path);
                listPaint.add(current_paint);
                invalidate();
                break;
        }
        return true;
    }

    public void setWeight(){
        current_paint.setStrokeWidth(spf.getPenWeight());
    }

    public void setPenColor(){
        Color current_color = color.getColorById(spf.getPenColor());
        if(current_color == null) {
            current_color = color.getBlack();
        }
        current_paint.setColor(android.graphics.Color.parseColor(current_color.getCode()));
    }

    public void setPen(){
        current_paint = new Paint();
        setColor();
        current_paint.setStyle(Paint.Style.STROKE);
        current_paint.setStrokeJoin(Paint.Join.ROUND);
        current_paint.setStrokeCap(Paint.Cap.ROUND);
        setWeight();
    }

    public void clear(){
        for (int i = 0; i < listPath.size(); i++) {
            Path pt = listPath.get(i);
            pt.reset();
        }
        listPath.clear();
        listPaint.clear();
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
        current_paint.setColor(android.graphics.Color.parseColor(current_color.getCode()));
    }

    public void setColor() {
        if(eraser) {
            setEraserColor();
        } else {
            setPenColor();
        }
    }
}
