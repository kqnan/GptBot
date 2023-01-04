package me.kqn.gptbot.Displayer

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
import taboolib.module.chat.HexColor
import taboolib.module.chat.colored
import taboolib.module.chat.uncolored

class FancyChatDisplayer  :IDisplayer{
    var prefix:String
    var atPlayer:Boolean
    var unitLines:Boolean
    var atPattern:String
    var linePattern:String
    var echo:Boolean
    //atPattern: &2&l@%player_name%
    //linePatter: &a&l%text%
    constructor(prefix:String,atPlayer:Boolean,atPattern:String,linePattern:String,unitLines:Boolean,echo:Boolean):super(){
        this.echo=echo
        this.prefix=prefix
        this.atPattern=atPattern
        this.atPlayer=atPlayer
        this.unitLines=unitLines
        this.linePattern=linePattern;
    }

    override fun display(text: Array<String>, player: CommandSender) {
        if(player is Player&&echo){
            submit { text.forEach {
                (player as Player).chat(it.colored())
            } }
        }

        var txt=text
        if(unitLines){
            var builder=StringBuilder()
            text.forEach { builder.append(it) }
            txt= arrayOf(builder.toString())
        }
        if(atPlayer){
            txt.forEach {
                player.sendMessage((prefix+atPattern.replace("%player_name%",player.name)+linePattern.replace("%text%",it)).colored())
            }
        }
        else {
            txt.forEach {
                player.sendMessage((prefix+linePattern.replace("%text%",it)).colored())
            }
        }

    }
}