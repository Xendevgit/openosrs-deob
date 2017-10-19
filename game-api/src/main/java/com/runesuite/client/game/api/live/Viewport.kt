package com.runesuite.client.game.api.live

import com.runesuite.client.game.raw.Client.accessor
import java.awt.Rectangle

interface Viewport {

    val scale: Int

    val x: Int

    val y: Int

    val width: Int

    val height: Int

    val shape get() = Rectangle(x, y, width, height)

    object Live : Viewport {

        override val x: Int get() = accessor.viewportX

        override val y: Int get() = accessor.viewportY

        override val width: Int get() = accessor.viewportWidth

        override val height: Int get() = accessor.viewportHeight

        override val scale: Int get() = accessor.viewportScale

        override fun toString(): String {
            return "Viewport.Live(scale=$scale, shape=$shape)"
        }
    }

    data class Fixed(override val scale: Int) : Viewport {

        override val x = 4
        override val y = 4
        override val width = 512
        override val height = 334

        companion object {
            const val SCALE_DEFAULT = 512
            const val SCALE_MIN = 390
            const val SCALE_MAX = 1400

            @JvmField
            val DEFAULT = Fixed(SCALE_DEFAULT)
        }
    }

    fun copyOf(): Copy = Copy(scale, x, y, width, height)

    data class Copy(
            override val scale: Int,
            override val x: Int,
            override val y: Int,
            override val width: Int,
            override val height: Int
    ) : Viewport
}