package me.kqn.gptbot.Holo

import org.bukkit.Location
import org.bukkit.entity.Player

interface HoloDisplay {
    /**չʾ����ҿ��������λ��+offset����holo
     * */
    fun showToPlayer(player: Player,message:Array<String>,offset:Location)
    /**�Ƴ���ҵ�holo
     * */
    fun removeHolo(player:Player)
    /**
     * ����ر�ʱ����
     * */
    fun onDisable()
}