package app.birth.h3

import android.Manifest
import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import app.birth.h3.databinding.ActivityMainBinding
import app.birth.h3.util.BottomToolbarMode
import app.birth.h3.util.FileUtil
import app.birth.h3.util.ScreenUtil
import app.birth.h3.view.PaintView
import app.birth.h3.view.PenSettingDialogFragment
import app.birth.h3.view.SaveConfirmDialogFragment
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.Exception

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PenSettingDialogFragment.Listener, SaveConfirmDialogFragment.Listener, NavigationView.OnNavigationItemSelectedListener {

    private val viewModel: MainViewModel by viewModels()
    private val premiumViewModel: PremiumViewModel by viewModels()
    private var binding: ActivityMainBinding? = null
    private var animator: ObjectAnimator? = null

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    private val FIREBASE_AUTH = 10

    private lateinit var fileUtil: FileUtil

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMessagingToken()
        this.lifecycle.addObserver(viewModel)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel

        fileUtil = FileUtil(this)

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

        binding?.save?.setOnClickListener {
            if (allPermissionsGranted()) {
                savePNG()
            } else {
                this.requestPermissions(REQUIRED_PERMISSIONS, REQUEST_EXTERNAL_STORAGE)
            }
        }

        binding?.share?.setOnClickListener {
            if (allPermissionsGranted()) {
                sharePNG()
            } else {
                this.requestPermissions(REQUIRED_PERMISSIONS, REQUEST_EXTERNAL_STORAGE)
            }
        }

        binding?.sperk?.setOnClickListener {
            val bitmap = premiumViewModel.compressBitmap(createBitmap())
            try {
                val base64encoded = premiumViewModel.encodeBitmapToBase64(bitmap)
                premiumViewModel.functionsInitilize()
                premiumViewModel.annotateImage(base64encoded, {
                    val text = premiumViewModel.annotationText(it)
                    Timber.d("annotetion text $text")
                    premiumViewModel.ttsInitilize()
                    premiumViewModel.queueSperk(text.trim())
                }, {
                    Timber.d("failed annotateImage")
                })
            } catch (e: Exception) {
                Timber.d("exception")
                Timber.e(e)
            }
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

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE)
            if (allPermissionsGranted()) {
                savePNG()
            } else {
                // パーミッション許可されなかった時、前の画面に戻す
                Toast.makeText(this, "アプリ設定からカメラとストレージの権限を許可してください。", Toast.LENGTH_SHORT).show()
            }
    }

    private fun sharePNG() {
        fileUtil.saveFile(createBitmap(), onSuccess = {
            if(it == null) return@saveFile Toast.makeText(this, "ファイル保存に失敗しました。アクセス権限を確認してください。", Toast.LENGTH_SHORT).show()

            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/png"

            share.putExtra(Intent.EXTRA_STREAM, it)
            startActivity(Intent.createChooser(share, "Share Image"))
        }, onFailed = {
            Toast.makeText(this, "ファイル保存に失敗しました。アクセス権限を確認してください。", Toast.LENGTH_SHORT).show()
        })
    }

    private fun createBitmap(): Bitmap {
        val screenUtil = ScreenUtil(this)
        val screenSize = screenUtil.getScreenSize()

        val mBitmap = Bitmap.createBitmap(screenSize.x, screenSize.y, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mBitmap)

        var paintView : PaintView = findViewById(R.id.paintView)
        paintView.draw(canvas)

        return mBitmap
    }

    private fun savePNG() {
        val mBitmap = createBitmap()
        SaveConfirmDialogFragment(this, mBitmap).show(supportFragmentManager, SaveConfirmDialogFragment.TAG)
    }

    override fun onClickPositive() {
        viewModel.onSettingBackground()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        when(item.getItemId()) {
            R.id.login -> launchFirebaseUI()
            else -> {}
        }
        return true
    }

    private fun launchFirebaseUI() {
        startActivityForResult(premiumViewModel.intentFirebaseUI(), FIREBASE_AUTH)
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

    override fun onSave(bitmap: Bitmap) {
        fileUtil.saveFile(bitmap, onSuccess = {
            Toast.makeText(this, "ファイル保存しました", Toast.LENGTH_SHORT).show()
        }, onFailed = {
            Toast.makeText(this, "ファイル保存に失敗しました。アクセス権限を確認してください。", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == FIREBASE_AUTH) {
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK) {
                FirebaseAuth.getInstance().currentUser?.let {
                    premiumViewModel.saveUser(it)
                    Toast.makeText(this, "ログインしました", Toast.LENGTH_SHORT).show()
                } ?:
                    Toast.makeText(this, "ユーザー情報の取得に失敗しました", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "認証に失敗しました", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "認証に失敗しました", Toast.LENGTH_SHORT).show()
        }
    }
}
