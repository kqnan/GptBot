package me.kqn.gptbot.Holo


import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI
import me.filoghost.holographicdisplays.api.hologram.Hologram
import me.filoghost.holographicdisplays.api.hologram.VisibilitySettings
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import taboolib.module.chat.colored
import java.util.UUID
/** ¼æÈÝHoloGraphicDisplay²å¼þ
 * */
class HoloGraphicDisplay :HoloDisplay {
    val holo:HolographicDisplaysAPI
    private val data=HashMap<UUID,Hologram>()
    constructor(plugin: Plugin){
        holo= HolographicDisplaysAPI.get(plugin)
    }

    override fun setPosition(player: Player, location:Location) {
        val holos= data[player.uniqueId] ?: return
        holos.setPosition(location.clone())
    }
    override fun showToPlayer(player: Player,message: Array<String>, offset: Location) {
        var holos=data.getOrDefault(player.uniqueId,holo.createHologram(player.location.clone()))
        holos.lines.clear()
        for (s in message) {
            holos.lines.appendText(s.colored())
        }
        holos.setPosition(player.eyeLocation.clone().add(offset))

        holos.visibilitySettings.setIndividualVisibility(player,VisibilitySettings.Visibility.VISIBLE)
        data[player.uniqueId] = holos
    }

    override fun removeHolo(player: Player) {
        data.get(player.uniqueId)?.delete()
        data.remove(player.uniqueId)
    }

    override fun onDisable() {
        data.forEach { t, u ->u.delete()  }
    }

}