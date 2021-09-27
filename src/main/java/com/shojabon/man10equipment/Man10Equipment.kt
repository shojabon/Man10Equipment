package com.shojabon.man10equipment

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import com.shojabon.man10equipment.listeners.Man10EquipmentListener
import com.shojabon.man10lock.commands.Man10EquipmentCommandRouter
import com.shojabon.mcutils.Utils.MySQL.ThreadedMySQLAPI
import com.shojabon.mcutils.Utils.SItemStack
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.plugin.java.JavaPlugin

class Man10Equipment : JavaPlugin(), Listener {

    companion object{

        lateinit var mysql : ThreadedMySQLAPI
        lateinit var config : FileConfiguration
        lateinit var api: Man10EquipmentAPI
        var prefix : String? = "§6[§6Man10Lock§6]"
        var serverName: String? = ""


    }

    override fun onEnable() {
        // Plugin startup logic

        saveDefaultConfig()
        Man10Equipment.config = config
        mysql = ThreadedMySQLAPI(this)
        prefix = config.getString("prefix")
        serverName = config.getString("server")

        api = Man10EquipmentAPI(this)

        val commandRouter =  Man10EquipmentCommandRouter(this)
        commandRouter.pluginPrefix = prefix
        getCommand("mequip")?.setExecutor(commandRouter)
        getCommand("mequip")?.tabCompleter = commandRouter

        server.pluginManager.registerEvents(Man10EquipmentListener(this), this)

        for(player in Bukkit.getOnlinePlayers()){
            api.clearUserEquipmentCache(player)
            api.addUserEquipmentCache(player)
        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
        for(player in Bukkit.getOnlinePlayers()){
            api.clearUserEquipmentCache(player)
        }
    }

}