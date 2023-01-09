package me.kqn.gptbot

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBuilder
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.info
import taboolib.module.kether.KetherShell
import taboolib.module.kether.printKetherErrorMessage
import java.util.concurrent.CompletableFuture

fun debug(msg:String){
   if(ConfigObject.debug.booleanValue()) Bukkit.getLogger().info(msg)
}
fun List<String>.eval(player: Player):CompletableFuture<*>?{
    try {
       return KetherShell.eval(this, sender = adaptPlayer(player), cacheScript = true)
    } catch (e: Throwable) {
        e.printKetherErrorMessage()
    }
    return null
}
fun String.eval(player: Player):CompletableFuture<*>?{
    try {
        return KetherShell.eval(this, sender = adaptPlayer(player), cacheScript = true)
    }catch (e:Exception){
        e.printStackTrace()
    }
    return null
}
fun Array<String>.cut(length_limit:Int):Array<String>{
    var sb=StringBuilder()
    var len=0
    this.forEach {
        sb.append(it)
        len+=it.length
    }
    debug("len:$len  limit:$length_limit")
    if(len<length_limit){
        return this
    }
    var tmp=sb.substring(0,length_limit)
    var newArr=ArrayList<String>()
    while (tmp.length>20){
        newArr.add(tmp.substring(0,20))
        tmp=tmp.removeRange(0,20)
    }
    newArr.add(tmp)
    var res=Array<String>(newArr.size){it->newArr.get(it)}
   return res
}


fun CommandBuilder.CommandComponent.createHelper() {
    execute<ProxyCommandSender> { sender, context, _ ->
        val command = context.command
        val builder = StringBuilder("§a使用方法: /${command.name}")
        var newline = false
        fun space(space: Int): String {
            return (1..space).joinToString("") { " " }
        }
        fun print(compound: CommandBuilder.CommandComponent, index: Int, size: Int, offset: Int = 8, level: Int = 0, end: Boolean = false) {
            var comment = 0
            when (compound) {
                is CommandBuilder.CommandComponentLiteral -> {
                    if (size == 1) {
                        builder.append(" ").append("§a${compound.aliases[0]}")
                    } else {
                        newline = true
                        builder.appendLine()
                        builder.append(space(offset))
                        if (level > 1) {
                            builder.append(if (end) " " else "§7│")
                        }
                        builder.append(space(level))
                        if (index + 1 < size) {
                            builder.append("§7├── ")
                        } else {
                            builder.append("§7└── ")
                        }
                        builder.append("§a${compound.aliases[0]}")
                    }
                    comment = compound.aliases[0].length
                }
                is CommandBuilder.CommandComponentDynamic -> {
                    val value = if (compound.comment.startsWith("@")) {
                        compound.comment
                    } else {
                        compound.comment
                    }
                    comment = if (compound.optional) {
                        builder.append(" ").append("§7<$value§a?§7>")
                        compound.comment.length + 3
                    } else {
                        builder.append(" ").append("§7<$value>")
                        compound.comment.length + 2
                    }
                }
            }
            if (level > 0) {
                comment += 1
            }
            compound.children.forEachIndexed { i, children ->
                // 因 literal 产生新的行
                if (newline) {
                    print(children, i, compound.children.size, offset, level + comment, end = end)
                } else {
                    val length = if (offset == 8) command.name.length + 1 else comment + 1
                    print(children, i, compound.children.size, offset + length, level, end = end)
                }
            }
        }
        val size = context.commandCompound.children.size
        context.commandCompound.children.forEachIndexed { index, children ->
            print(children, index, size, end = index + 1 == size)
        }
        builder.lines().forEach {
            sender.sendMessage(it)
        }
    }
}