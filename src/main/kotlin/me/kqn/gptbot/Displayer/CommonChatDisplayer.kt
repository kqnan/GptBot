package me.kqn.gptbot.Displayer

import org.bukkit.command.CommandSender
import taboolib.module.chat.colored

open class CommonChatDisplayer : IDisplayer() {
    override fun display(text: Array<String>, player: CommandSender) {
           text.forEach {
               player.sendMessage(it.colored())
    }
}
}