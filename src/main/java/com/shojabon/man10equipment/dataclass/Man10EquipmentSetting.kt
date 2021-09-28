package com.shojabon.man10equipment.dataclass

import com.shojabon.man10equipment.Man10Equipment
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Man10EquipmentSetting(val equipmentObject: Man10EquipmentObject, val settingIndex: Int) {

    val attributes = ArrayList<Pair<Attribute, Double>>()
    val commands = HashMap<Int, ArrayList<String>>()
    val potionEffects = HashMap<Int, ArrayList<Triple<Int, Int, PotionEffectType>>>()

    fun addAttribute(attributeAndBase: Pair<Attribute, Double>){
        attributes.add(attributeAndBase)
    }

    fun addCommand(perTick: Int, command: String){
        if(!commands.containsKey(perTick)){
            commands[perTick] = ArrayList()
        }
        commands[perTick]?.add(command)
    }

    fun addPotionEffects(perTick: Int, secondsAmplifierEffect: Triple<Int, Int, PotionEffectType>){
        if(!potionEffects.containsKey(perTick)){
            potionEffects[perTick] = java.util.ArrayList()
        }
        potionEffects[perTick]?.add(secondsAmplifierEffect)
    }

    fun getTaskIds(): ArrayList<Int> {
        val result = ArrayList<Int>()
        for(key in commands.keys){
            if(result.contains(key)) continue
            result.add(key)
        }
        for(key in potionEffects.keys){
            if(result.contains(key)) continue
            result.add(key)
        }
        return result
    }

    //task control

    fun executeCommandAndPotion(uuid: UUID, tickTask: Long){
        //execute command
        val player = Bukkit.getPlayer(uuid)?: return
        if(!player.isOnline) return
        if(Man10Equipment.disabledWorlds.isNotEmpty() && Man10Equipment.disabledWorlds.contains(player.world.name)) return
        if(equipmentObject.playersWithEquipment[uuid] != settingIndex) return

        //execute commands
        if(commands.containsKey(tickTask.toInt())){
            val executingCommands = commands[tickTask.toInt()]
            if(executingCommands != null){
                for(command in executingCommands){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{uuid}", player.uniqueId.toString()).replace("{name}", player.name))
                }
            }
        }

        //execute potion
        if(potionEffects.containsKey(tickTask.toInt())){
            val applyingPotion = potionEffects[tickTask.toInt()]
            if(applyingPotion != null){
                for(potion in applyingPotion){
                    player.addPotionEffect(PotionEffect(potion.third, potion.first, potion.second))
                }
            }
        }



    }

    // user control
    fun applyAttributes(player: Player){
        for(attributeData in attributes){
            if(player.getAttribute(attributeData.first) == null) continue
            player.getAttribute(attributeData.first)!!.baseValue = player.getAttribute(attributeData.first)!!.baseValue + attributeData.second
        }
    }

    fun executeSingleCommands(player: Player){
        if(!commands.containsKey(0)) return
        val executingCommands = commands[0]?: return
        for(command in executingCommands){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{uuid}", player.uniqueId.toString()).replace("{name}", player.name))
        }
    }

    fun executeSinglePotionEffect(player: Player){
        if(!potionEffects.containsKey(0)) return
        val applyingPotion = potionEffects[0]?: return
        for(potion in applyingPotion){
            player.addPotionEffect(PotionEffect(potion.third, potion.first, potion.second))
        }
    }

}