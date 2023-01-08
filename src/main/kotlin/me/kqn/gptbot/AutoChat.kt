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
import taboolib.platform.type.BukkitPlayer
import java.lang.Boolean

object AutoChat {
    val baffle=Baffle.of(ConfigObject.autoChat_interval.toInt())
    @SubscribeEvent
    fun autoChat(e:AsyncPlayerChatEvent){
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
                        FancyChatDisplayer.getPreset().display(it,(onlinePlayer as BukkitPlayer).player)
                    }
                }
            }

        }
    }
    @Awake(LifeCycle.DISABLE)
    fun reset(){
        baffle.resetAll()
    }
}