package me.kqn.gptbot.Kether

import me.kqn.gptbot.HookPlugin
import org.bukkit.Bukkit
import org.bukkit.Material
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
    fun actionMoney() = scriptParser {
        val str = it.nextParsedAction()
        actionTake { run(str).str { s -> VaultService.economy?.has(Bukkit.getOfflinePlayer(player().uniqueId),s.toDouble()) ?: error("no Vault depend") } }
    }
    @KetherParser(["takeMoney"])
    fun actionTakeMoney() = scriptParser {
        val str = it.nextParsedAction()
        actionTake { run(str).str { s -> VaultService.economy?.withdrawPlayer(Bukkit.getOfflinePlayer(player().uniqueId),s.toDouble()) ?: error("no Vault depend") } }
    }
    @KetherParser(["giveMoney"])
    fun actionGiveMoney() = scriptParser {
        val str = it.nextParsedAction()
        actionTake { run(str).str { s -> VaultService.economy?.depositPlayer(Bukkit.getOfflinePlayer(player().uniqueId),s.toDouble()) ?: error("no Vault depend") } }
    }
    @KetherParser(["points"])
    fun actionPoints() = scriptParser {
        val str = it.nextParsedAction()
        actionTake { run(str).str { s -> HookPlugin.playerPointsAPI.look(player().uniqueId) >=s.toInt() } }
    }
    @KetherParser(["takePoints"])
    fun actionTakePoints() = scriptParser {
        val str = it.nextParsedAction()
        actionTake { run(str).str { s -> HookPlugin.playerPointsAPI.take(player().uniqueId,s.toInt())} }
    }
    @KetherParser(["givePoints"])
    fun actionGivePoints() = scriptParser {
        val str = it.nextParsedAction()
        actionTake { run(str).str { s -> HookPlugin.playerPointsAPI.give(player().uniqueId,s.toInt()) } }
    }

}

