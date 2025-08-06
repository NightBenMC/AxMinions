package com.example.superminion

import com.artillexstudios.axminions.api.AxMinionsAPI
import com.artillexstudios.axminions.api.minions.miniontype.MinionTypes
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class SuperMinionPlugin : JavaPlugin() {

    override fun onEnable() {
        // Create an instance of our custom minion type
        val superMinionType = SuperMinion(this)

        // Register the new minion type with AxMinions
        MinionTypes.register(superMinionType)

        // As per the documentation, we need to load the minions for worlds that are already loaded.
        // This ensures that if the server is reloaded, our custom minions will still work.
        for (world in Bukkit.getWorlds()) {
            AxMinionsAPI.INSTANCE.dataHandler.loadMinionsForWorld(superMinionType, world)
        }

        logger.info("SuperMinion plugin has been enabled!")
    }

    override fun onDisable() {
        logger.info("SuperMinion plugin has been disabled.")
    }
}
