package core.localization;

import core.logging.Console;
import core.prefs.Prefs;
import core.prefs.PrefsValue;

import java.util.ArrayList;
import java.util.List;

public class Localization {

    private static Language defaultLanguage = Language.English;

    private static Language currentLanguage = defaultLanguage;

    private static List<LanguageListener> languageListeners = new ArrayList<>();

    static {
        setLanguage(Prefs.SETTINGS.getString("lang"));
    }

    public static void setLanguage(String languageCode) {
        currentLanguage = Language.getLanguage(languageCode);

        Prefs.SETTINGS.updateValue("lang", languageCode, PrefsValue.Type.STRING);

        for (LanguageListener languageListener : languageListeners)
            languageListener.onLanguageChange(currentLanguage);
    }

    public static Language getCurrentLanguage() {
        return currentLanguage;
    }

    public static String get(String key) {
        if (currentLanguage.isKeyAbsent(key)) {
            if (defaultLanguage.isKeyAbsent(key)) {
                Console.err("No language entry found for " + key);
                return "LANG ERROR";
            }

            return defaultLanguage.getString(key);
        }

        return currentLanguage.getString(key);
    }

    public static void addLanguageListener(LanguageListener languageListener) {
        languageListeners.add(languageListener);
    }

}
