package com.comissar.politics.objects;

import com.comissar.politics.PoliticsUniverse;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class TownPolitic {

    public String name;
    public Integer formID;
    public Integer[] buildings;

    public Inventory tBank;
    public boolean tBankWasChanged = false;

    public List<String> boostedPlayers;
    public List<String> tBankUsers;

    //Data were taken from save
    public TownPolitic(String name, Integer formID, Integer[] buildings, Inventory tBank, List<String> boostedPlayers, List<String> tBankUsers){
        this.name = name;
        this.formID = formID;
        this.buildings = buildings;
        this.tBank = tBank;
        this.boostedPlayers = boostedPlayers;
        this.tBankUsers = tBankUsers;
    }
    public TownPolitic(String name){
        this.name = name;
        formID = 0;
        buildings = new Integer[PoliticsUniverse.Llv.length];
        for(int i = 0; i < buildings.length; i++) {
            buildings[i] = 0;
        }
        tBank = Bukkit.createInventory(null, 54, name);
        tBankUsers = new ArrayList<>();
    }

    //try to upgrade building
    public boolean TryUpgrade(int buildingID){
        if(PoliticsUniverse.Llv[buildingID] > buildings[buildingID]){
            buildings[buildingID]++;
            return true;
        }
        return false;
    }
    public void setForm(int formID){
        this.formID = formID;
    }
    public String getTownName() { return name; }

    public void addTBankUser(String playerName){
        tBankUsers.add(playerName);
    }
    public void removeTBankUser(String playerName){
        tBankUsers.remove(playerName);
    }
}
