package app.birth.h3.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import app.birth.h3.R
import app.birth.h3.databinding.DialogPenSetBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PenSettingDialogFragment(val listener: Listener): DialogFragment() {
    companion object {
        val TAG = this.javaClass.simpleName
    }
    interface Listener {
        fun onClickPositive()
    }
    private val viewModel: PenSettingViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogPenSetBinding.inflate(LayoutInflater.from(activity), null, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.penColorPicker.setOnColorChangedListener {
            viewModel.setPenColorOnPicker(it)
        }
        viewModel.hexCodePerseLong()?.let {
            binding.penColorPicker.setColor(it)
        }

        val dialog = AlertDialog.Builder(activity)
                .setView(binding.root)
                .setPositiveButton("OK") { dialogInterface, _ ->
                    viewModel.onComplete()
                    listener.onClickPositive()
                    dialogInterface.dismiss()
                }

        return dialog.create()
    }
}
