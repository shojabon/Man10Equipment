package com.shojabon.man10equipment.commands.subCommands

import com.shojabon.man10equipment.Man10Equipment
import com.shojabon.mcutils.Utils.SConfigFile
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.io.File

class CreateConfigFileCommand(private val plugin: Man10Equipment) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val result = SConfigFile.saveResource(plugin, "ArmorTemplate.yml", plugin.dataFolder.toString() + File.separator + "types" + File.separator + args[1] + ".yml")
        if(!result){
            sender.sendMessage(Man10Equipment.prefix.toString() + "§c§l内部エラーが発生しました")
        }else{
            sender.sendMessage(Man10Equipment.prefix.toString() + "§a§lファイルを作成しました")
        }

//        plugin.reloadConfig()
//        config = plugin.config
//        Man10Lock.mysql = ThreadedMySQLAPI(plugin)
//        Man10Lock.prefix = config.getString("prefix")
//        Man10Lock.serverName = config.getString("server")
//
//        Man10LockAPI.worldConfigurations.clear()
//        Man10LockAPI.ownerLockCount.clear()
//        Man10LockAPI.ownerLockBlock.clear()
//        Man10LockAPI.lockedBlockData.clear()
//
//        Man10Lock.api.loadConfig()
//        Man10Lock.api.loadAllLockedBlocks()
//
//        sender.sendMessage(Man10Lock.prefix.toString() + "§a§lプラグインがリロードされました")
//        return true
        return false
    }
}