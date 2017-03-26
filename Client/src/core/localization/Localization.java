package core.localization;

import core.logging.Logger;

public class Localization {

    private static Language defaultLanguage = Language.English;

    private static Language currentLanguage = defaultLanguage;

    public static void setLanguage(Language language) {
        currentLanguage = language;
    }

    public static String getString(String key) {
        if (!currentLanguage.hasKey(key)) {
            if (!defaultLanguage.hasKey(key)) {
                Logger.err("No language entry found for " + key);
                return "LANG ERROR";
            }

            return defaultLanguage.getString(key);
        }

        return currentLanguage.getString(key);
    }

}
