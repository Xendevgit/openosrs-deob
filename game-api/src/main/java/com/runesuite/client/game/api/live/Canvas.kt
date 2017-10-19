package com.runesuite.client.game.api.live

import com.hunterwb.kxtra.swing.graphics2d.create2D
import com.hunterwb.kxtra.swing.toolkit.fontDesktopHints
import com.runesuite.client.game.raw.Client.accessor
import com.runesuite.client.game.raw.access.XGameShell
import com.runesuite.client.game.raw.access.XRasterProvider
import hu.akarnokd.rxjava2.swing.SwingObservable
import io.reactivex.Observable
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.Toolkit
import java.awt.event.ComponentEvent
import java.awt.event.FocusEvent

interface Canvas {

    val shape: Rectangle

    object Live : Canvas {

        init {
            accessor.gameDrawingMode = 2
        }

        private val desktopHints = Toolkit.getDefaultToolkit().fontDesktopHints()

        override val shape get() = Rectangle(accessor.canvas.size)

        val repaints: Observable<Graphics2D> = XRasterProvider.drawFull0.enter.map {
            (it.instance.image.graphics as Graphics2D).apply {
                desktopHints?.let { this.addRenderingHints(it) }
            }
        }.publish().refCount().map { it.create2D() }

        /**
         * @see[java.awt.event.FocusListener]
         */
        val focusEvents: Observable<FocusEvent> = XGameShell.replaceCanvas.exit.map { accessor.canvas }.startWith(accessor.canvas)
                .flatMap { SwingObservable.focus(it) }

        /**
         * @see[java.awt.event.ComponentListener]
         */
        val componentEvents: Observable<ComponentEvent> = XGameShell.replaceCanvas.exit.map { accessor.canvas }.startWith(accessor.canvas)
                .flatMap { SwingObservable.component(it) }

        override fun toString(): String {
            return "Canvas.Live(shape=$shape)"
        }
    }

    object Fixed : Canvas {
        override val shape = Rectangle(0, 0, 765, 503)
    }

    fun copyOf(): Copy = Copy(shape)

    data class Copy(override val shape: Rectangle): Canvas
}