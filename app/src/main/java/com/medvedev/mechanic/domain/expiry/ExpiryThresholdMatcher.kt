package com.medvedev.mechanic.domain.expiry

import com.medvedev.mechanic.domain.model.ExpiryThreshold

object ExpiryThresholdMatcher {

    fun matching(
        daysUntil: Long,
        enabled: Set<ExpiryThreshold>,
    ): ExpiryThreshold? {
        val candidate = when {
            daysUntil <= 0L -> ExpiryThreshold.EXPIRED
            daysUntil == 1L -> ExpiryThreshold.DAYS_1
            daysUntil == 7L -> ExpiryThreshold.DAYS_7
            daysUntil == 14L -> ExpiryThreshold.DAYS_14
            daysUntil == 30L -> ExpiryThreshold.DAYS_30
            else -> null
        }
        return candidate.takeIf { it in enabled }
    }
}
