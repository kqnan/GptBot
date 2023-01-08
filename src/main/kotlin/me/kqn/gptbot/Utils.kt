package me.kqn.gptbot

import org.bukkit.Bukkit
import taboolib.common.platform.function.info

fun debug(msg:String){
    Bukkit.getLogger().info(msg)
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