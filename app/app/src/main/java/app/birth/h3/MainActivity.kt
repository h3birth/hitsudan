package app.birth.h3

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.birth.h3.databinding.ActivityMainBinding
import app.birth.h3.databinding.DialogPenSetBinding
import app.birth.h3.util.UtilCommon
import app.birth.h3.view.PaintView
import app.birth.h3.view.PenSettingDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PenSettingDialogFragment.Listener {

    private val viewModel: MainViewModel by viewModels()
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel

        binding?.fabPenSet?.setOnClickListener {_ ->
            PenSettingDialogFragment(this).show(supportFragmentManager, PenSettingDialogFragment.TAG)
        }

        // 削除
        binding?.fabDelete?.setOnClickListener {_ ->
            var paintView : PaintView = findViewById(R.id.paintView)
            paintView.clear()
        }
    }

    override fun onClickPositive() {
        viewModel.onSettingBackground()
    }
}
