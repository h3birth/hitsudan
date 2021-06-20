package app.birth.h3.feature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import app.birth.h3.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StorageImagesActivity : AppCompatActivity() {
    private val viewModel: StorageImagesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage_images)

        viewModel.init(this)

        viewModel.loadImage()
    }
}
