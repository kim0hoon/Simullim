package com.simullim.start.model

import androidx.compose.runtime.Immutable
import com.simullim.start.model.PaceSetting.Type


internal abstract class PaceSetting {
    abstract val paceList: List<Pace>
    abstract val type: Type
    abstract val totalDistanceMeter: Int
    abstract val totalTimeSec: Int
    abstract fun withPaceList(paceList: List<Pace>): PaceSetting

    enum class Type {
        Distance,
        Time;

        companion object {
            val Default = Distance
        }
    }

    data class Pace(val start: Int = 0, val length: Int = 0, val velocitySecPerKiloMeter: Int = 0)
}

@Immutable
private data class DistanceType(
    override val paceList: List<Pace> = emptyList()
) : PaceSetting() {
    override val type: Type = Type.Distance
    override val totalDistanceMeter: Int = paceList.sumOf { it.length }
    override val totalTimeSec: Int =
        paceList.sumOf { it.run { length * velocitySecPerKiloMeter } / 1000 }

    override fun withPaceList(paceList: List<Pace>) = this.copy(paceList = paceList)

}

@Immutable
private data class TimeType(
    override val paceList: List<Pace> = emptyList()
) : PaceSetting() {
    override val type: Type = Type.Time
    override val totalDistanceMeter: Int =
        paceList.sumOf { it.run { if (velocitySecPerKiloMeter == 0) 0 else length * 1000 / velocitySecPerKiloMeter } }
    override val totalTimeSec: Int = paceList.sumOf { it.length }
    override fun withPaceList(paceList: List<Pace>) = this.copy(paceList = paceList)
}

internal fun PaceSetting(type: Type) = when (type) {
    Type.Distance -> DistanceType()
    Type.Time -> TimeType()
}