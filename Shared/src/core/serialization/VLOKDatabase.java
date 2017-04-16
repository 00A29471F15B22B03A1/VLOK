package core.serialization;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static core.serialization.SerializationUtils.*;

public class VLOKDatabase extends VLOKBase {

    private static final byte[] HEADER = "VLOKDB".getBytes();
    private static final short VERSION = 0x0100;
    private static final byte CONTAINER_TYPE = ContainerType.DATABASE;
    private short objectCount;
    public List<VLOKObject> objects = new ArrayList<>();

    private VLOKDatabase() {
    }

    public VLOKDatabase(String name) {
        setName(name);

        size += HEADER.length + 2 + 1 + 2;
    }

    public void addObject(VLOKObject object) {
        objects.add(object);
        size += object.getSize();

        objectCount = (short) objects.size();
    }

    public int getSize() {
        return size;
    }

    public int getBytes(byte[] dest, int pointer) {
        pointer = writeBytes(dest, pointer, HEADER);
        pointer = writeBytes(dest, pointer, VERSION);
        pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
        pointer = writeBytes(dest, pointer, nameLength);
        pointer = writeBytes(dest, pointer, name);
        pointer = writeBytes(dest, pointer, size);

        pointer = writeBytes(dest, pointer, objectCount);
        for (VLOKObject object : objects)
            pointer = object.getBytes(dest, pointer);

        return pointer;
    }

    public static VLOKDatabase Deserialize(byte[] data) {
        int pointer = 0;
        assert (readString(data, pointer, HEADER.length).equals(HEADER));
        pointer += HEADER.length;

        if (readShort(data, pointer) != VERSION) {
            System.err.println("Invalid RCDB version!");
            return null;
        }
        pointer += 2;

        byte containerType = readByte(data, pointer++);
        assert (containerType == CONTAINER_TYPE);

        VLOKDatabase result = new VLOKDatabase();
        result.nameLength = readShort(data, pointer);
        pointer += 2;
        result.name = readString(data, pointer, result.nameLength).getBytes();
        pointer += result.nameLength;

        result.size = readInt(data, pointer);
        pointer += 4;

        result.objectCount = readShort(data, pointer);
        pointer += 2;

        for (int i = 0; i < result.objectCount; i++) {
            VLOKObject object = VLOKObject.Deserialize(data, pointer);
            result.objects.add(object);
            pointer += object.getSize();
        }

        return result;
    }

    public VLOKObject findObject(String name) {
        for (VLOKObject object : objects) {
            if (object.getName().equals(name))
                return object;
        }
        return null;
    }

    public static VLOKDatabase DeserializeFromFile(String path) {
        byte[] buffer = null;
        try {
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(path));
            buffer = new byte[stream.available()];
            stream.read(buffer);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Deserialize(buffer);
    }

    public void serializeToFile(String path) {
        byte[] data = new byte[getSize()];
        getBytes(data, 0);
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(path));
            stream.write(data);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
