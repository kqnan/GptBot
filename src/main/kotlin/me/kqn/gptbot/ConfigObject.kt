package me.kqn.gptbot

import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import java.io.File

object ConfigObject {
    @taboolib.module.configuration.Config(value = "config.yml",autoReload = true)
    lateinit var config:Configuration
    @ConfigNode(value = "autoChat.enable","config.yml")
    lateinit var autoChat_enable:java.lang.Boolean
    @ConfigNode(value = "autoChat.interval","config.yml")
    lateinit var autoChat_interval:Integer

    @ConfigNode(value = "ChatGpt.api_key")
    lateinit var api_key:String
    @ConfigNode(value = "ChatGpt.Model")
    lateinit var model:String

    @ConfigNode(value = "ChatGpt.token_length")
    lateinit var token_len:Integer
    @ConfigNode(value="ChatGpt.default_response")
    lateinit var defualt_answer:String
    fun save(){
        config.saveToFile(File("plugins/GptBot/config.yml"))
    }

}