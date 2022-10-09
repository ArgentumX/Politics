package com.comissar.politics.utils;

import com.comissar.politics.PoliticsUniverse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GUIMaster {



    public static Inventory getTBuildsGUI(Integer[] buildings){

        Inventory toReturn = Bukkit.createInventory(null, 27, "TBuilds");
        toReturn.setItem(10, createItem(Material.GOLD_INGOT, PoliticsUniverse.names[0], "§bУровень: " + buildings[0]));
        toReturn.setItem(12, createItem(Material.FARMLAND, PoliticsUniverse.names[1], "§bУровень: " + buildings[1]));
        toReturn.setItem(14, createItem(Material.BRICKS, PoliticsUniverse.names[2], "§bУровень: " + buildings[2]));
        toReturn.setItem(16, createItem(Material.BOOK, PoliticsUniverse.names[3], "§bУровень: " + buildings[3]));

        return toReturn;
    }
    public static ItemStack createItem(Material material, String name, String loreLvl){
        ItemStack toReturn = new ItemStack(material);

        ItemMeta met = toReturn.getItemMeta();
        met.setDisplayName(name);
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(loreLvl);
        lore.add(" ");
        lore.add("§aНажмите чтобы улучшить");
        met.setLore(lore);
        toReturn.setItemMeta(met);

        return toReturn;
    }

    public static void removeItems(Inventory inventory, Material type, int amount) {
        //if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (type == is.getType()) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }
}
