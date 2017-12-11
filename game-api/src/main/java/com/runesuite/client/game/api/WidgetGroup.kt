package com.runesuite.client.game.api

import com.runesuite.client.game.api.live.Widgets
import com.runesuite.client.game.raw.Client
import com.runesuite.client.game.raw.access.XWidgetNode

class WidgetGroup
internal constructor(val id: Int) {

    val parent: Widget.Parent? get() {
        val table = Client.accessor.widgetNodes
        var node = table.first() as XWidgetNode?
        while (node != null) {
            if (node.id == id) {
                return Widgets[WidgetId(node.key.toInt())]
            }
            node = table.next() as XWidgetNode?
        }
        return null
    }

    val roots: List<Widget.Parent> get() = all.filter { it.predecessor == null }

    val size get() = Client.accessor.widgets[id]?.size

    val flat: List<Widget> get() = all.flatMap { it.flat }

    val all: List<Widget.Parent> get() = Client.accessor.widgets[id]?.map { Widget.Parent(it) } ?: emptyList()

    operator fun get(id: Int): Widget.Parent? =
            Client.accessor.widgets.getOrNull(this.id)?.getOrNull(id)?.let { Widget.Parent(it) }
}