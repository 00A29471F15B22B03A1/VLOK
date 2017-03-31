package core.localization;

import core.logging.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Language {

//    English("en"),
//    Dutch("nl"),
//    French("fr"),
//    German("de"),
//    Spanish("es"),
//    Polish("pl"),
//    Russian("ru"),
//    Italian("it"),
//    Japanese("ja"),
//    Punjabi("pa"),
//    Romanian("ro"),
//    Afrikaans("af"),
//    Swahili("sw");

    private String languageName;
    private String languageCode;
    private Map<String, String> languageValues;

    Language(String langPath) {
        File langFile = new File(langPath);
        this.languageCode = langFile.getName().replace(".lang", "");
        this.languageName = new Locale(languageCode).getDisplayName();

        loadLangFile(langFile);
    }

    public String getString(String key) {
        return languageValues.get(key);
    }

    public boolean hasKey(String key) {
        return languageValues.containsKey(key);
    }

    public String getLanguageName() {
        return languageName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    private void loadLangFile(File file) {
        try {
            languageValues = new HashMap<>();

            if (!file.exists()) {
                Logger.warn("Language file " + file.getName() + " not found");
                return;
            }

            System.out.println(file.getName().toUpperCase());

            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#"))
                    continue;

                if (!line.contains("=")) {
                    Logger.err("Language file " + file.getName() + " has invalid line");
                    continue;
                }

                String[] keyValue = line.split("=");

                if (keyValue.length != 2) {
                    Logger.err("Failed to parse language file " + file.getName());
                    continue;
                }

                System.out.println(keyValue[1]);

                languageValues.put(keyValue[0], keyValue[1]);
            }

            reader.close();

            Logger.info("Finished loading language file " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return languageName;
    }
}
