package com.runesuite.client.game.api

import com.hunterwb.kxtra.swing.polygon.Polygon
import com.runesuite.client.game.api.live.Projection
import com.runesuite.client.game.api.live.Scene
import java.awt.Polygon

/**
 * Tile relative to a Scene
 *
 * @see[Scene]
 */
data class SceneTile(val x: Int, val y: Int, val plane: Int) {

    val center get() = Position(x, Position.MID_SUB, y, Position.MID_SUB, 0, plane)

    val base get() = Position(x, 0, y, 0, 0, plane)

    val isLoaded get() = x in 0 until Scene.SIZE && y in 0 until Scene.SIZE && plane in 0 until Scene.PLANE_SIZE

    fun toGlobalTile(scene: Scene = Scene.Live): GlobalTile {
        return GlobalTile(x + scene.base.x, y + scene.base.y, plane)
    }

    fun height(scene: Scene = Scene.Live): Int? {
        return scene.getHeight(this)
    }

    operator fun plus(sceneTile: SceneTile) : SceneTile {
        return SceneTile(x + sceneTile.x, y + sceneTile.y, plane + sceneTile.plane)
    }

    val corners: List<Position>
        get() = base.run { listOf(
                this,
                copy(subX = Position.MAX_SUB),
                copy(subX = Position.MAX_SUB, subY = Position.MAX_SUB),
                copy(subY = Position.MAX_SUB)) }

    fun outline(projection: Projection = Projection.Viewport.Live): Polygon {
        check(isLoaded) { this }
        return Polygon(corners.map { it.toScreen(projection) })
    }
}