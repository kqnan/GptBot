package me.kqn.gptbot.Displayer

import me.kqn.gptbot.ConfigObject
import me.kqn.gptbot.cut
import me.kqn.gptbot.debug
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submit
import taboolib.common.util.sync
import taboolib.expansion.getDataContainer
import taboolib.module.chat.HexColor
import taboolib.module.chat.colored
import taboolib.module.chat.uncolored
import taboolib.platform.compat.PlaceholderExpansion
import taboolib.platform.compat.replacePlaceholder

class FancyChatDisplayer  :IDisplayer{

    var prefix:String
    var atPlayer:Boolean
    var unitLines:Boolean
    var atPattern:String
    var linePattern:String
companion object
{
    fun getPreset():FancyChatDisplayer{
       return FancyChatDisplayer("&aChatGpt&8>>",true,"&c&l@%player_name%","&f%text%",true)
    }
}
    //atPattern: &2&l@%player_name%
    //linePatter: &a&l%text%
    constructor(prefix:String,atPlayer:Boolean,atPattern:String,linePattern:String,unitLines:Boolean):super(){

        this.prefix=prefix
        this.atPattern=atPattern
        this.atPlayer=atPlayer
        this.unitLines=unitLines
        this.linePattern=linePattern;
    }

    override fun display(text: Array<String>, player: CommandSender) {


        var txt=text
        var display_len=ConfigObject.token_len.toInt()
        if(player is Player){
            display_len=(player as Player).getDataContainer()["display_length"]?.toInt()?:0
        }
        debug(display_len.toString())
        txt=txt.cut(display_len)
        if(unitLines){
            var builder=StringBuilder()
            txt.forEach { builder.append(it) }
            txt= arrayOf(builder.toString())
        }


        var format=ConfigObject.format

            txt.forEach {
                var msg=format.replace("%name%",ConfigObject.name.colored()).replace("%text%",it.colored())
                msg= sync { msg.replacePlaceholder(player as Player) }

                //player.sendMessage((prefix+atPattern.replace("%player_name%",player.name)+linePattern.replace("%text%",it)).colored())
                player.sendMessage(msg.colored())
            }


    }
}