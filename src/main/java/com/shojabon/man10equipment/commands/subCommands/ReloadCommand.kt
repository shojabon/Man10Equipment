package com.shojabon.man10equipment.commands.subCommands

import com.shojabon.man10equipment.Man10Equipment
import com.shojabon.man10equipment.Man10EquipmentAPI
import com.shojabon.mcutils.Utils.MySQL.ThreadedMySQLAPI
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ReloadCommand(private val plugin: Man10Equipment) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        plugin.reloadConfig()
        Man10Equipment.api.stopAllTasks()
        Man10Equipment.config = plugin.config
        Man10Equipment.disabledWorlds = Man10Equipment.config.getStringList("effectDisabledWorlds")
        Man10Equipment.api = Man10EquipmentAPI(plugin)
        Man10Equipment.prefix = plugin.config.getString("prefix")
        for(player in Bukkit.getOnlinePlayers()){
            Man10Equipment.api.addUserEquipmentCache(player)
        }

        sender.sendMessage(Man10Equipment.prefix.toString() + "§a§lプラグインがリロードされました")
        return false
    }
}