package me.kqn.gptbot.Displayer

import org.bukkit.command.CommandSender

abstract class IDisplayer {
   public abstract fun display(text:Array<String> ,player:CommandSender );
}