package app.birth.h3

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import app.birth.h3.databinding.ActivityMainBinding
import app.birth.h3.databinding.DialogPenSetBinding
import app.birth.h3.util.BottomToolbarMode
import app.birth.h3.util.UtilCommon
import app.birth.h3.view.PaintView
import app.birth.h3.view.PenSettingDialogFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PenSettingDialogFragment.Listener, NavigationView.OnNavigationItemSelectedListener {

    private val viewModel: MainViewModel by viewModels()
    private var binding: ActivityMainBinding? = null
    private var animator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMessagingToken()
        this.lifecycle.addObserver(viewModel)

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

        binding?.toolbarTabTemp?.setOnClickListener {
            animationBottomToolbar(viewModel.bottomToolbarMode.value == BottomToolbarMode.Close)
        }

        viewModel.onEraser.observe(this, Observer {
            var paintView : PaintView = findViewById(R.id.paintView)
            paintView.setEraser(it)
        })
        viewModel.shownEraser.observe(this, Observer {
            if(it == false) viewModel.onEraser.postValue(false)
        })

        viewModel.backgroundColor.observe(this, Observer {
            Log.d(this.javaClass.simpleName, "background color $it")
            var paintView : PaintView = findViewById(R.id.paintView)
            paintView.changeEraserColor(it)
        })
    }

    override fun onClickPositive() {
        viewModel.onSettingBackground()
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setMessagingToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(this.javaClass.simpleName, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(this.javaClass.simpleName, msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onPause() {
        super.onPause()
        animator?.cancel()
        animator?.removeAllListeners()
    }

    private fun animationBottomToolbar(isShow: Boolean) {
        if (animator != null || animator?.isRunning == true) return

        binding?.toolbarWrap?.let {
            animator = setAnimationProperties(it, isShow)
            animator?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    animator = null
                }

                override fun onAnimationCancel(animation: Animator?) {
                    animator?.cancel()
                    animator = null
                }

                override fun onAnimationStart(animation: Animator?) {
                    viewModel.onClickBottomToolbar()
                }
            })
            animator?.start()
        }
    }

    private fun setAnimationProperties(target: View, isShow: Boolean): ObjectAnimator {
        val fromY = if (isShow) 100f else 0f
        val toY = if (isShow) 0f else 0f

        val translateY = PropertyValuesHolder.ofFloat("translationY", fromY, toY)
        return ObjectAnimator.ofPropertyValuesHolder(target, translateY).apply {
            duration = 300
        }
    }
}
