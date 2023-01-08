package me.kqn.gptbot

import me.kqn.gptbot.Bot.IChatBot
import org.bukkit.entity.Player

object BotManager {
    val playerBots=HashMap<Player, IChatBot>()

}