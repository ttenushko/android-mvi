package com.ttenushko.androidmvi.demo.presentation.dialog

import android.content.DialogInterface

interface DialogFragmentDismissListener {
    fun onDialogFragmentDismiss(dialogFragment: BaseDialogFragment, dialog: DialogInterface)
}
