package com.shojabon.man10lock.commands

import com.shojabon.man10equipment.Man10Equipment
import com.shojabon.man10equipment.commands.subCommands.*
import com.shojabon.mcutils.Utils.SCommandRouter.SCommandArgument
import com.shojabon.mcutils.Utils.SCommandRouter.SCommandObject
import com.shojabon.mcutils.Utils.SCommandRouter.SCommandRouter

class Man10EquipmentCommandRouter (private val plugin: Man10Equipment): SCommandRouter() {

    init{
        registerCommands()
        registerEvents()
    }

    private fun registerEvents(){
        setNoPermissionEvent { e -> e.sender.sendMessage(Man10Equipment.prefix.toString() + "§c§lあなたは権限がありません") }
        setOnNoCommandFoundEvent { e -> e.sender.sendMessage(Man10Equipment.prefix.toString() + "§c§lコマンドが存在しません") }
    }

    private fun registerCommands(){
        addCommand(
            SCommandObject()
                .addArgument(SCommandArgument().addAllowedString("reload")).addRequiredPermission("man10lock.reload")
                .addExplanation("コンフィグをリロードする").setExecutor(ReloadCommand(plugin))
        )

        // config functions

        addCommand(
            SCommandObject()
                .addArgument(SCommandArgument().addAllowedString("create"))
                .addArgument(SCommandArgument().addAlias("名前"))
                .addRequiredPermission("man10equipment.create")
                .addExplanation("装備シリーズファイルを作成する").setExecutor(CreateConfigFileCommand(plugin))
        )

        // type setting functions
        addCommand(
            SCommandObject()
                .addArgument(SCommandArgument().addAllowedString("types"))
                .addArgument(SCommandArgument().addAllowedString("list"))
                .addRequiredPermission("man10equipment.types.list")
                .addExplanation("装備シリーズ属性を表示する").setExecutor(ListEquipmentTypesCommand(plugin))
        )

        addCommand(
            SCommandObject()
                .addArgument(SCommandArgument().addAllowedString("types"))
                .addArgument(SCommandArgument().addAllowedString("add"))
                .addArgument(SCommandArgument().addAlias("タイプ名"))
                .addRequiredPermission("man10equipment.types.add")
                .addExplanation("装備シリーズ属性を追加する").setExecutor(ToggleEquipmentTypeCommand(plugin))
        )


        addCommand(
            SCommandObject()
                .addArgument(SCommandArgument().addAllowedString("types"))
                .addArgument(SCommandArgument().addAllowedString("remove"))
                .addArgument(SCommandArgument().addAlias("タイプ名"))
                .addRequiredPermission("man10equipment.types.remove")
                .addExplanation("装備シリーズ属性を削除する").setExecutor(RemoveEquipmentTypeCommand(plugin))
        )

        addCommand(
            SCommandObject()
                .addArgument(SCommandArgument().addAllowedString("equipable"))
                .addArgument(SCommandArgument().addAllowedString("toggle"))
                .addRequiredPermission("man10equipment.equipable.toggle")
                .addExplanation("アイテムを装備可能にする").setExecutor(ToggleEquipableItemCommand(plugin))
        )

        addCommand(
            SCommandObject()
                .addArgument(SCommandArgument().addAllowedString("test"))
                .addRequiredPermission("man10equipment.test")
                .addExplanation("テストコマンド").setExecutor(TestCommand(plugin))
        )


    }
}