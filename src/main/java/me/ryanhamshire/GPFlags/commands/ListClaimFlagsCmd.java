package me.ryanhamshire.GPFlags.commands;

import me.ryanhamshire.GPFlags.Flag;
import me.ryanhamshire.GPFlags.GPFlags;
import me.ryanhamshire.GPFlags.Messages;
import me.ryanhamshire.GPFlags.TextMode;
import me.ryanhamshire.GPFlags.flags.FlagDefinition;
import me.ryanhamshire.GPFlags.util.Util;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;

public class ListClaimFlagsCmd extends BaseCmd {

    public final HashMap<FlagDefinition, String> name = new HashMap<>();

    ListClaimFlagsCmd(GPFlags plugin) {
        super(plugin);
        command = "ListClaimFlags";
        usage = "";
        requirePlayer = true;
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoEnter"), "NoEnter");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoFluidFlow"), "NoFluidFlow");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoItemDrop"), "NoItemDrop");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoItemPickup"), "NoItemPickup");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoLeafDecay"), "NoLeafDecay");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoMobSpawns"), "NoMobSpawns");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoEnderPearl"), "NoEnderPearl");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("OwnerMemberFly"), "OwnerMemberFly");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoMonsterSpawns"), "NoMonsterSpawns");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("RaidMemberOnly"), "RaidMemberOnly");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoOpenDoors"), "NoOpenDoors");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("ProtectNamedMobs"), "ProtectNamedMobs");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoGrowth"), "NoGrowth");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoSnowForm"), "NoSnowForm");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("JoinLocation"), "JoinLocation");
    }

    @Override
    boolean execute(CommandSender sender, String[] args) {
        Player player = ((Player) sender);
        PlayerData playerData = GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId());

        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), false, playerData.lastClaim);

        Collection<Flag> flags;
        boolean flagsFound = false;
        StringBuilder builder1 = new StringBuilder();
        StringBuilder builder2 = new StringBuilder();
        if (claim != null) {
            flags = PLUGIN.getFlagManager().getFlags(claim.getID().toString());
            for (Flag flag : flags) {
                flagsFound = true;
                builder1.append((flag.getSet() ? "+" : "-") + name.getOrDefault(flag.getFlagDefinition(), flag.getFlagDefinition().getName())).append(" ");
            }

            if (claim.parent != null) {
                flags = PLUGIN.getFlagManager().getFlags(claim.parent.getID().toString());
                for (Flag flag : flags) {
                    flagsFound = true;
                    builder2.append((flag.getSet() ? "+" : "-") + name.getOrDefault(flag.getFlagDefinition(), flag.getFlagDefinition().getName())).append(" ");
                }
            }
        }

        if (builder1.length() > 0)
            Util.sendMessage(player, TextMode.Info, Messages.FlagsClaim, builder1.toString());
        if (builder2.length() > 0)
            Util.sendMessage(player, TextMode.Info, Messages.FlagsParent, builder2.toString());

        if (!flagsFound) {
            Util.sendMessage(player, TextMode.Info, Messages.NoFlagsHere);
        }

        return true;
    }

}
