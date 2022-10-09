package com.comissar.politics.events;

import com.comissar.politics.Politics;
import com.comissar.politics.PoliticsUniverse;
import com.comissar.politics.objects.Building;
import com.comissar.politics.objects.TownPolitic;
import com.comissar.politics.utils.TownyUtils;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.event.*;
import com.palmergames.bukkit.towny.event.time.NewHourEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.WorldCoord;
import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EventListener implements Listener {

    private Politics plugin;

    public EventListener(Politics plugin) {
        this.plugin = plugin;
    }

    /*@EventHandler//(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        if(p != null) {

        }
    }*/
    @EventHandler
    public void TownDeleteEvent(DeleteTownEvent e){
        PoliticsUniverse.removeTown(e.getTownName());
    }
    @EventHandler
    public void TownRenameEvent(RenameTownEvent e){
        PoliticsUniverse.renameTown(e.getOldName(), e.getTown().getName());

    }
    @EventHandler
    public void NewTownEvent(NewTownEvent e){
        PoliticsUniverse.addTown(e.getTown().getName());
    }

    @EventHandler
    public void UpkeepEvent(TownUpkeepCalculationEvent e){

        Double boost = PoliticsUniverse.getBoostTownForm(e.getTown().getName(), 0);
        if(boost != null){

            e.setUpkeep(e.getUpkeep() * boost);
        }
    }
    @EventHandler
    public void NewDayEvent(NewDayEvent e){


        for (String townName : PoliticsUniverse.townsPolitic.keySet()){


            //forms daily boost
            Double boost = PoliticsUniverse.getBoostTownForm(townName, 2);
            if(boost != null){
                TownyUniverse.getInstance().getTown(townName).getAccount().deposit(boost, "");
            }

            //buildings daily boost
            TownPolitic townPolitic = PoliticsUniverse.getTownPolitic(townName);

            for(int i = 0; i < townPolitic.buildings.length; i++){

                if(townPolitic.buildings[i] > 0) {

                    Building building = PoliticsUniverse.getBuilding(i + 1);
                    HashMap<Integer, Double> buildingBoosts = building.boosts.get(townPolitic.buildings[i]);


                    for (Integer buildingBoostID : buildingBoosts.keySet()) {

                        switch (buildingBoostID) {

                            case 0:
                                PoliticsUniverse.addItemsToTBank(townPolitic, building.itemsIncome.get(townPolitic.buildings[i]));
                                break;
                            case 1:
                                TownyUniverse.getInstance().getTown(townName).getAccount().deposit(buildingBoosts.get(buildingBoostID), "");
                                break;
                            case 2:
                                List<String> boostedPlayers = new ArrayList<>();
                                for(Resident resident : TownyUniverse.getInstance().getTown(townName).getResidents()){
                                    if(resident.isOnline()){
                                        resident.getPlayer().giveExpLevels(PoliticsUniverse.getBuilding(4).boosts.get(townPolitic.buildings[3]).get(2).intValue());
                                    }else {
                                        boostedPlayers.add(resident.getName());
                                    }
                                }
                                townPolitic.boostedPlayers = boostedPlayers;
                                PoliticsUniverse.SaveTownData(townPolitic.name, "boostedPlayers", townPolitic.boostedPlayers);

                                break;
                        }


                    }

                }

            }

        }



    }
    @EventHandler
    public void NewHourEvent(NewHourEvent e){
        PoliticsUniverse.saveTBanks();
    }

    @EventHandler
    public void PlayerEnterTown(PlayerEnterTownEvent e){
        if(TownyUniverse.getInstance().getResident(e.getPlayer().getName()).hasTown()){
            try {
                if(TownyUniverse.getInstance().getResident(e.getPlayer().getName()).getTown().getName() == e.getEnteredtown().getName()){
                    String townName = e.getEnteredtown().getName();
                    if(PoliticsUniverse.isFormedTown(townName)){
                        Double boost = PoliticsUniverse.getBoostPlayerForm(townName, 0);
                        if(boost != null){
                            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 0));
                        }
                    }
                }
            } catch (NotRegisteredException ex) {
                ex.printStackTrace();
            }
        }
    }

    @EventHandler
    public void PlayerLeaveTown(PlayerLeaveTownEvent e){
        if(TownyUniverse.getInstance().getResident(e.getPlayer().getName()).hasTown()){
            try {
                if(TownyUniverse.getInstance().getResident(e.getPlayer().getName()).getTown().getName() == e.getLefttown().getName()){
                    String townName = e.getLefttown().getName();
                    if(PoliticsUniverse.isFormedTown(townName)){
                        Double boost = PoliticsUniverse.getBoostPlayerForm(townName, 0);
                        if(boost != null){
                            e.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
                        }
                    }
                }
            } catch (NotRegisteredException ex) {
                ex.printStackTrace();
            }
        }
    }

    @EventHandler
    public void PlayerLeaveTown(TownBlockClaimCostCalculationEvent e){
        if(PoliticsUniverse.isFormedTown(e.getTown().getName())){
            Double boost = PoliticsUniverse.getBoostTownForm(e.getTown().getName(), 1);
            if(boost != null){
                e.setPrice(e.getPrice() * boost);
            }
        }
    }

    @EventHandler
    public void OnClickEvent(InventoryClickEvent e){

        if(e.getInventory() != null) {

            String inventoryName = e.getView().getTitle();

            if(inventoryName.equals("TBuilds")) {

                e.setCancelled(true);
                ItemStack item = e.getCurrentItem();

                if(item != null && item.getType() != Material.AIR) {

                    Player p = (Player) e.getWhoClicked();

                    String buildingName = item.getItemMeta().getDisplayName();
                    buildingName = buildingName.replace(" ", "_");
                    buildingName = buildingName.replace("§l", "");

                    p.performCommand("tbuilds upgrade " + buildingName);
                    p.closeInventory();

                }
            }
        }
    }
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){

        if(!TownyUniverse.getInstance().hasResident(e.getPlayer().getName())) return;

        String townName = TownyUtils.getTownName(e.getPlayer().getName());
        if (townName != null) {//задел на будущее

            boolean wasChanged = false;

            TownPolitic townPolitic = PoliticsUniverse.getTownPolitic(townName);
            if(townPolitic.boostedPlayers.contains(e.getPlayer().getName())){
                townPolitic.boostedPlayers.remove(e.getPlayer().getName());
                PoliticsUniverse.log.info("give " + PoliticsUniverse.getBuilding(4).boosts.get(townPolitic.buildings[3]).get(2).intValue());
                e.getPlayer().giveExpLevels(PoliticsUniverse.getBuilding(4).boosts.get(townPolitic.buildings[3]).get(2).intValue());
                wasChanged = true;
            }

            if(wasChanged){
                PoliticsUniverse.SaveTownData(townPolitic.name, "boostedPlayers", townPolitic.boostedPlayers);
            }
        }

    }

    @EventHandler
    public void FurnaceEvent(FurnaceBurnEvent e)
    {
        if(TownyAPI.getInstance().isWilderness(e.getBlock().getLocation()) || TownyAPI.getInstance().getTownName(e.getBlock().getLocation()).equals("Lapland"))
        {
            return;
        }
        new BukkitRunnable() {
            private short speedUp = 20;
            @Override
            public void run() {
                Furnace furnace = (Furnace) e.getBlock().getState();
                furnace.setBurnTime((short) (furnace.getBurnTime() - speedUp + speedUp / 10));
                if (furnace.getInventory().getSmelting() != null && furnace.getInventory().getSmelting().getType() != Material.AIR && (furnace.getCookTime() > 0 || furnace.getBurnTime() > 0)) {
                    furnace.setCookTime((short) (furnace.getCookTime() + speedUp));
                    furnace.update();
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);

    }

    @EventHandler
    public void PlantGrowthEvent(BlockGrowEvent e){

        if(TownyAPI.getInstance().isWilderness(e.getBlock().getLocation()) || TownyAPI.getInstance().getTownName(e.getBlock().getLocation()).equals("Lapland"))
        {
            return;
        }

        Block block = e.getBlock();
        BlockData data = block.getBlockData();
        if (data instanceof Ageable) {

            Ageable ag = (Ageable) data;
            if(ag.getAge() < 6) {
                ag.setAge(ag.getAge() + 2);
                block.setBlockData(ag);
                e.setCancelled(true);
            }
        }

    }



}
