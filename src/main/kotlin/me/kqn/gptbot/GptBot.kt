package me.kqn.gptbot

import me.kqn.gptbot.Displayer.CommonChatDisplayer
import me.kqn.gptbot.Displayer.FancyChatDisplayer
import org.bukkit.command.CommandSender
import taboolib.common.platform.Plugin
import taboolib.common.platform.command.command
import taboolib.common.platform.function.submit
import taboolib.common.util.asList
import taboolib.module.chat.colored

object GptBot : Plugin() {
    lateinit var gpt:ChatGPT
    override fun onEnable() {
        gpt=ChatGPT("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",ChatGPT.Model.TEXT_DAVINCI_003,1024,"&a机器人迷糊了".colored())
        registercommand()
    }

    fun registercommand(){
        command(name="GptBot", aliases = listOf("gb,bot,chatbot,cb")){
            literal("debug"){
                dynamic {
                    suggestion<CommandSender>(uncheck = false){ sender, context ->
                        ChatGPT.Model.values().asList()
                    }
                    dynamic {
                        suggestion<CommandSender>(uncheck = true){
                            sender, context ->
                            listOf("请输入你想和ai机器人说的话".colored())
                        }
                        execute<CommandSender>(){sender, context, argument ->

                            sender.sendMessage("&a请等待ai机器人的应答...".colored())
                            submit (async = true){
                                var response=gpt.input(context.argumentOrNull(0)!!)
                                var d=FancyChatDisplayer("&aChatGpt&8>>",true,"&c&l@%player_name%","&f%text%",true,true)
                                d.display(response,sender)
                            }


                        }
                    }
                }
            }
//ctrl+q查看文档  f2定位报错行 shift+f2定位上一个报错行  shift+q光标移动到行尾 shift+w光标移动到行首

        }
    }
}