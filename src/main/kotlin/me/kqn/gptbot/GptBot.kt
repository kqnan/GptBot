package me.kqn.gptbot

import me.kqn.gptbot.Bot.ChatGPT
import me.kqn.gptbot.Displayer.SingletonDisplayer
import me.kqn.gptbot.Holo.HoloDisplay
import net.minecraft.network.protocol.game.PacketPlayOutEntity
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.io.newFile
import taboolib.common.platform.Plugin
import taboolib.common.platform.command.command
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.getProxyPlayer
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.submitAsync
import taboolib.common.util.asList
import taboolib.common5.Baffle
import taboolib.common5.FileWatcher
import taboolib.expansion.*
import taboolib.module.chat.colored
import taboolib.platform.BukkitPlugin
import taboolib.platform.compat.VaultService
import taboolib.platform.util.actionBar
import taboolib.platform.util.onlinePlayers
import java.io.File
import java.util.concurrent.TimeUnit

object GptBot : Plugin() {

    lateinit var gpt: ChatGPT
    var echo=true
    lateinit var plugin:JavaPlugin

    // TODO: 2023/1/9
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
        HookPlugin.holoDisplay?.onDisable()
    }
    @SubscribeEvent
    fun setupData(e: PlayerJoinEvent) {
        // ��ʼ���������

        e.player.setupDataContainer()

    }

    @SubscribeEvent
    fun releaseDAta(e: PlayerQuitEvent) {
        // �ͷ������������
        e.player.releaseDataContainer()
    }
    fun registercommand(){
        command(name="GptBot", aliases = listOf("gb,bot,chatbot,cb")){
            createHelper()
            literal("set-length"){
                dynamic (comment = "���û�����������ı��������ʾ����"){
                    execute<Player>(){sender, context, argument ->
                        var len=0
                        try {
                            len=argument.toInt()
                        }catch (e:Exception){
                            sender.sendMessage(Message.WRONG_NUMBER.colored())
                            return@execute
                        }
                        if(len>ConfigObject.token_len.toInt()){
                            sender.sendMessage(Message.EXCEED_LIMIT.replace("%token_length%",ConfigObject.token_len.toString()).colored())
                            return@execute
                        }
                        sender.getDataContainer()["display_length"]=argument.toInt()
                        sender.sendMessage(Message.SUCCESS_SET.colored())
                    }
                }
            }
            literal("reload"){
                execute<CommandSender>(){sender, context, argument ->
                    ChatEvent.baffle.resetAll()
                    ChatEvent.baffle10s.resetAll()
                    ChatEvent.baffle= Baffle.of(ConfigObject.autoChat_interval.toInt())
                    ChatEvent.baffle10s= Baffle.of(ConfigObject.cool.toLong(),TimeUnit.SECONDS)
                    sender.sendMessage("&a�������".colored())
                }
            }
            literal("kether"){
                dynamic ("ִ��һ��kether����"){
                    execute<Player>(){sender, context, argument ->

                        debug(argument.asList().eval(sender)?.get().toString())

                    }
                }
            }
            literal("debug"){
                dynamic ("ѡ��ģ��"){
                    suggestion<CommandSender>(uncheck = false){ sender, context ->
                        ChatGPT.Model.values().asList()
                    }
                    dynamic ("�����ı�") {
                        suggestion<CommandSender>(uncheck = true){
                            sender, context ->
                            listOf("�����������ai������˵�Ļ�".colored())
                        }
                        execute<CommandSender>(){sender, context, argument ->
                            if(echo&& sender is Player){
                                sender.chat(argument)
                            }
                            sender.sendMessage("&a��ȴ�ai�����˵�Ӧ��...".colored())
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
//ctrl+q�鿴�ĵ�  f2��λ������ shift+f2��λ��һ��������  shift+q����ƶ�����β shift+w����ƶ�������

        }
    }
}