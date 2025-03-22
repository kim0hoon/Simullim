package com.simullim.start.model

import androidx.compose.runtime.Immutable

internal data class PaceSettingModel(
    val selectedType: PaceSetting.Type = PaceSetting.Type.Default,
    val distanceModel: PaceSetting = PaceSetting.DistanceType(),
    val timeModel: PaceSetting = PaceSetting.TimeType()
) {
    val currentModel = when (selectedType) {
        PaceSetting.Type.Time -> timeModel
        PaceSetting.Type.Distance -> distanceModel
    }
}

@Immutable
internal abstract class PaceSetting {
    abstract val type: Type
    open val value: Int = 0
    open val pace: List<Pace> = emptyList()
    abstract val totalDistanceMeter: Int
    abstract val totalTimeSec: Int


    enum class Type {
        Distance,
        Time;

        companion object {
            val Default = Distance
        }
    }

    internal data class Pace(val range: IntRange, val value: Int)

    internal data class DistanceType(
        override val value: Int = 0,
        override val pace: List<Pace> = emptyList()
    ) : PaceSetting() {
        override val type: Type = Type.Distance
        override val totalDistanceMeter: Int = value
        override val totalTimeSec: Int = 0//TODO 계산
    }

    internal data class TimeType(
        override val value: Int = 0,
        override val pace: List<Pace> = emptyList()
    ) : PaceSetting() {
        override val type: Type = Type.Time
        override val totalDistanceMeter: Int = 0//TODO 계산
        override val totalTimeSec: Int = value
    }
}