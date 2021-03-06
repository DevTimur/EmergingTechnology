package io.moonman.emergingtechnology.config.polymers;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;

public class PolymersModuleShredder {

    @Name("Shredder Operation Energy Cost")
    @Config.Comment("How much energy the Shredder uses when shredding per cycle (~10 ticks).")
    @RangeInt(min = 0, max = 100)
    public int shredderEnergyBaseUsage = 10;

    @Name("Shredder Operation Time")
    @Config.Comment("How long the Shredder takes to shred items.")
    @RangeInt(min = 0, max = 100)
    public int shredderBaseTimeTaken = 10;
}