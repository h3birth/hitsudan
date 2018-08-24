package app.birth.h3.util;

import android.app.Application;
import android.util.Log;

/**
 * Created by k-kobayashi on 2018/05/30.
 */

public class UtilCommon extends Application {
    private static final String TAG = "UtilCommon";
    private boolean isLearning;  // 学習モード
    private int learningNum = 0;

    /**
     * アプリケーションの起動時に呼び出される
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        isLearning = false;
    }

    /**
     * アプリケーション終了時に呼び出される
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate");
        isLearning = false;
    }

    /**
     * グローバル変数の値を変更
     * @param bool 変更する値
     */
    public void setGlobal(boolean bool) {
        Log.d(TAG, "setGlobal");
        isLearning = bool;
    }

    /**
     * グローバル変数の値を取得
     * @return グローバル変数（mGlobal）
     */
    public boolean getGlobal() {
        Log.d(TAG, "getGlobal");
        return isLearning;
    }

    public void setLearningNum(int learningNum){
        this.learningNum = learningNum;
    }

    public int getLearningNum(){
        return this.learningNum;
    }
}
