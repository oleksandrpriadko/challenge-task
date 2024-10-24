package com.oleksandrpriadko.shared.extension

import androidx.lifecycle.ViewModel
import com.oleksandrpriadko.kermit

fun ViewModel.logD(message: String?) {
    message?.let {
        kermit.withTag(this.javaClass.simpleName).d { message }
    }
}

fun ViewModel.logE(message: String?) {
    message?.let {
        kermit.withTag(this.javaClass.simpleName).e { message }
    }
}