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

class ToggleEnableWorldCommand(private val plugin: Man10Equipment) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val currentWorld = plugin.config.getStringList("effectDisabledWorlds")
        if(currentWorld.contains(args[1])){
            currentWorld.remove(args[1])
            sender.sendMessage(Man10Equipment.prefix + "§c§l" + args[1] + "を消去しました")
        }else{
            currentWorld.add(args[1])
            sender.sendMessage(Man10Equipment.prefix + "§a§l" + args[1] + "を追加しました")
        }
        plugin.config.set("effectDisabledWorlds", currentWorld)
        plugin.saveConfig()
        Man10Equipment.disabledWorlds = Man10Equipment.config.getStringList("effectDisabledWorlds")
        return false
    }
}