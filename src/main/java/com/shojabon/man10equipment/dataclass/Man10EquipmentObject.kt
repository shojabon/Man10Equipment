package com.shojabon.man10equipment.dataclass

import com.shojabon.man10equipment.Man10Equipment
import com.shojabon.man10equipment.Man10EquipmentAPI
import com.shojabon.mcutils.Utils.BaseUtils
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitTask
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Man10EquipmentObject (val config: YamlConfiguration){
    val plugin = Bukkit.getPluginManager().getPlugin("Man10Equipment")

    val settings = HashMap<Int, Man10EquipmentSetting>()
    val playersWithEquipment = HashMap<UUID, Int>()

    var enabledWorld = ArrayList<String>()
    var disabledWorld = ArrayList<String>()

    val tasks = ArrayList<BukkitTask>()

    init {
        //load config
        loadConfig()
    }

    fun getSetting(amountOfEquipment: Int): Man10EquipmentSetting? {
        return settings[amountOfEquipment]
    }

    private fun loadConfig(){
        loadEffects()
        loadEnabledDisabledWorld()
        //initiate tasks
        createAllTasks()

    }

    private fun loadEnabledDisabledWorld(){
        if(config.contains("enabledWorlds")){
            this.enabledWorld = config.getStringList("enabledWorlds") as ArrayList<String>
        }
        if(config.contains("disabledWorlds")){
            this.disabledWorld = config.getStringList("disabledWorlds") as ArrayList<String>
        }
    }

    private fun loadEffects(){
        if(!config.contains("effects")) return

        for(amountOfEquipment in config.getConfigurationSection("effects")?.getKeys(false)!!){
            if(!BaseUtils.isInt(amountOfEquipment)) continue
            val setting = Man10EquipmentSetting(this, Integer.parseInt(amountOfEquipment))

            //load attributes
            if(config.contains("effects.$amountOfEquipment.attributes")){
                val attributes = config.getStringList("effects.$amountOfEquipment.attributes");
                for(attributesRecord in attributes){
                    //if not 2 variables
                    val local = attributesRecord.split(" ")
                    if(local.size != 2) continue
                    if(!BaseUtils.isDouble(local[1]))continue
                    try{
                        Attribute.valueOf(local[0]);
                    }catch (e: Exception){
                        continue
                    }
                    setting.addAttribute(Pair(Attribute.valueOf(local[0]), local[1].toDouble()))
                }
            }

            //load commands
            if(config.contains("effects.$amountOfEquipment.commands")){
                for(commandPerTick in config.getConfigurationSection("effects.$amountOfEquipment.commands")!!.getKeys(false)){
                    if(!BaseUtils.isInt(commandPerTick)) continue
                    val commandRecord = config.getStringList("effects.$amountOfEquipment.commands.$commandPerTick")
                    for(command in commandRecord){
                        setting.addCommand(Integer.parseInt(commandPerTick), command)
                    }
                }
            }

            //load potions
            if(config.contains("effects.$amountOfEquipment.potions")){
                for(potionPerTick in config.getConfigurationSection("effects.$amountOfEquipment.potions")!!.getKeys(false)){
                    if(!BaseUtils.isInt(potionPerTick)) continue
                    val potionRecord = config.getStringList("effects.$amountOfEquipment.potions.$potionPerTick")
                    for(potion in potionRecord){
                        val splitArg = potion.split(" ")
                        if(!BaseUtils.isInt(splitArg[0]) || !BaseUtils.isInt(splitArg[1])) continue
                        val potionType = PotionEffectType.getByName(splitArg[2]) ?: continue

                        setting.addPotionEffects(Integer.parseInt(potionPerTick), Triple(Integer.parseInt(splitArg[0]), Integer.parseInt(splitArg[1]), potionType))
                    }
                }
            }

            settings[Integer.parseInt(amountOfEquipment)] = setting

        }
    }

    fun addUser(player: Player, amountOfEquipment: Int){

        playersWithEquipment[player.uniqueId] = amountOfEquipment

        // single execute objects

        //execute
        val setting = getSetting(amountOfEquipment) ?: return
        setting.applyAttributes(player)
        setting.executeSingleCommands(player)
        setting.executeSinglePotionEffect(player)

    }

    fun removeUser(player: Player){
        if(!playersWithEquipment.containsKey(player.uniqueId)){
            return
        }
        playersWithEquipment.remove(player.uniqueId)
        Man10EquipmentAPI.resetPlayerAttributes(player)
    }

    private fun allowedToUseInWorld(world: String): Boolean {
        if(enabledWorld.size != 0){
            return enabledWorld.contains(world)
        }
        if(disabledWorld.size != 0){
            return !disabledWorld.contains(world)
        }
        return true
    }

    //tasks

    private fun getAllTaskIds(): ArrayList<Int> {
        val result = ArrayList<Int>()
        for(setting in settings.values){
            for(id in setting.getTaskIds()){
                if(!result.contains(id)) result.add(id)
            }
        }
        return result
    }

    private fun createAllTasks(){
        if(plugin == null)return
        tasks.add(Bukkit.getScheduler().runTaskTimer(plugin, Runnable { freezePlayerTask() }, 0L, 8))
        val ids = getAllTaskIds()
        for(id in ids){
            if(id == 0) continue
            tasks.add(Bukkit.getScheduler().runTaskTimer(plugin, Runnable { executeEquipmentEffectTask(id) }, 0L, id.toLong()))
        }
    }

    fun stopAllTasks(){
        for(task in tasks){
            task.cancel()
        }
    }

    private fun executeEquipmentEffectTask(taskId: Int){
        for(uuid in playersWithEquipment.keys){
            val setting = settings[playersWithEquipment[uuid]]?: continue
            setting.executeCommandAndPotion(uuid, taskId.toLong())
        }
    }

    private fun freezePlayerTask(){
        for(uuid in playersWithEquipment.keys){
            val p = Bukkit.getPlayer(uuid) ?: continue
            if(!p.isOnline) continue
            if(allowedToUseInWorld(p.world.name)) continue
            val loc = Man10EquipmentAPI.playerLocationCache[uuid] ?: continue
            p.teleport(loc)
            p.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 3, 10))
            p.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 3, 10))
            p.sendTitle("§c§l使用禁止アーマーを使用しています", "§c§l脱いでください")

        }
    }



}