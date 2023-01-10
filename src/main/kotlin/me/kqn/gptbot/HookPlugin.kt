package me.kqn.gptbot

import org.black_ixx.playerpoints.PlayerPoints
import org.black_ixx.playerpoints.PlayerPointsAPI
import org.bukkit.Bukkit

object HookPlugin {
    val  playerPointsAPI:PlayerPointsAPI
    init {
        playerPointsAPI= PlayerPointsAPI(Bukkit.getPluginManager().getPlugin("PlayerPoints") as PlayerPoints?)
    }

}