package com.simullim.start.model

import androidx.compose.runtime.Immutable

internal data class PaceSettingModel(
    val selectedType: PaceSetting.Type = PaceSetting.Type.Default,
    val paceSettingMap: Map<PaceSetting.Type, PaceSetting> = mapOf(
        PaceSetting.Type.Distance to PaceSetting.DistanceType(),
        PaceSetting.Type.Time to PaceSetting.TimeType()
    )
) {
    val currentModel = paceSettingMap[selectedType] ?: run {
        when (selectedType) {
            PaceSetting.Type.Time -> PaceSetting.TimeType()
            PaceSetting.Type.Distance -> PaceSetting.DistanceType()
        }
    }

    fun withCurrentPaceSetting(currentPaceSetting: PaceSetting): PaceSettingModel =
        this.copy(paceSettingMap = paceSettingMap.toMutableMap().apply {
            replace(selectedType, currentPaceSetting)
        })

}

@Immutable
internal abstract class PaceSetting {
    abstract val length: Int
    abstract val paceList: List<Pace>
    abstract val type: Type
    abstract val totalDistanceMeter: Int
    abstract val totalTimeSec: Int

    protected abstract fun with(length: Int, paceList: List<Pace>): PaceSetting

    fun withLength(length: Int): PaceSetting = with(length = length, paceList = paceList)
    fun withAddPace(pace: Pace): PaceSetting =
        with(length = length, paceList = paceList + pace)

    fun withRemovePace(pace: Pace): PaceSetting = with(length = length, paceList = paceList - pace)
    fun withClearPace(): PaceSetting = with(length = length, paceList = emptyList())

    enum class Type {
        Distance,
        Time;

        companion object {
            val Default = Distance
        }
    }

    data class Pace(val range: IntRange, val value: Int)

    data class DistanceType(
        override val length: Int = 0,
        override val paceList: List<Pace> = emptyList()
    ) : PaceSetting() {
        override val type: Type = Type.Distance
        override val totalDistanceMeter: Int = length
        override val totalTimeSec: Int = 0//TODO 계산
        override fun with(length: Int, paceList: List<Pace>): PaceSetting =
            copy(length = length, paceList = paceList)
    }

    data class TimeType(
        override val length: Int = 0,
        override val paceList: List<Pace> = emptyList()
    ) : PaceSetting() {
        override val type: Type = Type.Time
        override val totalDistanceMeter: Int = 0//TODO 계산
        override val totalTimeSec: Int = length
        override fun with(length: Int, paceList: List<Pace>): PaceSetting =
            copy(length = length, paceList = paceList)
    }
}