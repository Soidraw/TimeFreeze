package com.soidraw;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class logout {
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;

            MinecraftServer server = player.getServer();
            if (server != null) {
                scheduler.schedule(() -> {
                    if (server.getPlayerList().getPlayers().isEmpty()) {
                        TimeFreeze.LOGGER.info("Player logged out: {}", player.getName());
                        freezeServer(server);
                    }
                }, 10, TimeUnit.SECONDS);
            }
        }
    }

    private void freezeServer(MinecraftServer server) {
        synchronized (server) {
            World world = server.getWorld(0);
            long worldTime = world.getWorldTime();
            TimeFreeze.LOGGER.info("World time when frozen: {} ticks", worldTime);
            ServerCommandManager commandManager = server.createCommandManager();
            server.getPlayerList().sendMessage(new TextComponentString("You shouldn't read this in client!"));
            commandManager.executeCommand(server, "gamerule doDaylightCycle false");
            commandManager.executeCommand(server, "gamerule doWeatherCycle false");
//          server.wait();
        }
    }
}
