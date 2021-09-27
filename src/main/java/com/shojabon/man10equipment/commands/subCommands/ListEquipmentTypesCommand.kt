package com.shojabon.man10equipment.commands.subCommands

import com.shojabon.man10equipment.Man10Equipment
import com.shojabon.mcutils.Utils.SConfigFile
import com.shojabon.mcutils.Utils.SItemStack
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File

class ListEquipmentTypesCommand(private val plugin: Man10Equipment) : CommandExecutor {

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
        if(sItem.getCustomData(plugin, "types") == null){
            p.sendMessage(Man10Equipment.prefix + "§c§lこのアイテムは特殊アイテムではありません")
            return false
        }

        val types = ArrayList<String>()
        for(type in sItem.getCustomData(plugin, "types").split("|")){
            types.add(type)
        }

        p.sendMessage("§e§l====================")
        for(type in types){
            p.sendMessage("§d§l$type")
        }
        p.sendMessage("§e§l====================")


        return false
    }
}