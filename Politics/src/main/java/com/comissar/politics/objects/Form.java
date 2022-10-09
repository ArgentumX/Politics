package com.comissar.politics.objects;

import java.util.HashMap;

public class Form {
    public int id;
    public String name;

    public HashMap<Integer, Double> playerBoost;
    public HashMap<Integer, Double> townBoost;


    public Form(int id, String name, HashMap<Integer, Double> playerBoost, HashMap<Integer, Double> townBoost){
        this.id = id;
        this.name = name;
        this.playerBoost = playerBoost;
        this.townBoost = townBoost;
    }
    public boolean containsPlayerBoost(Integer ID){
        return playerBoost.containsKey(ID);
    }
    public boolean containsTownBoost(Integer ID){
        return townBoost.containsKey(ID);
    }
    public Double getPlayerBoost(Integer ID){
        return playerBoost.getOrDefault(ID, 0.0);
    }
    public Double getTownBoost(Integer ID){
        return townBoost.getOrDefault(ID, 0.0);
    }


}
