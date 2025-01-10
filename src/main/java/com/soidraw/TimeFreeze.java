package com.soidraw;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
	modid = TimeFreeze.MODID,
	name = TimeFreeze.NAME,
	version = TimeFreeze.VERSION,
	serverSideOnly = true,
	acceptableRemoteVersions = "*"
)
public class TimeFreeze {
	public static final String MODID = "timefreeze";
	public static final String NAME = "TimeFreeze mod";
	public static final String VERSION = "1.0";

	public static Logger logger;
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public MinecraftServer server;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		MinecraftForge.EVENT_BUS.register(new login());
		MinecraftForge.EVENT_BUS.register(new logout());
		LOGGER.info("TimeFreeze mod loaded");
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		server = event.getServer();
		if(server.getPlayerList().getPlayers().isEmpty()){
			World world = server.getWorld(0);
			long worldTime = world.getWorldTime();
			LOGGER.info("Server is empty and is freezing at " + worldTime + " ticks.");
			ServerCommandManager commandManager = server.createCommandManager();
			commandManager.executeCommand(server, "gamerule doDaylightCycle false");
			commandManager.executeCommand(server, "gamerule doWeatherCycle false");
		} else{
			server.getPlayerList().sendMessage(new TextComponentString("You shouldn't read this in client!"));
		}
	}
}
