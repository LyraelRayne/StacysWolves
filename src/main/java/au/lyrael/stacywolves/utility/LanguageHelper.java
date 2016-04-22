package au.lyrael.stacywolves.utility;


import com.google.common.collect.ImmutableMap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SideOnly(Side.CLIENT)
public class LanguageHelper {
    public static Map<String, String> globals = new HashMap<String, String>();
    private static Map<String, String> preprocesssed = new HashMap<String, String>();

    @SideOnly(Side.CLIENT)
    public static void setupColors() {
        /* Unicode colors that you can use in the tooltips/names lang files.
         * Use by calling {{!name}}, with name being the name being colors.color. */
        LanguageHelper.globals.put("colors.black", "\u00A70");
        LanguageHelper.globals.put("colors.navy", "\u00A71");
        LanguageHelper.globals.put("colors.green", "\u00A72");
        LanguageHelper.globals.put("colors.blue", "\u00A73");
        LanguageHelper.globals.put("colors.red", "\u00A74");
        LanguageHelper.globals.put("colors.purple", "\u00A75");
        LanguageHelper.globals.put("colors.gold", "\u00A76");
        LanguageHelper.globals.put("colors.light_gray", "\u00A77");
        LanguageHelper.globals.put("colors.gray", "\u00A78");
        LanguageHelper.globals.put("colors.dark_purple", "\u00A79");
        LanguageHelper.globals.put("colors.light_green", "\u00A7a");
        LanguageHelper.globals.put("colors.light_blue", "\u00A7b");
        LanguageHelper.globals.put("colors.rose", "\u00A7c");
        LanguageHelper.globals.put("colors.light_purple", "\u00A7d");
        LanguageHelper.globals.put("colors.yellow", "\u00A7e");
        LanguageHelper.globals.put("colors.white", "\u00A7f");
        LanguageHelper.globals.put("colors.reset", EnumChatFormatting.RESET.toString());
    }

    /**
     * Gets the preprocessed version of the localized string. Preprocessing will only be ran once, not on every call.
     *
     * @param key The localization key.
     * @return A preprocessed localized string. If your current language dosen't have a localized string, it defaults to en_US.
     */
    public static String getLocalization(String key) {
        key = key.replace(":", ".");
        String localization = getLocalization(key, true);

        if (preprocesssed.containsKey(key)) {
            return preprocesssed.get(key);
        } else if (localization.contains("{{!")) {
            while (localization.contains("{{!")) {
                int startingIndex = localization.indexOf("{{!");
                int endingIndex = localization.substring(startingIndex).indexOf("}}") + startingIndex;
                String fragment = localization.substring(startingIndex + 3, endingIndex);

                try {
                    String replacement = globals.get(fragment.toLowerCase());
                    localization = localization.substring(0, startingIndex) + replacement + localization.substring(endingIndex + 2);
                } catch (Exception e) {
                    localization = localization.substring(0, startingIndex) + localization.substring(endingIndex + 2);
                }
            }

            preprocesssed.put(key, localization);
        }
        return localization;
    }

    private static String getLocalization(String key, boolean fallback) {
        key = key.replace(":", ".");
        String localization = StatCollector.translateToLocal(key);
        if (localization.equals(key) && fallback) {
            localization = StatCollector.translateToFallback(key);
        }
        return localization;
    }

    public static void formatTooltip(String langName, ImmutableMap<String, String> toFormat, ItemStack stack, List list) {
        String langTooltip = LanguageHelper.getLocalization(langName);
        if (langTooltip == null || langTooltip.equals(langName))
            return;
        if (toFormat != null) {
            for (Entry<String, String> toReplace : toFormat.entrySet()) {
                langTooltip = langTooltip.replace("{{" + toReplace.getKey() + "}}", toReplace.getValue());
            }
        }

        for (String descriptionLine : langTooltip.split(";")) {
            if (descriptionLine != null && descriptionLine.length() > 0)
                list.add(descriptionLine);
        }
    }

    public static String unwrapUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }
}
