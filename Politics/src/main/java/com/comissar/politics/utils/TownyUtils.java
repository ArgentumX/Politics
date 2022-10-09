package com.comissar.politics.utils;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;

public class TownyUtils {

    public static boolean isMayor(String name){
        return TownyUniverse.getInstance().getResident(name).isMayor();
    }

    public static String getTownName(String playerName){
        if(!TownyUniverse.getInstance().getResident(playerName).hasTown()){
            return null;
        }

        try {
            return TownyUniverse.getInstance().getResident(playerName).getTown().getName();
        } catch (NotRegisteredException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Town getTownByResident(String name){
        try {
            return TownyUniverse.getInstance().getResident(name).getTown();
        } catch (NotRegisteredException e) {
            e.printStackTrace();
        }
        return null;
    }
}
