package com.comissar.politics;

import com.comissar.politics.command.TownyBankCommand;
import com.comissar.politics.command.TownyBuildsCommand;
import com.comissar.politics.command.TownyFormCommand;
import com.comissar.politics.events.EventListener;
import com.comissar.politics.utils.FileManager;
import com.comissar.politics.utils.Translate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Politics extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PoliticsUniverse.saveTBanks();
    }

    public void init(){

        FileManager.init(this); //1
        Translate.init();
        PoliticsUniverse.init(this);


        Bukkit.getPluginManager().registerEvents(new EventListener(this), this);

        getCommand("tforms").setExecutor(new TownyFormCommand(this));
        getCommand("tbuilds").setExecutor(new TownyBuildsCommand(this));
        getCommand("tbank").setExecutor(new TownyBankCommand(this));

    }
}
