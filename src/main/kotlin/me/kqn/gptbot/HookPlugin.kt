package me.kqn.gptbot

import me.kqn.gptbot.Holo.HoloDisplay
import me.kqn.gptbot.Holo.HoloGraphicDisplay
import org.black_ixx.playerpoints.PlayerPoints
import org.black_ixx.playerpoints.PlayerPointsAPI
import org.bukkit.Bukkit

object HookPlugin {
    val  playerPointsAPI:PlayerPointsAPI = PlayerPointsAPI(Bukkit.getPluginManager().getPlugin("PlayerPoints") as PlayerPoints?)
    var holoDisplay:HoloDisplay?=null
    init {
        holoDisplay= Bukkit.getPluginManager().getPlugin("HolographicDisplays")?.let { HoloGraphicDisplay(it) }
    }
}