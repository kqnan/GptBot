package me.kqn.gptbot

import me.kqn.gptbot.Bot.ChatGPT
import me.kqn.gptbot.Bot.IChatBot
import me.kqn.gptbot.Displayer.FancyChatDisplayer
import org.bukkit.event.player.AsyncPlayerChatEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.onlinePlayers
import taboolib.common.platform.function.submitAsync
import taboolib.common5.Baffle
import taboolib.module.chat.colored
import taboolib.platform.type.BukkitPlayer
import java.lang.Boolean
import java.util.concurrent.TimeUnit

object ChatEvent {
    var baffle=Baffle.of(ConfigObject.autoChat_interval.toInt())
    var baffle10s=Baffle.of(ConfigObject.cool.toLong(),TimeUnit.SECONDS)
    @SubscribeEvent
    fun autoChat(e:AsyncPlayerChatEvent){
        //主动呼叫
        if(ConfigObject.enable.booleanValue()){
            var prefix=ConfigObject.prefix.replace("%name%",ConfigObject.name)
            var chagemsg=e.message
            if(chagemsg.startsWith(prefix)){//判断前缀
                if(!baffle10s.hasNext(e.player.name)){
                    e.player.sendMessage(Message.COOLING.colored())
                    return
                }
                baffle10s.next(e.player.name)
                submitAsync {//异步执行动作，防止卡主线程
                    var condition=ConfigObject.condition
                    var res=condition.eval(e.player)//执行条件动作的判断
                    if(res!=null){
                        var b=res.get() as kotlin.Boolean
                        if(b){
                            var action=ConfigObject.action
                            action.eval(e.player)

                            var bot= ChatGPT.instance(ConfigObject.api_key, ChatGPT.Model.valueOf(ConfigObject.model),ConfigObject.token_len.toInt(),ConfigObject.defualt_answer)
                            GptBot.displayer.display(bot.input(chagemsg.removePrefix(prefix)),e.player)
            //                FancyChatDisplayer.getPreset().display(bot.input(chagemsg.removePrefix(prefix)),e.player)
                        }
                        else {
                            ConfigObject.deny.eval(e.player)
                        }
                    }
                }
                return

            }

        }
        //自动聊天
        if(ConfigObject.autoChat_enable== Boolean(false))return
        if(baffle.hasNext()){
            baffle.next()
            var bot: IChatBot?=null
            if(BotManager.playerBots.containsKey(e.player)){
                bot=BotManager.playerBots.get(e.player)
            }
            else {
                bot= ChatGPT.instance(ConfigObject.api_key, ChatGPT.Model.valueOf(ConfigObject.model),ConfigObject.token_len.toInt(),ConfigObject.defualt_answer)
            }
            submitAsync {
                var rsp=bot?.input(e.message)
                rsp?.let {
                    for (onlinePlayer in onlinePlayers()) {
                        GptBot.displayer.display(it,(onlinePlayer as BukkitPlayer).player)
                    //FancyChatDisplayer.getPreset().display(it,(onlinePlayer as BukkitPlayer).player)
                    }
                }
            }
        }
    }
    @Awake(LifeCycle.DISABLE)
    fun reset(){
        baffle.resetAll()
        baffle10s.resetAll()
    }
}