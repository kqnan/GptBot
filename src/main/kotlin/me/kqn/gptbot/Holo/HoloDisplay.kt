package me.kqn.gptbot.Holo

import org.bukkit.Location
import org.bukkit.entity.Player

interface HoloDisplay {
    /**展示给玩家看，以玩家位置+offset生成holo
     * */
    fun showToPlayer(player: Player,message:Array<String>,offset:Location)
    /**移除玩家的holo
     * */
    fun removeHolo(player:Player)
    /**为玩家的holo设置位置
     * */
    fun setPosition(player: Player,location: Location)
    /**
     * 插件关闭时调用
     * */
    fun onDisable()
}