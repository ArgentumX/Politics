package com.comissar.politics.command;

import com.comissar.politics.Politics;
import com.comissar.politics.PoliticsUniverse;
import com.comissar.politics.utils.TownyUtils;
import com.comissar.politics.utils.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownyBankCommand implements CommandExecutor {

    private Politics plugin;
    public TownyBankCommand(Politics plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(args.length == 0){

                String townName = TownyUtils.getTownName(p.getName());
                if(townName != null){

                    if(PoliticsUniverse.isTBankUser(townName, p.getName()) || TownyUtils.isMayor(p.getName())){

                        PoliticsUniverse.townsPolitic.get(townName).tBankWasChanged = true;
                        p.openInventory(PoliticsUniverse.getTBank(townName));

                    }else {//answer
                        p.sendMessage(Translate.of("no_permissions"));
                    }

                }else { //answer
                    p.sendMessage(Translate.of("not_civilian"));
                }
                return true;

            }else if(args.length == 1){

                if(args[0].equals("list")){
                    String townName = TownyUtils.getTownName(p.getName());
                    if(townName != null) {

                        p.sendMessage(String.format(Translate.of("tbank_list"), PoliticsUniverse.getTBankUsers(townName)));

                    }else { //answer
                        p.sendMessage(Translate.of("not_civilian"));
                    }
                    return true;
                }

            }else if(args.length == 2){

                String townName = TownyUtils.getTownName(p.getName());
                if(townName != null) {

                    if (TownyUtils.isMayor(p.getName())) {

                        if (args[0].equals("add")) {

                            if(!PoliticsUniverse.isTBankUser(townName, args[1])) {

                                PoliticsUniverse.addTBankUser(townName, args[1]);
                                p.sendMessage(String.format(Translate.of("tbank_add"), args[1]));

                            }else {//answer
                                p.sendMessage(Translate.of("tbank_already_added"));
                            }


                        } else if (args[0].equals("remove")) {

                            PoliticsUniverse.removeTBankUser(townName, args[1]);
                            p.sendMessage(String.format(Translate.of("tbank_remove"), args[1]));

                        }else {
                            return false;
                        }

                    }else {//answer
                        p.sendMessage(Translate.of("no_permissions"));
                    }

                } else { //answer
                    p.sendMessage(Translate.of("not_civilian"));
                }
                return true;

            }

        }
        return false;
    }
}
