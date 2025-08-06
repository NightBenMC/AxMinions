package com.example.superminion

import com.artillexstudios.axminions.api.AxMinionsAPI
import com.artillexstudios.axminions.api.minions.Minion
import com.artillexstudios.axminions.api.minions.miniontype.MinionType
import com.artillexstudios.axminions.api.minions.misc.Animation
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.roundToInt

class SuperMinion(plugin: JavaPlugin) : MinionType("super-minion", plugin.getResource("super-minion.yml")!!) {

    override fun shouldRun(minion: Minion): Boolean {
        // This will run the minion's action every `nextAction` ticks.
        return AxMinionsAPI.INSTANCE.tick % minion.nextAction == 0L
    }

    override fun onToolDirty(minion: Minion) {
        // This is called when the minion's tool is updated.
        // We update the minion's stats based on its level and tool enchantments.
        minion.range = getDouble("levels.${minion.level}.range")
        val baseSpeed = getLong("levels.${minion.level}.speed")

        // Calculate a speed multiplier based on the Efficiency enchantment.
        val efficiencyLevel = minion.tool?.getEnchantmentLevel(Enchantment.DIG_SPEED) ?: 0
        val toolBonus = (efficiencyLevel / 10.0).coerceAtMost(0.9)
        val speedMultiplier = 1.0 - toolBonus

        minion.nextAction = (baseSpeed * speedMultiplier).roundToInt().toLong().coerceAtLeast(1)
    }

    override fun run(minion: Minion) {
        val searchRadius = minion.range.toInt()
        val location = minion.location
        val world = location.world ?: return

        // Search for a mineable block in a cube around the minion
        for (x in -searchRadius..searchRadius) {
            for (y in -searchRadius..searchRadius) {
                for (z in -searchRadius..searchRadius) {
                    // To prevent the minion from digging straight down and falling
                    if (y < 0 && x == 0 && z == 0) continue

                    val blockLocation = location.clone().add(x.toDouble(), y.toDouble(), z.toDouble())
                    val block = world.getBlockAt(blockLocation)

                    if (isMineable(block.type)) {
                        // Check if the minion has space for the drops
                        val drops = block.getDrops(minion.tool)
                        if (minion.addItems(drops)) {
                            // If items are added successfully, break the block
                            block.type = Material.AIR
                            minion.setAnimation(Animation.SWING_ARM)
                            return // Exit after mining one block
                        } else {
                            // Inventory is full
                            minion.setAnimation(Animation.SHAKE_HEAD)
                            return
                        }
                    }
                }
            }
        }

        // No mineable blocks found in range
        minion.setAnimation(Animation.SHAKE_HEAD)
    }

    private fun isMineable(material: Material): Boolean {
        // A simple list of blocks the minion is allowed to break.
        return when (material) {
            Material.STONE, Material.COBBLESTONE, Material.DIRT, Material.GRAVEL,
            Material.COAL_ORE, Material.IRON_ORE, Material.GOLD_ORE, Material.DIAMOND_ORE,
            Material.EMERALD_ORE, Material.LAPIS_ORE, Material.REDSTONE_ORE,
            Material.NETHERRACK, Material.NETHER_QUARTZ_ORE, Material.SAND, Material.SANDSTONE -> true
            else -> false
        }
    }
}
