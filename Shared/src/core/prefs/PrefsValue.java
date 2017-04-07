package core.prefs;

public class PrefsValue {

    public enum Type {INT, FLOAT, STRING, BOOLEAN}

    public String value;
    public Type type;

    public PrefsValue(String value, Type type) {
        this.value = value;
        this.type = type;
    }
}
