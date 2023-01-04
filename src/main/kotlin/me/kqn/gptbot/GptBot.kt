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
        gpt=ChatGPT("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",ChatGPT.Model.TEXT_DAVINCI_003,1024,"&a�������Ժ���".colored())
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
                            listOf("�����������ai������˵�Ļ�".colored())
                        }
                        execute<CommandSender>(){sender, context, argument ->

                            sender.sendMessage("&a��ȴ�ai�����˵�Ӧ��...".colored())
                            submit (async = true){
                                var response=gpt.input(context.argumentOrNull(0)!!)
                                var d=FancyChatDisplayer("&aChatGpt&8>>",true,"&c&l@%player_name%","&f%text%",true,true)
                                d.display(response,sender)
                            }


                        }
                    }
                }
            }
//ctrl+q�鿴�ĵ�  f2��λ������ shift+f2��λ��һ��������  shift+q����ƶ�����β shift+w����ƶ�������

        }
    }
}