package com.comissar.politics;

import com.comissar.politics.objects.Building;
import com.comissar.politics.objects.Form;
import com.comissar.politics.objects.TownPolitic;
import com.comissar.politics.utils.FileManager;
import com.comissar.politics.utils.GUIMaster;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class PoliticsUniverse {

    public static HashMap<String, TownPolitic> townsPolitic = new HashMap<String, TownPolitic>();

    public static HashMap<Integer, Form> forms = new HashMap<Integer, Form>();
    public static HashMap<Integer, Building> buildings = new HashMap<Integer, Building>();

    public static int setFormCost = 0;
    public static int changeFormCost = 0;

    //!!!
    private static final int specialF = 5;
    private static final int specialB = 5;
    public static final int[] Llv = {4, 2, 3, 2};
    public static final String[] names = new String[Llv.length];
    //!!!
    public static Logger log;

    public static void init(Politics pl){

        log = pl.getLogger();
        //loading configuration of towny Politics
        FileConfiguration politics = FileManager.getConfig("configuration.yml");
        setFormCost = politics.getInt("cost");
        changeFormCost = politics.getInt("change_cost");


        //loading towny forms
        for(int i = 1; i < specialF; i++){

            String path = "forms." + i;
            Form form = new Form(i, politics.getString(path + ".name"), getBoostDataFromString(politics.getStringList(path + ".player_boost")), getBoostDataFromString(politics.getStringList(path + ".town_boost")));
            forms.put(i, form);
        }

        //loading towny buildings
        for(int i = 1; i < specialB; i++){

            String path = "buildings."+i; //maxLvl = trash

            List<ItemStack[]> itemsCost = new ArrayList<>();
            List<HashMap<Integer, Double>> boosts = new ArrayList<>();
            List<ItemStack[]> itemsIncome = new ArrayList<>();

            itemsCost.add(0, null);
            boosts.add(0, null);
            itemsIncome.add(0, null);

            for(int b = 1; b < Llv[i-1]+1; b++){
                itemsCost.add(b, getItemsDataFromString(politics.getStringList(path + ".levels." + b + ".items_cost")));
                boosts.add(b, getBoostDataFromString(politics.getStringList(path + ".levels." + b + ".boost")));
                itemsIncome.add(b, getItemsDataFromString(politics.getStringList(path + ".levels." + b + ".items_income")));
            }

            Building building = new Building(i, Llv[i-1], politics.getString(path + ".name"), itemsCost, boosts, itemsIncome);

            //filling names (need edit)
            names[i-1] = "§l" + building.name;
            buildings.put(i, building);
        }
        FileManager.createDirs("/data");

        FileConfiguration towns = FileManager.getConfig("townList.yml");
        for(String s : towns.getStringList("towns")){


            FileConfiguration town = FileManager.getDataFile(s);
            TownPolitic townPolitic = new TownPolitic(s, town.getInt("formID"), getIntegerArray(town.getIntegerList("buildings")), getTBankData(town, s), town.getStringList("boostedPlayers"), town.getStringList("tBankUsers"));
            townsPolitic.put(s, townPolitic);

        }


    }

    public static Integer getPolitic(String name){
        if(townsPolitic.containsKey(name)){
            return townsPolitic.get(name).formID;
        }
        return 0;
    }
    public static boolean isFormedTown(String name) {
        if(townsPolitic.containsKey(name)) {
            return townsPolitic.get(name).formID != 0;
        }
        return false;
    }

    public static void saveNewTownData(TownPolitic townPolitic) {

        FileConfiguration townListConfiguration = FileManager.getConfig("townList.yml");
        List<String> townList = townListConfiguration.getStringList("towns");
        townList.add(townPolitic.name);
        townListConfiguration.set("towns", townList);
        FileManager.SaveConfiguration(townListConfiguration, "townList.yml");

        FileConfiguration townConfig = new YamlConfiguration();
        townConfig.set("name", townPolitic.name);
        townConfig.set("formID" , townPolitic.formID);
        townConfig.set("buildings", townPolitic.buildings);

        townConfig.set("tbank.size", townPolitic.tBank.getSize());
        ItemStack[] items = townPolitic.tBank.getContents();
        for(int i = 0; i < items.length; i++){
            townConfig.set("tbank."+ i, items[i]);
        }


        townConfig.set("tBankUsers", townPolitic.tBankUsers);
        FileManager.saveDataFile(townConfig, townPolitic.name);
    }
    
    
    
    
    public static void SaveTownData(String name,Integer formID){
        FileConfiguration townConfig = FileManager.getDataFile(name);
        townConfig.set("formID", formID);
        FileManager.saveDataFile(townConfig, name);
    }
    public static void SaveTownData(String name, Integer[] buildings){
        FileConfiguration townConfig = FileManager.getDataFile(name);
        townConfig.set("buildings", buildings);
        FileManager.saveDataFile(townConfig, name);
    }
    public static void SaveTownData(String name, Inventory tbank){

        log.info(name);

        FileConfiguration townConfig = FileManager.getDataFile(name);
        townConfig.set("tbank.size", tbank.getSize());
        ItemStack[] items = tbank.getContents();
        for(int i = 0; i < items.length; i++){
            townConfig.set("tbank.inventory."+ i, items[i]);
        }

        FileManager.saveDataFile(townConfig, name);

    }

    public static Inventory getTBankData(FileConfiguration config, String townName){
        Inventory toReturn = Bukkit.createInventory(null, 54, townName);
        if(config.contains("tbank.inventory")) {
            Set<String> keys = config.getConfigurationSection("tbank.inventory").getKeys(false);

            for (String key : keys) {
                toReturn.setItem(Integer.valueOf(key), config.getItemStack("tbank.inventory." + key));
            }
        }
        return toReturn;
    }

    public static void SaveTownData(String name, String key, List<String> value){
        FileConfiguration townConfig = FileManager.getDataFile(name);
        townConfig.set(key, value);
        FileManager.saveDataFile(townConfig, name);
    }

    public static void SaveTownData(String name, String key, String value){
        FileConfiguration townConfig = FileManager.getDataFile(name);
        townConfig.set(key, value);
        FileManager.saveDataFile(townConfig, name);
    }

    public static void SaveTownData(TownPolitic townPolitic){
        FileConfiguration townConfig = FileManager.getDataFile(townPolitic.name);
        townConfig.set("formID", townPolitic.formID);
        townConfig.set("buildings", townPolitic.buildings);
        townConfig.set("tbank", townPolitic.tBank);
        FileManager.saveDataFile(townConfig, townPolitic.name);
    }
    
    
    

    public static void removeTown(String name){
        townsPolitic.remove(name);

        FileConfiguration townListConfiguration = FileManager.getConfig("townList.yml");
        List<String> townList = townListConfiguration.getStringList("towns");
        townList.remove(name);
        townListConfiguration.set("towns", townList);
        FileManager.SaveConfiguration(townListConfiguration, "townList.yml");

        FileManager.removeDataFile(name);
    }

    public static void addTown(String name){
        TownPolitic townPolitic = new TownPolitic(name);
        townsPolitic.put(name, townPolitic);
        saveNewTownData(townPolitic);
    }
    public static void setFormTown(String name, Integer formID){
        TownPolitic townPolitic = townsPolitic.get(name);
        townPolitic.setForm(formID);
        townsPolitic.put(name, townPolitic);
        SaveTownData(name, formID);
    }
    public static void renameTown(String oldName, String newName){

        TownPolitic townPolitic = townsPolitic.get(oldName);
        townPolitic.name = newName;

        townsPolitic.put(newName, townPolitic);
        townsPolitic.remove(oldName);

        FileConfiguration townListConfiguration = FileManager.getConfig("townList.yml");
        List<String> townList = townListConfiguration.getStringList("towns");
        townList.remove(oldName);
        townList.add(newName);
        townListConfiguration.set("towns", townList);
        FileManager.SaveConfiguration(townListConfiguration, "townList.yml");

        SaveTownData(oldName, "name", newName);
        FileManager.renameDataFile(oldName, newName);
    }
    public static void addTBankUser(String townName, String playerName){
        townsPolitic.get(townName).addTBankUser(playerName);
        SaveTownData(townName, "tBankUsers", townsPolitic.get(townName).tBankUsers);
    }
    public static TownPolitic getTownPolitic(String townName) { return townsPolitic.get(townName); }
    public static void removeTBankUser(String townName, String playerName){
        townsPolitic.get(townName).removeTBankUser(playerName);
        SaveTownData(townName, "tBankUsers", townsPolitic.get(townName).tBankUsers);
    }
    public static boolean isTBankUser(String townName, String playerName){
        return townsPolitic.get(townName).tBankUsers.contains(playerName);
    }
    public static Inventory getTBank(String townNane){
        return townsPolitic.get(townNane).tBank;
    }



    public static HashMap<Integer, Double> getBoostDataFromString(List<String> strings){
        HashMap<Integer, Double> toReturn = new HashMap<Integer, Double>();
        for(String s : strings){
            String[] splited = s.split(":");
            toReturn.put(Integer.valueOf(splited[0]), Double.valueOf(splited[1]));
        }

        return toReturn;
    }
    public static ItemStack[] getItemsDataFromString(List<String> strings){
        ItemStack[] toReturn = new ItemStack[strings.size()];
        for(int i = 0; i < strings.size(); i++){

            String[] splited = strings.get(i).split(":");
            toReturn[i] = new ItemStack(Material.getMaterial(splited[0]), Integer.valueOf(splited[1]));
        }

        return toReturn;
    }
    public static Integer[] getIntegerArray(List<Integer> integers){

        Integer[] toReturn = new Integer[integers.size()] ;
        for(int i = 0; i < integers.size(); i++){
            toReturn[i] = integers.get(i);
        }
        return toReturn;
    }


    public static Integer getFormID(String formName){
        for(Form f : forms.values()){
            if(f.name.equalsIgnoreCase(formName)){
                return f.id;
            }
        }
        return -1;
    }
    public static String getFormName(Integer id){
        return forms.get(id).name;
    }

    public static String getFormNameOfTown(String townName){
        return getFormName(getPolitic(townName));
    }
    public static Double getBoostTownForm(String townName, Integer boostID){
        if(isFormedTown(townName)){
            Form f = forms.get(townsPolitic.get(townName).formID);



            if(f.containsTownBoost(boostID)){
                return f.getTownBoost(boostID);
            }
        }
        return null;
    }
    public static Double getBoostPlayerForm(String townName, Integer boostID){
        if(isFormedTown(townName)){

            Form f = forms.get(townsPolitic.get(townName).formID);

            if(f.containsPlayerBoost(boostID)){
                return f.getPlayerBoost(boostID);
            }
        }
        return null;
    }
    public static void saveTBanks(){

        for(TownPolitic townPolitic : townsPolitic.values()){
            if(townPolitic.tBankWasChanged) {
                SaveTownData(townPolitic.name, townPolitic.tBank);
                townPolitic.tBankWasChanged = false;
            }
        }

    }
    public static Integer getBuildingID(String buildingName){
        for(Building building : buildings.values()){
            if(building.name.equals(buildingName)){
                return building.id;
            }
        }
        return null;
    }
    public static Building getBuilding(Integer buildingID){
        return buildings.get(buildingID);
    }
    public static String getBuildingName(Integer buildingID){
        return buildings.get(buildingID).name;
    }
    public static boolean tryGetResources(TownPolitic townPolitic, Integer buildingID){

        for(ItemStack item : buildings.get(buildingID).itemsCost.get(townPolitic.buildings[buildingID-1]+1)){
            if(!townPolitic.tBank.contains(item.getType(), item.getAmount())){
                return false;
            }
        }
        for(ItemStack item : buildings.get(buildingID).itemsCost.get(townPolitic.buildings[buildingID-1]+1)){
            GUIMaster.removeItems(townPolitic.tBank, item.getType(), item.getAmount());
        }
        SaveTownData(townPolitic.name, townPolitic.tBank);
        return true;

    }
    public static String getUpResourcesString(Integer buildingID, Integer level){
        String toReturn = "";
        ItemStack[] upResources = buildings.get(buildingID).itemsCost.get(level);

        for(int i = 0; i < upResources.length - 1; i++){
            toReturn += upResources[i].getType().name() + " x " + upResources[i].getAmount() + ", ";
        }
        if(upResources.length > 0) {
            toReturn += upResources[upResources.length - 1].getType().name() + " x " + upResources[upResources.length - 1].getAmount();
        }
        return toReturn;
    }
    public static String getTBankUsers(String townName){
        String toReturn = "";
        List<String> tBankUsers = townsPolitic.get(townName).tBankUsers;

        for(int i = 0; i < tBankUsers.size() - 1; i++){
            toReturn += tBankUsers.get(i) + ", ";
        }
        if(tBankUsers.size() > 0) {
            toReturn += tBankUsers.get(tBankUsers.size() - 1);
        }
        return toReturn;
    }
    public static void addItemsToTBank(TownPolitic townPolitic,ItemStack[] items){
        for(ItemStack item : items){
            townPolitic.tBank.addItem(item);
        }
        SaveTownData(townPolitic.name, townPolitic.tBank);
    }




}
