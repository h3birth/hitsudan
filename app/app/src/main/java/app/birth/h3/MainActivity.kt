package app.birth.h3

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import app.birth.h3.util.UtilCommon
import app.birth.h3.view.PaintView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var common: UtilCommon
    private val mContext: Context = this
    var setColortext: String = "●"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // カスタムビューを設定
        val customLayout = layoutInflater.inflate(R.layout.dialog_pen_set, null)
        var seekbar_pen_weight : SeekBar = customLayout.findViewById(R.id.seekbar_pen_weight)
        seekbar_pen_weight.setProgress(10)
        val prefer : SharedPreferences = this.getSharedPreferences(getString(R.string.pref_pen_set), Context.MODE_PRIVATE)

        //
        fab_pen_set.setOnClickListener {view ->
            val customLayout = layoutInflater.inflate(R.layout.dialog_pen_set, null)
            var seekbar_pen_weight : SeekBar = customLayout.findViewById(R.id.seekbar_pen_weight)
            seekbar_pen_weight.setProgress(prefer.getInt(getString(R.string.pref_key_pen_weight), 10))

            // 1段目
            var button_set_color_black : Button = customLayout.findViewById(R.id.button_set_color_black)
            var button_set_color_blue : Button = customLayout.findViewById(R.id.button_set_color_blue)
            var button_set_color_green : Button = customLayout.findViewById(R.id.button_set_color_green)
            var button_set_color_orange : Button = customLayout.findViewById(R.id.button_set_color_orange)
            var button_set_color_red : Button = customLayout.findViewById(R.id.button_set_color_red)
            // 2段目
            var button_set_color_brawn : Button = customLayout.findViewById(R.id.button_set_color_brawn)
            var button_set_color_cyan : Button = customLayout.findViewById(R.id.button_set_color_cyan)
            var button_set_color_teal : Button = customLayout.findViewById(R.id.button_set_color_teal)
            var button_set_color_yellow : Button = customLayout.findViewById(R.id.button_set_color_yellow)
            var button_set_color_pink : Button = customLayout.findViewById(R.id.button_set_color_pink)
            // 3段目
            var button_set_color_grey : Button = customLayout.findViewById(R.id.button_set_color_grey)
            var button_set_color_indigo : Button = customLayout.findViewById(R.id.button_set_color_indigo)
            var button_set_color_lime : Button = customLayout.findViewById(R.id.button_set_color_lime)
            var button_set_color_deep_orange : Button = customLayout.findViewById(R.id.button_set_color_deep_orange)
            var button_set_color_people : Button = customLayout.findViewById(R.id.button_set_color_people)

            var setColorButtons : List<Button> = mutableListOf<Button>(
                    button_set_color_black,
                    button_set_color_blue,
                    button_set_color_green,
                    button_set_color_orange,
                    button_set_color_red,
                    button_set_color_brawn,
                    button_set_color_cyan,
                    button_set_color_teal,
                    button_set_color_yellow,
                    button_set_color_pink,
                    button_set_color_grey,
                    button_set_color_indigo,
                    button_set_color_lime,
                    button_set_color_deep_orange,
                    button_set_color_people
            )

            for( (key, btn) in setColorButtons.withIndex() ){
                if( key == prefer.getInt(getString(R.string.pref_key_pen_color), 0)){
                    btn.text = setColortext
                }else {
                    btn.text = ""
                }
                btn.setOnClickListener {
                    for( btn in setColorButtons ){
                        btn.text = ""
                    }
                    setColorButtons.get(key).text = setColortext
                    with (prefer.edit()) {
                        putInt(getString(R.string.pref_key_pen_color), key)
                        commit()
                    }
                }
            }
            AlertDialog.Builder(mContext).apply {
                setView(customLayout)
                setTitle(R.string.setting_title)
                setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, which ->
                    var seekbar_pen_weight : SeekBar = customLayout.findViewById(R.id.seekbar_pen_weight)
                    with (prefer.edit()) {
                        putInt(getString(R.string.pref_key_pen_weight), seekbar_pen_weight.getProgress())
                        commit()
                    }
//                    Toast.makeText(mContext, "POSITIVE"+seekbar_pen_weight.getProgress().toString(), Toast.LENGTH_SHORT).show()
                })
                show()
            }
        }

        // 削除
        fab_delete.setOnClickListener  {view ->
            var paintView : PaintView = findViewById(R.id.paintView)
            paintView.clear()
        }

    }

    fun getCurrentPenWeight(seekBar: SeekBar){
        seekBar.progress
    }

    fun setPenColor(){

    }
}
