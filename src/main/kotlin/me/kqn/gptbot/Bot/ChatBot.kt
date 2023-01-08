package me.kqn.gptbot.Bot

abstract class IChatBot {
    abstract fun input(vararg text:String):Array<String>;

}