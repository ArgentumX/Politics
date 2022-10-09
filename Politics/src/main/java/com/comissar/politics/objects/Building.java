package com.comissar.politics.objects;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Building {

    //main
    public Integer id;
    public Integer maxLevel;
    public String name;
    public List<ItemStack[]> itemsCost;
    public List<HashMap<Integer, Double>> boosts;
    public List<ItemStack[]> itemsIncome;

    public Building (int id, int maxLevel, String name, List<ItemStack[]> itemsCost, List<HashMap<Integer, Double>> boosts, List<ItemStack[]> itemsIncome){
        this.maxLevel = maxLevel;
        this.id = id;
        this.name = name;
        this.itemsCost = itemsCost;
        this.boosts = boosts;
        this.itemsIncome = itemsIncome;
    }

}
