package me.canhvjp.blindp.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.canhvjp.blindp.blindp;
import net.minecraft.command.arguments.ItemStackArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    @Shadow
    @Final
    private ServerStatHandler statHandler;

    public ServerPlayerEntityMixin(World world, BlockPos blockPos, GameProfile gameProfile) {
        super(world, blockPos, gameProfile);
    }

    private ItemStack itemStackFromString(String string, int count) throws CommandSyntaxException {
        return new ItemStackArgumentType().parse(new StringReader(string)).createStack(count, false);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo info) throws CommandSyntaxException {
        if (statHandler.getStat(Stats.CUSTOM.getOrCreateStat(Stats.PLAY_ONE_MINUTE)) == 0) {

            blindp.log(Level.INFO, "New player detected, activating blindp.");

            //Item strings copy & pasted directly from jojoe's datapack
            ItemStack chestplate = itemStackFromString("minecraft:golden_chestplate", 1);
            ItemStack loot = itemStackFromString("minecraft:chest{BlockEntityTag:{Items:[{Slot:0,id:ender_pearl,Count:16},{Slot:1,id:flint_and_steel,Count:1},{Slot:2,id:lava_bucket,Count:1},{Slot:3,id:acacia_boat,Count:1},{Slot:4,id:iron_pickaxe,Count:1},{Slot:5,id:golden_carrot,Count:64},{Slot:6,id:obsidian,Count:64},{Slot:7,id:oak_log,Count:64},{Slot:8,id:splash_potion,Count:1,tag:{Potion:\"minecraft:fire_resistance\"}},{Slot:9,id:splash_potion,Count:1,tag:{Potion:\"minecraft:fire_resistance\"}},{Slot:10,id:golden_pickaxe,Count:1},{Slot:11,id:blaze_rod,Count:7},{Slot:12,id:iron_axe,Count:1},{Slot:13,id:white_wool,Count:64},{Slot:14,id:white_wool,Count:64},{Slot:15,id:ender_pearl,Count:16}]}}", 1);

            inventory.armor.set(2, chestplate);
            inventory.main.set(0, loot);
        }
    }
}
