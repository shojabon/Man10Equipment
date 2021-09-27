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

class ToggleEquipableItemCommand(private val plugin: Man10Equipment) : CommandExecutor {

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

        var currentTypeString = sItem.getCustomData(plugin, "equipable")
        if(currentTypeString == null) currentTypeString = "false"

        var currentItemIsHelmet = currentTypeString.equals("true")

        currentItemIsHelmet = !currentItemIsHelmet


        sItem.setCustomData(plugin, "equipable", currentItemIsHelmet.toString())
        p.inventory.setItemInMainHand(sItem.build())
        if(currentItemIsHelmet){
            p.sendMessage(Man10Equipment.prefix + "§a§lアイテムを装備化しました")
        }else{
            p.sendMessage(Man10Equipment.prefix + "§c§lアイテムの装備化を解除しました")
        }
        return false
    }
}