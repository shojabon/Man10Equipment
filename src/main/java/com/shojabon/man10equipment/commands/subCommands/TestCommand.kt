package com.shojabon.man10equipment.commands.subCommands

import com.shojabon.man10equipment.Man10Equipment
import com.shojabon.man10equipment.Man10EquipmentAPI
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TestCommand(private val plugin: Man10Equipment) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val test = Man10Equipment.api.getEquipment("test") ?: return false
        test.playersWithEquipment[(sender as Player).uniqueId] = 1
        return false
    }
}