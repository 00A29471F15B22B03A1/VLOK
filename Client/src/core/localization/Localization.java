package core.localization;

import core.logging.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Localization {

    private static Language defaultLanguage = null;

    private static Language currentLanguage = defaultLanguage;

    private static List<Language> loadedLanguages = new ArrayList<>();

    private static List<LanguageListener> languageListeners = new ArrayList<>();

    static {
        File languageFolder = new File("Client/res/lang/");
        for (File f : languageFolder.listFiles()) {
            if (!f.getName().endsWith(".lang"))
                continue;

            loadedLanguages.add(new Language(f.getPath()));
        }

        defaultLanguage = getLanguage("en");
        currentLanguage = defaultLanguage;
    }

    public static void setLanguage(Language language) {
        currentLanguage = language;

        for (LanguageListener languageListener : languageListeners)
            languageListener.onLanguageChange(currentLanguage);
    }

    public static Language getLanguage(String languageCode) {
        for (Language language : loadedLanguages) {
            if (language.getLanguageCode().equals(languageCode))
                return language;
        }
        return null;
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

    public static Language[] getLoadedLanguages() {
        return loadedLanguages.toArray(new Language[loadedLanguages.size()]);
    }

    public static void addLanguageListener(LanguageListener languageListener) {
        languageListeners.add(languageListener);
    }

}
