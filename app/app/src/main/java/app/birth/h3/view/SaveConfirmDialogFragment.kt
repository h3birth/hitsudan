package app.birth.h3.view

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import app.birth.h3.R
import app.birth.h3.databinding.FragmentSaveConfirmDialogBinding

class SaveConfirmDialogFragment(val bitmap: Bitmap) : DialogFragment() {
    companion object {
        val TAG = this.javaClass.simpleName
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = FragmentSaveConfirmDialogBinding.inflate(LayoutInflater.from(activity), null, false)
        binding.lifecycleOwner = this
        binding.bitmap = bitmap


        binding.cancel.setOnClickListener {
            this.dismiss()
        }

        val dialog = AlertDialog.Builder(activity)
                .setView(binding.root)

        return dialog.create()
    }
}
