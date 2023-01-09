package me.kqn.gptbot.Displayer

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.submitAsync
import taboolib.common5.util.createBar
import taboolib.common5.util.printed
import taboolib.module.chat.colored
import taboolib.platform.util.sendActionBar
import java.util.function.BiConsumer
import java.util.function.BinaryOperator
import java.util.function.Consumer

class SingletonDisplayer:IDisplayer  {
    var onDisplay:BiConsumer<Player,String>
    constructor(onDisplay:BiConsumer<Player,String>){

        this.onDisplay=onDisplay
    }
    override fun display(text: Array<String>, player: CommandSender) {
        if(!(player is Player))return
        var p=player as Player
        submitAsync {
            var strb=StringBuilder()
            var s:String
            var txt=ArrayList<String>()
            text.forEach {
                var tmp=it
                while (tmp.length>20){
                    txt.add(tmp.substring(0,19))
                    tmp=tmp.removeRange(0,19)
                }
                txt.add(tmp)
            }

            txt.forEach {
                var split=it.printed("")
                split.forEach {it->
                    if(p.isOnline==false)return@submitAsync
                    onDisplay.accept(p,it.colored())
                    Thread.sleep(1000*5/20)//ясЁы5tick
                }
            }
        }
    }

}