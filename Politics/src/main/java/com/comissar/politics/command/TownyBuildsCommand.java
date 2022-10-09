package com.comissar.politics.command;

import com.comissar.politics.Politics;
import com.comissar.politics.PoliticsUniverse;
import com.comissar.politics.objects.TownPolitic;
import com.comissar.politics.utils.GUIMaster;
import com.comissar.politics.utils.TownyUtils;
import com.comissar.politics.utils.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownyBuildsCommand implements CommandExecutor {

    private Politics plugin;
    public TownyBuildsCommand(Politics plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(args.length == 0){

                String townName = TownyUtils.getTownName(p.getName());
                if (townName != null) {//задел на будущеe

                    if(TownyUtils.isMayor(p.getName())) {

                        p.openInventory(GUIMaster.getTBuildsGUI(PoliticsUniverse.getTownPolitic(townName).buildings));


                    }else {//answer
                        p.sendMessage(Translate.of("no_permissions"));
                    }

                }else { //answer
                    p.sendMessage(Translate.of("not_civilian"));
                }

                return true;
            }
            if(args.length == 2){

                if(args[0].equals("upgrade")){

                    if(TownyUtils.isMayor(p.getName())) {

                        TownPolitic townPolitic = PoliticsUniverse.getTownPolitic(TownyUtils.getTownName(p.getName()));

                        Integer id = PoliticsUniverse.getBuildingID(args[1].replace("_", " "));

                        if(id != null){

                            if(townPolitic.buildings[id-1] < PoliticsUniverse.Llv[id-1]){

                                if(PoliticsUniverse.tryGetResources(townPolitic, id)){

                                    townPolitic.buildings[id-1]++;
                                    PoliticsUniverse.SaveTownData(townPolitic.name, townPolitic.buildings);
                                    p.sendMessage(String.format(Translate.of("tbuilds_upgraded"), PoliticsUniverse.getBuildingName(id), townPolitic.buildings[id-1]));

                                }else {
                                    p.sendMessage(String.format(Translate.of("tbank_not_enought_resources"),PoliticsUniverse.getUpResourcesString(id, townPolitic.buildings[id-1]+1)));
                                }


                            }else {//answer
                                p.sendMessage(Translate.of("tbuilds_max_level"));
                            }

                        }else {//answer
                            p.sendMessage(Translate.of("tbuilds_unknown_building"));
                        }

                    }else {//answer
                        p.sendMessage(Translate.of("no_permissions"));
                    }

                    return true;

                }
            }

        }

        return false;
    }
}
