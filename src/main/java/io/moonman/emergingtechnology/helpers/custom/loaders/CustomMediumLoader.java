package io.moonman.emergingtechnology.helpers.custom.loaders;

import java.io.IOException;

import io.moonman.emergingtechnology.EmergingTechnology;
import io.moonman.emergingtechnology.providers.classes.ModMedium;
import io.moonman.emergingtechnology.providers.ModMediumProvider;
import io.moonman.emergingtechnology.helpers.custom.system.JsonHelper;
import io.moonman.emergingtechnology.helpers.custom.wrappers.CustomMediumWrapper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Loads and validates the custom growth medium JSON file
 */
public class CustomMediumLoader {

    public static void preInit(FMLPreInitializationEvent e) {

        String path = e.getModConfigurationDirectory().getAbsolutePath() + "\\" + EmergingTechnology.MODID
                + "\\custom-media.json";
        loadCustomGrowthMedia(path);
    }

    public static void loadCustomGrowthMedia(String customGrowthMediaFilePath) {
        EmergingTechnology.logger.info("EmergingTechnology - Loading custom growth media...");
        try {
            ModMediumProvider.customMedia= readFromJson(customGrowthMediaFilePath);
            EmergingTechnology.logger.info("EmergingTechnology - Loaded "
                    + ModMediumProvider.customMedia.length + " custom growth media.");

        } catch (Exception ex) {
            EmergingTechnology.logger.warn("Warning! There was a problem loading custom growth media:");
            EmergingTechnology.logger.warn(ex);
            ModMediumProvider.customMedia = new ModMedium[] {};
        }
    }

    private static ModMedium[] readFromJson(String filePath) throws IOException {

        CustomMediumWrapper[] wrappers = JsonHelper.GSON_INSTANCE.fromJson(JsonHelper.readFromJson(filePath), CustomMediumWrapper[].class);

        return generateModMediaFromWrappers(wrappers);
    }

    private static ModMedium[] generateModMediaFromWrappers(CustomMediumWrapper[] wrappers) {
        ModMedium[] customGrowthMedia = new ModMedium[wrappers.length];

        for (int i = 0; i < wrappers.length; i++) {
            ModMedium medium = validateWrapper(wrappers[i]);
            customGrowthMedia[i] = medium;
        }

        return customGrowthMedia;
    }

    private static ModMedium validateWrapper(CustomMediumWrapper wrapper) {

        String name = wrapper.name;
        int growthModifier = checkBounds(wrapper.growthModifier);
        int waterUsage = checkBounds(wrapper.waterUsage);

        int boostModifier = wrapper.boostModifier;
        String[] plants = wrapper.plants;

        return new ModMedium(0, name, waterUsage, growthModifier, plants, boostModifier);
    }

    private static int checkBounds(int value) {
        value = value > 100 ? 100 : value;
        value = value < 0 ? 0 : value;

        return value;
    }
}