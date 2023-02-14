package me.kqn.gptbot.Displayer

import me.kqn.gptbot.HookPlugin
import me.kqn.gptbot.debug
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
import taboolib.common.util.sync

class HoloDisplayer:IDisplayer() {
    override fun display(text: Array<String>, player: CommandSender) {
        HookPlugin.holoDisplay?:return
        if(player !is Player)return
        sync{HookPlugin.holoDisplay!!.showToPlayer(player,text,player.facing.direction.toLocation(player.world).multiply(3.0))}
        var tickrun=0
        val uuid=player.uniqueId
        submit(period = 1) {
            val p=Bukkit.getPlayer(uuid)
            if((tickrun >= 200) || (p == null) || !p.isOnline){
                this.cancel()
                p?.let {HookPlugin.holoDisplay!!.removeHolo(it)}

                return@submit
            }
                val facing=p.facing.direction.toLocation(p.world).multiply(3.0)

               HookPlugin.holoDisplay!!.setPosition(p,p.eyeLocation.add(facing.x,0.0,facing.y))
            tickrun++
        }
    }

}