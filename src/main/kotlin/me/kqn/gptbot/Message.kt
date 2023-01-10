package me.kqn.gptbot

import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import java.io.File

object Message {
    @Config(value="message.yml", autoReload = true)
    lateinit var message:Configuration
    @ConfigNode(value="WRONG_NUMBER",bind = "message.yml")
    lateinit var WRONG_NUMBER:String
    @ConfigNode(value = "COOLING", bind = "message.yml")
    lateinit var COOLING:String
    fun save(){
        ConfigObject.config.saveToFile(File("plugins/GptBot/config.yml"))
    }
}