package core;

public final class PermissionLevels {

    public static int ADMIN = 5;
    public static int TRUSTED = 4;
    public static int STUDENT = 3;
    public static int PEASAN = 2;
    public static int QUEUED = 1;
    public static int BLOCKED = 0;

    public static boolean canRead(int permissionLevel) {
        return permissionLevel >= 2;
    }

    public static boolean canWrite(int permissionLevel) {
        return permissionLevel >= 3;
    }

    public static boolean needsCheck(int permissionLevel) {
        return permissionLevel < 4;
    }

    public static boolean isAdmin(int permissionLevel) {
        return permissionLevel >= ADMIN;
    }

}
