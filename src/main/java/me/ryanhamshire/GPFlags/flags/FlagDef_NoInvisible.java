package me.ryanhamshire.GPFlags.flags;

import me.ryanhamshire.GPFlags.*;
import me.ryanhamshire.GPFlags.event.PlayerClaimBorderEvent;
import me.ryanhamshire.GPFlags.util.Util;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

// Custom flag

public class FlagDef_NoInvisible extends FlagDefinition {


    public FlagDef_NoInvisible(FlagManager manager, GPFlags plugin) {
        super(manager, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Flag flag = this.getFlagInstanceAtLocation(player.getLocation(), player);
        if (flag == null) return;
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null);
        if(claim == null || claim.getPermission(player.getUniqueId().toString()) == ClaimPermission.Build || claim.getOwnerName().equals(player.getName())) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if(player.isOnline()){
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }, 1L);
        /*Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if(player.isOnline()){
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }, 60L);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if(player.isOnline()){
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }, 100L);*/

    }

    @EventHandler
    public void onPotion(EntityPotionEffectEvent e){
        if(e.getModifiedType().equals(PotionEffectType.INVISIBILITY) && (e.getAction() == EntityPotionEffectEvent.Action.ADDED || e.getAction() == EntityPotionEffectEvent.Action.CHANGED) && e.getEntity() instanceof Player){
            Player player = (Player) e.getEntity();
            Flag flag = this.getFlagInstanceAtLocation(player.getLocation(), player);
            if (flag == null) return;
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null);
            if(claim == null || claim.getPermission(player.getUniqueId().toString()) == ClaimPermission.Build || claim.getOwnerName().equals(player.getName())) return;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Util.sendClaimMessage(player, TextMode.Info, Messages.NoInvisible);
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }, 1L);
        }
    }

    @EventHandler
    public void onBorder(PlayerClaimBorderEvent e){
        Player player = e.getPlayer();
        if(player.hasPotionEffect(PotionEffectType.INVISIBILITY)){
            Claim claim = e.getClaimTo();
            if(claim == null) return;
            Flag flag = this.getClaimFlagFromClaim(claim);
            if(flag == null || claim.getPermission(player.getUniqueId().toString()) == ClaimPermission.Build || claim.getOwnerName().equals(player.getName())) return;
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            Util.sendClaimMessage(player, TextMode.Info, Messages.NoInvisible);
        }
    }

    @Override
    public String getName() {
        return "NoInvisible";
    }

    @Override
    public MessageSpecifier getSetMessage(String parameters) {
        return new MessageSpecifier(Messages.EnabledNoInvisible);
    }

    @Override
    public MessageSpecifier getUnSetMessage() {
        return new MessageSpecifier(Messages.DisabledNoInvisible);
    }

    @Override
    public List<FlagType> getFlagType() {
        return Collections.singletonList(FlagType.CLAIM);
    }

}
