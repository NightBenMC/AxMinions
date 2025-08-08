package com.example.axminionsaddon

import com.artillexstudios.axminions.api.AxMinionsAPI
import com.artillexstudios.axminions.api.minions.miniontype.MinionTypes
import com.example.axminionsaddon.minions.SuperMinion
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class AxMinionsAddon : JavaPlugin() {

    override fun onEnable() {
        // Save the default config.yml if it doesn't exist
        saveDefaultConfig()
        // Reload the config to ensure we have the latest user-defined values
        reloadConfig()

        logger.info("AxMinionsAddon is enabling...")

        // Register custom minions based on the config
        registerMinions()
    }

    private fun registerMinions() {
        // Register SuperMinion
        if (config.getBoolean("minions.super-minion.enabled", false)) {
            registerMinion("super-minion") { SuperMinion() }
        }

        // Add more minions here in the future following the same pattern
        // if (config.getBoolean("minions.another-minion.enabled", false)) {
        //     registerMinion("another-minion") { AnotherMinion() }
        // }
    }

    private fun registerMinion(minionId: String, minionSupplier: () -> com.artillexstudios.axminions.api.minions.miniontype.MinionType) {
        val minionConfigFile = File(AxMinionsAPI.INSTANCE.plugin.dataFolder, "minions/$minionId.yml")

        if (!minionConfigFile.exists()) {
            logger.warning("Cannot register '$minionId' because its config file is missing!")
            logger.warning("Please create the file at: ${minionConfigFile.absolutePath}")
            return
        }

        try {
            val minionType = minionSupplier()
            MinionTypes.register(minionType)

            // Retroactively load for existing worlds, as per AxMinions documentation
            for (world in Bukkit.getWorlds()) {
                AxMinionsAPI.INSTANCE.dataHandler.loadMinionsForWorld(minionType, world)
            }
            logger.info("Successfully registered custom minion: $minionId")
        } catch (e: Exception) {
            logger.severe("Failed to register custom minion: $minionId. Error: ${e.message}")
            e.printStackTrace()
        }
    }

    override fun onDisable() {
        logger.info("AxMinionsAddon has been disabled.")
    }
}
