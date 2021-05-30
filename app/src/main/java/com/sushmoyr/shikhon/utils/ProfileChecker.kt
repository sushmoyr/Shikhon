package com.sushmoyr.shikhon.utils

import android.text.TextUtils
import com.sushmoyr.shikhon.backend.data.User

object rofileChecker {
    private const val requiredPercent: Double = 0.8
    fun isProfileComplete(user: User): Boolean {
        val completePercent: Double = getProfileCompletePercent(user)

        if (completePercent < requiredPercent)
            return false
        return true
    }

    private fun getProfileCompletePercent(user: User): Double {
        var infoCount = 0
        val totalInfo = 5.0
        if (!TextUtils.isEmpty(user.uuid))
            infoCount++

        if (!TextUtils.isEmpty(user.name))
            infoCount++

        if (!TextUtils.isEmpty(user.email))
            infoCount++

        if (user.accountType != Constants.USER_TYPE_NONE)
            infoCount++

        if (!TextUtils.isEmpty(user.profilePicUri))
            infoCount++

        val result = (infoCount / totalInfo)

        return result
    }
}