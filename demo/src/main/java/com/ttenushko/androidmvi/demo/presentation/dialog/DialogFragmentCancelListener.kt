package com.ttenushko.androidmvi.demo.presentation.dialog

import android.content.DialogInterface
import androidx.fragment.app.DialogFragment


interface DialogFragmentCancelListener {
    fun onDialogFragmentCancel(dialogFragment: DialogFragment, dialog: DialogInterface)
}