package com.medvedev.mechanic.data.resources

import androidx.annotation.StringRes

interface StringProvider {
    fun getString(@StringRes resId: Int): String
}
