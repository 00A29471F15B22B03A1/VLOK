package core.prefs;

import core.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Prefs {

    public static Prefs SETTINGS = new Prefs("VLOKData/main.prefs");

    private static final char SPLIT_CHAR = ':';

    private String prefsPath;

    private Map<String, PrefsValue> loadedValues = new HashMap<>();

    public Prefs(String path) {
        this.prefsPath = path;
        loadValues();
    }

    private void loadValues() {
        loadedValues.clear();

        File f = new File(prefsPath);
        if (!f.exists()) {
            f.getParentFile().mkdir();
            Utils.writeToFile(prefsPath, Utils.readFileWithIS("/default.prefs"));
        }

        String file = Utils.readFile(prefsPath);
        assert file != null;
        for (String line : file.split("\n")) {

            if (line.isEmpty())
                continue;

            String[] splitLine = line.split(SPLIT_CHAR + "");

            String name = splitLine[0];
            PrefsValue.Type type = PrefsValue.Type.valueOf(splitLine[1]);
            String value = "";
            if (splitLine.length >= 3)
                value = splitLine[2];

            PrefsValue finalValue = new PrefsValue(value, type);

            loadedValues.put(name, finalValue);
        }
    }

    private void storeValues() {
        String file = "";

        for (Map.Entry<String, PrefsValue> entry : loadedValues.entrySet())
            file += entry.getKey() + SPLIT_CHAR + entry.getValue().type.toString() + SPLIT_CHAR + entry.getValue().value + "\n";

        Utils.writeToFile(prefsPath, file);
    }

    public void addValue(String name, String value, PrefsValue.Type type) {
        PrefsValue prefsValue = new PrefsValue(value, type);

        if (loadedValues.containsKey(name))
            loadedValues.remove(name);

        loadedValues.put(name, prefsValue);

        storeValues();
    }

    public void updateValue(String name, String value, PrefsValue.Type type) {
        PrefsValue prefsValue = new PrefsValue(value, type);

        if (!verifyValue(prefsValue)) {         //Verify if value is correct
            System.err.println("Value " + value + " does not correspond with type " + type + " in prefs value " + name);
            return;
        }

        addValue(name, value, type);
    }

    public int getInt(String name) {
        PrefsValue prefsValue = loadedValues.get(name);

        if (prefsValue.type != PrefsValue.Type.INT) {
            System.err.println("Value " + name + " is not an integer");
            return -1;
        }

        return Integer.parseInt(prefsValue.value);
    }

    public float getFloat(String name) {
        PrefsValue prefsValue = loadedValues.get(name);

        if (prefsValue.type != PrefsValue.Type.FLOAT) {
            System.err.println("Value " + name + " is not an float");
            return -1;
        }

        return Float.parseFloat(prefsValue.value);
    }

    public boolean getBoolean(String name) {
        PrefsValue prefsValue = loadedValues.get(name);

        if (prefsValue.type != PrefsValue.Type.BOOLEAN) {
            System.err.println("Value " + name + " is not an boolean");
            return false;
        }

        return Boolean.parseBoolean(prefsValue.value);
    }

    public String getString(String name) {
        PrefsValue prefsValue = loadedValues.get(name);

        if (prefsValue == null)
            return "";

        if (prefsValue.type != PrefsValue.Type.STRING) {
            System.err.println("Value " + name + " is not an string");
            return "";
        }

        return prefsValue.value;
    }

    public boolean doesValueExist(String name) {
        return loadedValues.containsKey(name);
    }

    private boolean verifyValue(PrefsValue value) {
        switch (value.type) {
            case INT:
                if (Utils.isInteger(value.value))
                    return true;
                break;

            case FLOAT:
                if (Utils.isFloat(value.value))
                    return true;
                break;

            case BOOLEAN:
                if (Utils.isBoolean(value.value))
                    return true;
                break;
            case STRING:
                return true;
        }
        return false;
    }

}