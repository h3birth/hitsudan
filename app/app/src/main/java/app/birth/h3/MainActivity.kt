package app.birth.h3

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import app.birth.h3.databinding.ActivityMainBinding
import app.birth.h3.databinding.DialogPenSetBinding
import app.birth.h3.util.UtilCommon
import app.birth.h3.view.PaintView
import app.birth.h3.view.PenSettingDialogFragment
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PenSettingDialogFragment.Listener, NavigationView.OnNavigationItemSelectedListener {

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

        binding?.drawerLayout?.let {
            val toggle = ActionBarDrawerToggle(
                    this,
                    it,
                    R.string.drawer_open,
                    R.string.drawer_close
            )
            it.addDrawerListener(toggle)
            toggle.syncState()
            it.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
        binding?.navigationView?.setNavigationItemSelectedListener(this)


        binding?.fabDrawer?.setOnClickListener {
            binding?.drawerLayout?.open()
        }
    }

    override fun onClickPositive() {
        viewModel.onSettingBackground()
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }
}
