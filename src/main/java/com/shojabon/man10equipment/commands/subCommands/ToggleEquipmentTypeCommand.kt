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

class ToggleEquipmentTypeCommand(private val plugin: Man10Equipment) : CommandExecutor {

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

        val types = ArrayList<String>()
        var currentTypeString = sItem.getCustomData(plugin, "types")
        if(currentTypeString == null) currentTypeString = ""
        for(type in currentTypeString.split("|")){
            types.add(type)
        }
        if(types.contains(args[2])){
            p.sendMessage(Man10Equipment.prefix + "§c§lこのタイプはすでに追加されています")
            return false
        }
        types.add(args[2])

        var result = ""
        for(type in types){
            if(type == "") continue
            result += "$type|"
        }
        sItem.setCustomData(plugin, "types", result.substring(0,result.length-1))
        p.inventory.setItemInMainHand(sItem.build())
        p.sendMessage(Man10Equipment.prefix + "§a§lタイプを追加しました")
        return false
    }
}