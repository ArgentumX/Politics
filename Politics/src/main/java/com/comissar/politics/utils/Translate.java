package com.comissar.politics.utils;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class Translate {

    private static FileConfiguration Language;
    private static String[] help;
    private static String[] credits;

    public static void init(){
        Language = FileManager.getConfig("language.yml");
        for(String key : Language.getConfigurationSection("").getKeys(false)) {
            if (key.equals("prefix")) continue;
            Language.set(key, Language.get("prefix") + Language.getString(key));
        }
    }

    public static String of(String key){
        return Language.getString(key);
    }

    public static String[] help(){
        return help;
    }

    public static String[] credits(){
        return credits;
    }

}
