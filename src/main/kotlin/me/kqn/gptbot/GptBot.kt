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

    // TODO: 2023/1/9  1.����@������ ����Ҫ��һ��ѻ���Ȩ�ޣ�����ִ�ж���  2.��Ϸ�и���apikey 3.��Ϸ�в鿴apikey�����  4.�Զ����ص���϶�̫��
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
                        try {
                            sender.getDataContainer()["display_length"]=argument.toInt()
                        }catch (e:Exception){
                            sender.sendMessage(Message.WRONG_NUMBER.colored())
                        }
                    }
                }
            }
            literal("kether"){
                dynamic ("ִ��һ��kether����"){
                    execute<Player>(){sender, context, argument ->

                        println(argument.asList().eval(sender))

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