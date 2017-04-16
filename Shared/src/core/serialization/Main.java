package core.serialization;

public class Main {

    static void printBytes(byte[] data) {
        for (byte aData : data) {
            System.out.printf("0x%x ", aData);
        }
        System.out.println();
    }

    public static void main(String[] args) {

    }

    private static void dump(VLOKDatabase db) {
        System.out.println("----------------------------------");
        System.out.println("           VLOKDatabase           ");
        System.out.println("----------------------------------");
        System.out.println("Name: " + db.getName());
        System.out.println("Size: " + db.getSize());
        System.out.println("Object count: " + db.objects.size());
        System.out.println();
        for (VLOKObject object : db.objects) {
            System.out.println("\tObject:");
            System.out.println("\tName: " + object.getName());
            System.out.println("\tSize: " + object.getSize());
            System.out.println("\tField Count: " + object.fields.size());
            System.out.println();
            for (VLOKField field : object.fields) {
                System.out.println("\t\tField:");
                System.out.println("\t\tName: " + field.getName());
                System.out.println("\t\tSize: " + field.getSize());
                System.out.println();
            }
            for (VLOKString field : object.strings) {
                System.out.println("\t\tString:");
                System.out.println("\t\tName: " + field.getName());
                System.out.println("\t\tSize: " + field.getSize());
                System.out.println("\t\tValue: " + field.getString());
                System.out.println();
            }
            System.out.println();
        }
        System.out.println("----------------------------------");
    }

}
