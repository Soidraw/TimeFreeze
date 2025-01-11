package com.soidraw;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class login {
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;

            MinecraftServer server = player.getServer();
            if (server != null && server.getPlayerList().getPlayers().size() == 1) {
                synchronized (server) {
                    TimeFreeze.LOGGER.info("The first player logged in: {}", player.getName());
                    World world = server.getWorld(0);
                    long worldTime = world.getWorldTime();
                    TimeFreeze.LOGGER.info("World time when unfrozen: {} ticks", worldTime);
                    ServerCommandManager commandManager = server.createCommandManager();
                    commandManager.executeCommand(server, "gamerule doDaylightCycle true");
                    commandManager.executeCommand(server, "gamerule doWeatherCycle true");
//                    server.notifyAll();
                }
            }
        }
    }
}
