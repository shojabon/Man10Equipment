package com.shojabon.man10equipment.listeners

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import com.shojabon.man10equipment.Man10Equipment
import com.shojabon.mcutils.Utils.BaseUtils
import com.shojabon.mcutils.Utils.SItemStack
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.roundToInt

class Man10EquipmentListener(val plugin: Man10Equipment) : Listener {

    @EventHandler
    fun armorEquipEvent(e: PlayerArmorChangeEvent){
        Man10Equipment.api.clearUserEquipmentCache(e.player)
        Man10Equipment.api.addUserEquipmentCache(e.player)
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent){
        Man10Equipment.api.addUserEquipmentCache(e.player)
    }

    @EventHandler
    fun onPlayerLeave(e: PlayerQuitEvent){
        Man10Equipment.api.clearUserEquipmentCache(e.player)
    }

    @EventHandler
    fun equipEnabledListener(e: InventoryClickEvent){
        if(e.inventory.type != InventoryType.CRAFTING) return
        if(e.rawSlot !in 5..8) return
        if(e.whoClicked.itemOnCursor.type == Material.AIR) return
        if(SItemStack(e.whoClicked.itemOnCursor).getCustomData(plugin, "equipable") == null) return
        val equipable = SItemStack(e.whoClicked.itemOnCursor).getCustomData(plugin, "equipable").equals("true")
        if(!equipable) return
        e.isCancelled = true
        val armorContent = e.whoClicked.inventory.armorContents
        armorContent.reverse()
        val itemInSlot = armorContent[e.rawSlot - 5]
        armorContent[e.rawSlot - 5] = e.whoClicked.itemOnCursor
        armorContent.reverse()
        e.whoClicked.setItemOnCursor(itemInSlot)
        e.whoClicked.inventory.setArmorContents(armorContent)
    }

    @EventHandler
    fun durabilityListener(e: EntityDamageByEntityEvent){
        if(e.entity !is Player) return
        val p = e.entity as Player
        val contents = p.inventory.armorContents.clone()
        for(i in contents.indices){
            val item = contents[i] ?: continue
            val sItem = SItemStack(item)
            if(sItem.getCustomData(plugin, "defaultDurability") == null) continue



            val default = sItem.getCustomData(plugin, "defaultDurability").toDouble()
            var remaining = sItem.getCustomData(plugin, "durability").toDouble()

            remaining -= e.damage / 4
            sItem.setCustomData(plugin, "durability", remaining.toString())

            sItem.damage = sItem.maxDamage - ((remaining/default) * sItem.maxDamage.toDouble()).roundToInt()
            val lore = sItem.lore
            lore[lore.size-1] = "§c耐久力:${remaining.toInt()}/${default.toInt()}"
            sItem.lore = lore
            if(remaining.toInt() > 0) {
                contents[i] = sItem.build()
            }else{
                contents[i] = ItemStack(Material.AIR)
            }
        }
        p.inventory.setArmorContents(contents)
    }

}