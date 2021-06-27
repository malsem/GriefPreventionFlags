package me.ryanhamshire.GPFlags.flags;

import me.ryanhamshire.GPFlags.*;
import me.ryanhamshire.GPFlags.util.Util;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Collections;
import java.util.List;

// Custom flag

public class FlagDef_JoinLocation extends FlagDefinition {


    public FlagDef_JoinLocation(FlagManager manager, GPFlags plugin) {
        super(manager, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Flag flag = this.getFlagInstanceAtLocation(player.getLocation(), player);
        if (flag == null) return;
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null);
        if(claim == null || claim.getPermission(player.getUniqueId().toString()) == ClaimPermission.Build || claim.getOwnerName().equals(player.getName())) return;
        String message = flag.parameters;
        String[] coord = message.split(";");
        player.teleport(new Location(player.getWorld(), Integer.parseInt(coord[0]) + 0.5D, Integer.parseInt(coord[1]), Integer.parseInt(coord[2]) + 0.5D));
        Util.sendClaimMessage(player, TextMode.Info, Messages.JoinTeleport);
    }

    @Override
    public String getName() {
        return "JoinLocation";
    }

    @Override
    public SetFlagResult validateParameters(String parameters) {
        if (parameters.isEmpty()) {
            return new SetFlagResult(false, new MessageSpecifier(Messages.MessageRequired));
        }

        return new SetFlagResult(true, this.getSetMessage(parameters));
    }

    @Override
    public MessageSpecifier getSetMessage(String parameters) {
        return new MessageSpecifier(Messages.AddedJoinLocation);
    }

    @Override
    public MessageSpecifier getUnSetMessage() {
        return new MessageSpecifier(Messages.RemovedJoinLocation);
    }

    @Override
    public List<FlagType> getFlagType() {
        return Collections.singletonList(FlagType.CLAIM);
    }

}
