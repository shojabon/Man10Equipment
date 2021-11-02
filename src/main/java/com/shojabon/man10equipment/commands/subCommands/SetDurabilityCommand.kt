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
import java.io.File
import kotlin.math.roundToInt

class SetDurabilityCommand(private val plugin: Man10Equipment) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if(sender !is Player){
            sender.sendMessage(Man10Equipment.prefix + "§c§lこのコマンドはプレイヤーからのみしか実行できません")
            return false
        }

        val p = sender as Player
        if(p.inventory.itemInMainHand.type == Material.AIR){
            p.sendMessage(Man10Equipment.prefix + "§c§lアイテムを手に持ってください")
            return false
        }

        val sItem = SItemStack(p.inventory.itemInMainHand)

        if(sItem.getCustomData(plugin,"defaultDurability") == null){
            p.sendMessage(Man10Equipment.prefix + "§c§lすデフォルト耐久値が設定されていません")
            return false
        }

        if(args[2] == "0"){
            p.sendMessage(Man10Equipment.prefix + "§c§l耐久値をリセットしました")
            sItem.removeCustomData(plugin, "durability")
            p.inventory.setItemInMainHand(sItem.build())
            return true
        }


        sItem.setCustomData(plugin, "durability", args[2].toInt().toString())
        sItem.damage = sItem.maxDamage - ((args[2].toDouble()/sItem.getCustomData(plugin,"defaultDurability").toDouble()) * sItem.maxDamage.toDouble()).roundToInt()
        p.inventory.setItemInMainHand(sItem.build())
        p.sendMessage(Man10Equipment.prefix + "§a§lアイテムの耐久値を" + args[2] + "に設定しました")
        return false
    }
}