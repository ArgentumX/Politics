package com.comissar.politics.command;

import com.comissar.politics.Politics;
import com.comissar.politics.PoliticsUniverse;
import com.comissar.politics.utils.TownyUtils;
import com.comissar.politics.utils.Translate;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownyFormCommand implements CommandExecutor {

    private Politics plugin;
    public TownyFormCommand(Politics plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(args.length == 2)
            {
                if(args[0].equals("set")){
                    if(TownyUtils.isMayor(p.getName())){

                        Town town = TownyUtils.getTownByResident(p.getName());

                        if(PoliticsUniverse.isFormedTown(town.getName()))
                        {

                            if(town.getAccount().canPayFromHoldings(PoliticsUniverse.changeFormCost)){

                                Integer newForm = PoliticsUniverse.getFormID(args[1]);
                                if(newForm != -1){
                                    if(PoliticsUniverse.getPolitic(town.getName()) != newForm){
                                        town.getAccount().withdraw(PoliticsUniverse.changeFormCost, "");
                                        PoliticsUniverse.setFormTown(town.getName(), newForm);
                                        p.sendMessage(String.format(Translate.of("set_form"), args[1]));
                                    }
                                }else {//answer
                                    p.sendMessage(Translate.of("unknown_form"));
                                }


                            }else{//answer
                                p.sendMessage(String.format(Translate.of("no_money"), PoliticsUniverse.changeFormCost));
                            }

                        }
                        else
                        {

                            if(town.getAccount().canPayFromHoldings(PoliticsUniverse.setFormCost)){
                                Integer newForm = PoliticsUniverse.getFormID(args[1]);
                                if(newForm != -1){
                                    town.getAccount().withdraw(PoliticsUniverse.setFormCost, "");
                                    PoliticsUniverse.setFormTown(town.getName(), newForm);
                                    p.sendMessage(String.format(Translate.of("set_form"), args[1]));
                                }else {//answer
                                    p.sendMessage(Translate.of("unknown_form"));
                                }
                            }else{//answer
                                p.sendMessage(String.format(Translate.of("no_money"), PoliticsUniverse.setFormCost));
                            }
                        }

                    }
                    else {//answer
                        p.sendMessage(Translate.of("no_permissions"));
                    }
                    return true;
                }
            }
            else if(args.length == 1)
            {
                if(args[0].equals("info")){

                    String townName = TownyUtils.getTownName(p.getName());
                    if(townName != null){
                        if(PoliticsUniverse.isFormedTown(townName)) {
                            p.sendMessage(String.format(Translate.of("town_politics_info"), PoliticsUniverse.getFormNameOfTown(townName)));
                        }else { //answer
                            p.sendMessage(Translate.of("not_formed_town"));
                        }
                    }
                    else { //answer
                        p.sendMessage(Translate.of("not_civilian"));
                    }
                    return true;
                }
            }


        }
        return false;
    }
}
