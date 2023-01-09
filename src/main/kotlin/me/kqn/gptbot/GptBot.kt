package me.kqn.gptbot

import me.kqn.gptbot.Bot.ChatGPT
import me.kqn.gptbot.Displayer.SingletonDisplayer
import net.minecraft.network.protocol.game.PacketPlayOutEntity
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import taboolib.common.io.newFile
import taboolib.common.platform.Plugin
import taboolib.common.platform.command.command
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.submit
import taboolib.common.util.asList
import taboolib.expansion.*
import taboolib.module.chat.colored
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.actionBar
import taboolib.platform.util.onlinePlayers

object GptBot : Plugin() {

    lateinit var gpt: ChatGPT
    var echo=true
    lateinit var plugin:JavaPlugin

    // TODO: 2023/1/9  1.主动@机器人 ，需要金币花费或者权限，或者执行动作  2.游戏中更换apikey 3.游戏中查看apikey的余额  4.自动重载的耦合度太高
    override fun onEnable() {
        var conf=ConfigObject.config
        if (conf.getBoolean("database.enable")) {
            setupPlayerDatabase(conf.getConfigurationSection("database")!!)
        } else {
            setupPlayerDatabase(newFile(getDataFolder(), "data.db"))
        }
        gpt= ChatGPT.instance(ConfigObject.api_key,
            ChatGPT.Model.valueOf(ConfigObject.model),ConfigObject.token_len.toInt(),ConfigObject.defualt_answer.colored())
        registercommand()
        plugin= BukkitPlugin.getInstance()


    }
    override fun onDisable(){
        for (onlinePlayer in onlinePlayers) {
            onlinePlayer.releaseDataContainer()
        }
    }
    @SubscribeEvent
    fun setupData(e: PlayerJoinEvent) {
        // 初始化玩家容器

        e.player.setupDataContainer()

    }

    @SubscribeEvent
    fun releaseDAta(e: PlayerQuitEvent) {
        // 释放玩家容器缓存
        e.player.releaseDataContainer()
    }
    fun registercommand(){
        command(name="GptBot", aliases = listOf("gb,bot,chatbot,cb")){
            createHelper()
            literal("set-length"){
                dynamic (comment = "设置机器人输出的文本的最大显示长度"){
                    execute<Player>(){sender, context, argument ->
                        try {
                            sender.getDataContainer()["display_length"]=argument.toInt()
                        }catch (e:Exception){
                            sender.sendMessage(Message.WRONG_NUMBER.colored())
                        }
                    }
                }
            }
            literal("kether"){
                dynamic ("执行一段kether动作"){
                    execute<Player>(){sender, context, argument ->

                        println(argument.asList().eval(sender))

                    }
                }
            }
            literal("debug"){
                dynamic ("选择模型"){
                    suggestion<CommandSender>(uncheck = false){ sender, context ->
                        ChatGPT.Model.values().asList()
                    }
                    dynamic ("输入文本") {
                        suggestion<CommandSender>(uncheck = true){
                            sender, context ->
                            listOf("请输入你想和ai机器人说的话".colored())
                        }
                        execute<CommandSender>(){sender, context, argument ->
                            if(echo&& sender is Player){
                                sender.chat(argument)
                            }
                            sender.sendMessage("&a请等待ai机器人的应答...".colored())
                            submit (async = true){
                                var response=gpt.input(context.argumentOrNull(0)!!)
                                //var d=FancyChatDisplayer("&aChatGpt&8>>",true,"&c&l@%player_name%","&f%text%",true)
                                var d=SingletonDisplayer(){p,s->p.actionBar(s)}
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