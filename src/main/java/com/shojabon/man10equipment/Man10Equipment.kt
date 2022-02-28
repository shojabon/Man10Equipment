package com.shojabon.man10equipment

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import com.shojabon.man10equipment.listeners.Man10EquipmentListener
import com.shojabon.man10lock.commands.Man10EquipmentCommandRouter
import com.shojabon.mcutils.Utils.MySQL.ThreadedMySQLAPI
import com.shojabon.mcutils.Utils.SItemStack
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class Man10Equipment : JavaPlugin(), Listener {

    companion object{

        lateinit var mysql : ThreadedMySQLAPI
        lateinit var config : FileConfiguration
        lateinit var api: Man10EquipmentAPI
        var prefix : String? = "§6[§8Man10Equipment§6]"
        var serverName: String? = ""


        lateinit var disabledWorlds: MutableList<String>


    }

    override fun onEnable() {
        // Plugin startup logic

        saveDefaultConfig()
        Man10Equipment.config = config
        disabledWorlds = Companion.config.getStringList("effectDisabledWorlds")
        prefix = config.getString("prefix")
        serverName = config.getString("server")

        api = Man10EquipmentAPI(this)

        val commandRouter =  Man10EquipmentCommandRouter(this)
        commandRouter.pluginPrefix = prefix
        getCommand("mequip")?.setExecutor(commandRouter)
        getCommand("mequip")?.tabCompleter = commandRouter
        getCommand("mhat")?.setExecutor(this)

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

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(!command.name.contentEquals("mhat")){
            return false
        }
        if(!sender.hasPermission("mhat.use")){
            sender.sendMessage("§e§l[§d§lm§a§lhat§e§l]§4権限が不足してます")
            return false
        }
        val p: Player  = sender as Player
        val item = p.inventory.itemInMainHand
        if(item.type == Material.AIR){
            return false
        }
        val helmet = p.inventory.helmet
        p.inventory.helmet = item
        p.inventory.setItemInMainHand(helmet)
        p.sendMessage("§e§l[§d§lm§a§lhat§e§l]§d頭にかぶりました")
        return true
    }

}