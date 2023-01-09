package me.kqn.gptbot.Kether

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.common.platform.function.adaptPlayer
import taboolib.common5.Coerce
import taboolib.module.kether.*
import taboolib.platform.BukkitAdapter
import taboolib.platform.compat.VaultService
import taboolib.platform.type.BukkitPlayer

@PlatformSide([Platform.BUKKIT])
object ExpansionKether {

    @KetherParser(["money"])
    fun actionMoney() = combinationParser {
        it.group(text()).apply(it) { money -> now {
            VaultService.economy?.has(Bukkit.getOfflinePlayer(player().uniqueId),money.toDouble())
         } }
    }

}