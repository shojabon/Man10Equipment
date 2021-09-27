package com.shojabon.man10equipment

import com.shojabon.man10equipment.dataclass.Man10EquipmentObject
import com.shojabon.mcutils.Utils.SConfigFile
import com.shojabon.mcutils.Utils.SItemStack
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class Man10EquipmentAPI (val plugin: Man10Equipment){

    companion object{
        var equipmentTypes = HashMap<String, Man10EquipmentObject>()

        fun resetPlayerAttributes(player: Player){
            for(attribute in Attribute.values()){
                val playerAttribute = player.getAttribute(attribute)?: continue
                //movement speed attribute
                if(playerAttribute.attribute == Attribute.GENERIC_MOVEMENT_SPEED){
                    playerAttribute.baseValue = 0.1
                    continue
                }
                playerAttribute.baseValue = playerAttribute.defaultValue
            }
        }
    }

    init {
        loadAll()
    }

    fun loadAll(){

        val files = SConfigFile.getAllFileNameInPath(plugin.dataFolder.toString() + File.separator + "types")
        for(file in files){
            if(!file.name.contains(".yml")) continue

            val configFile = SConfigFile.getConfigWithDefaultValues(plugin, "ArmorTemplate.yml", file.path) ?: continue

            equipmentTypes[file.name.replace(".yml", "")] = Man10EquipmentObject(configFile)
        }
    }

    fun getEquipment(name: String): Man10EquipmentObject? {
        if(!equipmentTypes.containsKey(name)) return null;
        return equipmentTypes[name]
    }

    fun clearUserEquipmentCache(player: Player){
        for(equipment in equipmentTypes.values){
            equipment.removeUser(player)
        }
    }

    private fun addUserEquipmentCache(player: Player, equipment: String, amount: Int){
        if(!equipmentTypes.containsKey(equipment)){
            return
        }
        equipmentTypes[equipment]!!.addUser(player, amount)
    }

    fun addUserEquipmentCache(player: Player){
        val resultMap = HashMap<String, Int>()
        for(item in player.inventory.armorContents){
            item?: continue
            val sItem = SItemStack(item)
            if(sItem.getCustomData(plugin, "types") == null) continue
            for(type in sItem.getCustomData(plugin, "types").split("|")){

                if(!resultMap.containsKey(type)){
                    resultMap[type] = 0
                }

                resultMap[type] = resultMap[type]!!.plus(1)

            }
        }
        clearUserEquipmentCache(player)
        for(type in resultMap.keys){
            addUserEquipmentCache(player, type, resultMap[type]!!)
        }
    }

    //tasks
    fun stopAllTasks(){
        for(type in equipmentTypes.values){
            type.stopAllTasks()
        }
    }


}