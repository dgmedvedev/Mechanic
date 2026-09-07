package com.medvedev.mechanic.data.resources

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidStringProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) : StringProvider {

    override fun getString(resId: Int): String = context.getString(resId)
}
