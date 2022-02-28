package com.shojabon.man10equipment.commands.subCommands

import com.shojabon.man10equipment.Man10Equipment
import com.shojabon.mcutils.Utils.SConfigFile
import com.shojabon.mcutils.Utils.SItemStack
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File
import kotlin.math.roundToInt

class RepairAllDurabilityCommand(private val plugin: Man10Equipment) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val p = Bukkit.getPlayer(args[2]) ?: return false

        val armorContents = p.inventory.armorContents
        for(i in armorContents.indices){
            val item = armorContents[i] ?: continue
            val armorItem = SItemStack(item)
            if(armorItem.getCustomData(plugin,"defaultDurability") == null) continue
            armorItem.setCustomData(plugin, "durability", armorItem.getCustomData(plugin, "defaultDurability"))
            armorItem.damage = 0

            val lore = armorItem.lore
            if(lore.size == 0) lore.add("")
            lore[lore.size-1] = "§c耐久力:${armorItem.getCustomData(plugin,"defaultDurability")}/${armorItem.getCustomData(plugin,"defaultDurability")}"
            armorItem.lore = lore
            armorContents[i] = armorItem.build()
        }
        p.inventory.setArmorContents(armorContents)
        p.sendMessage(Man10Equipment.prefix + "§a§l耐久を回復しました")
        return false
    }
}