package app.birth.h3.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
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
    private boolean flg = false;
    UtilCommon common;
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
        SharedPreferences prefer = context.getSharedPreferences(context.getString(R.string.pref_pen_set), Context.MODE_PRIVATE);
        current_paint.setStrokeWidth(prefer.getInt(context.getString(R.string.pref_key_pen_weight), 10));
    }

    public void setColor(){
        SharedPreferences prefer = context.getSharedPreferences(context.getString(R.string.pref_pen_set), Context.MODE_PRIVATE);
        Integer current_color = prefer.getInt(context.getString(R.string.pref_key_pen_color), 0);
        if( current_color == 0 ){ // くろ
            current_paint.setColor(getResources().getColor(R.color.colorPenBlack));
        }else if( current_color == 1 ){ // 青
            current_paint.setColor(getResources().getColor(R.color.colorPenBlue));
        }else if( current_color == 2 ){ // みどり
            current_paint.setColor(getResources().getColor(R.color.colorPenGreen));
        }else if( current_color == 3 ){ // オレンジ
            current_paint.setColor(getResources().getColor(R.color.colorPenOrange));
        }else if( current_color == 4 ){ // 赤
            current_paint.setColor(getResources().getColor(R.color.colorPenRed));
        }else if( current_color == 5 ){ // 茶色
            current_paint.setColor(getResources().getColor(R.color.colorPenBrawn));
        }else if( current_color == 6 ){ // シアン
            current_paint.setColor(getResources().getColor(R.color.colorPenCyan));
        }else if( current_color == 7 ){ // テール
            current_paint.setColor(getResources().getColor(R.color.colorPenTeal));
        }else if( current_color == 8 ){ // 黄色
            current_paint.setColor(getResources().getColor(R.color.colorPenYellow));
        }else if( current_color == 9 ){ // ピンク
            current_paint.setColor(getResources().getColor(R.color.colorPenPink));
        }else if( current_color == 10 ){ // グレイ
            current_paint.setColor(getResources().getColor(R.color.colorPenGrey));
        }else if( current_color == 11 ){ // インディゴ
            current_paint.setColor(getResources().getColor(R.color.colorPenIndigo));
        }else if( current_color == 12 ){ // ライム
            current_paint.setColor(getResources().getColor(R.color.colorPenLime));
        }else if( current_color == 13 ){ // ディープオレンジ
            current_paint.setColor(getResources().getColor(R.color.colorPenDeepOrange));
        }else if( current_color == 14 ){ // 紫
            current_paint.setColor(getResources().getColor(R.color.colorPenPeople));
        }
    }

    public void setPen(){
        current_paint = new Paint();
//        current_paint.setColor(getResources().getColor(R.color.colorFab));
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
}
