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
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoEnter"), "禁止非成員玩家進入領地");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoFluidFlow"), "防止液體流動");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoItemDrop"), "防止丟棄物品");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoItemPickup"), "防止撿起物品");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoLeafDecay"), "防止葉子自然消失");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoMobSpawns"), "防止生物生成");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoEnderPearl"), "防止玩家使用終界珍珠傳送");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("OwnerMemberFly"), "領地成員飛行");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoMonsterSpawns"), "防止怪物生成");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("RaidMemberOnly"), "突襲只能由領地成員觸發");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoOpenDoors"), "防止玩家開關木門、地板門、柵欄門");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("ProtectNamedMobs"), "保護已命名生物");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoGrowth"), "防止作物生長");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("NoSnowForm"), "防止積雪產生");
        name.put(plugin.getFlagManager().getFlagDefinitionByName("JoinLocation"), "領地初始位置");
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
