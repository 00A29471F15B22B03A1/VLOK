package core.localization;

import core.logging.Console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public enum Language {

    English("en"),
    Dutch("nl"),
    French("fr"),
    German("de"),
    Spanish("es"),
    Polish("pl"),
    Russian("ru"),
    Italian("it"),
    Japanese("ja"),
    Punjabi("pa"),
    Romanian("ro"),
    Afrikaans("af"),
    Swahili("sw");

    private Map<String, String> languageValues;

    public final String languageCode;

    Language(String langCode) {
        this.languageCode = langCode;
        loadLangFile("/lang/" + langCode + ".lang");
    }

    public String getString(String key) {
        return languageValues.get(key);
    }

    public boolean isKeyAbsent(String key) {
        return !languageValues.containsKey(key);
    }

    private void loadLangFile(String path) {
        try {
            languageValues = new HashMap<>();

            BufferedReader reader = new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream(path)));

            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#"))
                    continue;

                if (!line.contains("=")) {
                    Console.err("Language file " + path + " has invalid line");
                    continue;
                }

                String[] keyValue = line.split("=");

                if (keyValue.length != 2) {
                    Console.err("Failed to parse language file " + path);
                    continue;
                }

                languageValues.put(keyValue[0], keyValue[1]);
            }

            reader.close();

            Console.info("Finished loading language file " + path);
        } catch (IOException e) {
            e.printStackTrace();
            Console.err("Language file " + path + " not found");
        }
    }

    public static Language getLanguage(String code) {
        for (Language l : Language.values())
            if (l.languageCode.equals(code))
                return l;
        return null;
    }
}
