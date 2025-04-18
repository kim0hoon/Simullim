package com.simullim.playinfo.model

/**
 * @param timeSec : 시간(초)
 * @param totalDistanceMeter : 총 이동거리(미터)
 * @param averageVelocity : 평균속도(시간/km)
 * @param velocity : 현재속도(시간/km)
 */
internal data class PlayInfoModel(
    val timeSec: Int = 0,
    val totalDistanceMeter: Int = 0,
    val averageVelocity: Int = 0,
    val velocity: Int = 0
)
