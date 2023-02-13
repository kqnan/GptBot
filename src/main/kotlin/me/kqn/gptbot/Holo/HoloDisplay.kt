package me.kqn.gptbot.Holo

import org.bukkit.Location
import org.bukkit.entity.Player

interface HoloDisplay {
    fun showToPlayer(player: Player,message:Array<String>,offset:Location)
    fun removeHolo(player:Player)
    fun onDisable()
}