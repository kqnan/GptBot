package me.kqn.gptbot

abstract class IChatBot {
    abstract fun input(vararg text:String):Array<String>;

}