package com.naqi.nomnom.ui.reminder

import androidx.annotation.DrawableRes
import com.naqi.nomnom.R

enum class CharacterState(@DrawableRes val drawableRes: Int) {
    NORMAL(R.drawable.nomnom_character),   // drawable yang sudah ada
    HAPPY(R.drawable.nomnom_happy),    // ganti ke nomnom_happy saat aset tersedia
    SAD(R.drawable.nomnom_sad)       // ganti ke nomnom_sad saat aset tersedia
}