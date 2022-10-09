package com.comissar.politics.utils;

import com.comissar.politics.Politics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private static Politics plugin;

    public static void init(Politics main){
         plugin = main;
    }

    public static YamlConfiguration getConfig(String fileName){// needs path + name + ".yml" at the end


        if(!(new File(plugin.getDataFolder(), fileName).exists())) {

            plugin.saveResource(fileName, false);

        }

        File file = new File(plugin.getDataFolder(), fileName);
        return YamlConfiguration.loadConfiguration(file);
    }
    public static void SaveConfiguration(FileConfiguration toSave, String fileName){

        try {
            toSave.save(new File(plugin.getDataFolder(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static YamlConfiguration getDataFile(String fileName){

        File file = new File(plugin.getDataFolder(), "/data/" + fileName + ".yml");
        return YamlConfiguration.loadConfiguration(file);

    }
    public static void saveDataFile(FileConfiguration toSave, String fileName){
        try {
            toSave.save(new File(plugin.getDataFolder(), "/data/" + fileName + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void removeDataFile(String fileName){
        new File(plugin.getDataFolder(), "/data/" + fileName + ".yml").delete();
    }
    public static void renameDataFile(String fileName, String newFileName){
        File file = new File(plugin.getDataFolder(), "/data/" + fileName + ".yml");
        file.renameTo(new File(plugin.getDataFolder(), "/data/" + newFileName + ".yml"));
    }
    public static void createDirs(String dirs){
        new File(plugin.getDataFolder(), "/data").mkdirs();
    }





}

