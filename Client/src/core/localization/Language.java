package core.localization;

import core.logging.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public enum Language {

    English("en"),
    Dutch("nl"),
    French("fr"),
    German("de"),
    Polish("pl"),
    Russian("ru"),
    Italian("it"),
    Japanese("ja");

    String langFile;
    Map<String, String> languageValues;

    Language(String langFile) {
        this.langFile = langFile;
        loadLangFile(langFile);
    }

    public String getString(String key) {
        return languageValues.get(key);
    }

    public boolean hasKey(String key) {
        return languageValues.containsKey(key);
    }

    private void loadLangFile(String language) {
        try {
            languageValues = new HashMap<>();

            File file = new File("Client/res/lang/" + language + ".lang");

            if (!file.exists()) {
                Logger.warn("Language file " + language + ".lang not found");
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#"))
                    continue;

                if (!line.contains("=")) {
                    Logger.err("Language file " + language + ".lang has invalid line");
                    continue;
                }

                String[] keyValue = line.split("=");

                if (keyValue.length != 2) {
                    Logger.err("Failed to parse language file " + language + ".lang");
                    continue;
                }

                languageValues.put(keyValue[0], keyValue[1]);
            }

            reader.close();

            Logger.info("Finished loading language file " + language + ".lang");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
