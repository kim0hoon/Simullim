package com.simullim.debugtest

import com.simullim.common.PaceSetting
import com.simullim.common.PlaylistModel
import com.simullim.service.model.PlayServiceInputModel

internal val testPlayServiceInputModel = PlayServiceInputModel(
    playlistModel = PlaylistModel(),
    paceSetting = PaceSetting(PaceSetting.Type.Default).withPaceList(buildList {
        var acc = 0
        repeat((5..10).random()) {
            val length = (10..30).random() * 500
            add(
                PaceSetting.Pace(
                    start = acc,
                    length = length,
                    velocitySecPerKiloMeter = (10..14).random() * 30
                )
            )
            acc += length
        }
    })
)