package core.serialization;

import java.util.ArrayList;
import java.util.List;

import static core.serialization.SerializationUtils.*;

public class VLOKObject extends VLOKBase {

    public static final byte CONTAINER_TYPE = ContainerType.OBJECT;
    private short fieldCount;
    public List<VLOKField> fields = new ArrayList<>();
    private short stringCount;
    public List<VLOKString> strings = new ArrayList<>();
    private short arrayCount;
    public List<VLOKArray> arrays = new ArrayList<>();

    private VLOKObject() {
    }

    public VLOKObject(String name) {
        size += 1 + 2 + 2 + 2;
        setName(name);
    }

    public void addField(VLOKField field) {
        fields.add(field);
        size += field.getSize();

        fieldCount = (short) fields.size();
    }

    public void addString(VLOKString string) {
        strings.add(string);
        size += string.getSize();

        stringCount = (short) strings.size();
    }

    public void addArray(VLOKArray array) {
        arrays.add(array);
        size += array.getSize();

        arrayCount = (short) arrays.size();
    }

    public int getSize() {
        return size;
    }

    public VLOKField findField(String name) {
        for (VLOKField field : fields) {
            if (field.getName().equals(name))
                return field;
        }
        return null;
    }

    public VLOKString findString(String name) {
        for (VLOKString string : strings) {
            if (string.getName().equals(name))
                return string;
        }
        return null;
    }

    public VLOKArray findArray(String name) {
        for (VLOKArray array : arrays) {
            if (array.getName().equals(name))
                return array;
        }
        return null;
    }

    public int getBytes(byte[] dest, int pointer) {
        pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
        pointer = writeBytes(dest, pointer, nameLength);
        pointer = writeBytes(dest, pointer, name);
        pointer = writeBytes(dest, pointer, size);

        pointer = writeBytes(dest, pointer, fieldCount);
        for (VLOKField field : fields)
            pointer = field.getBytes(dest, pointer);

        pointer = writeBytes(dest, pointer, stringCount);
        for (VLOKString string : strings)
            pointer = string.getBytes(dest, pointer);

        pointer = writeBytes(dest, pointer, arrayCount);
        for (VLOKArray array : arrays)
            pointer = array.getBytes(dest, pointer);

        return pointer;
    }

    public static VLOKObject Deserialize(byte[] data, int pointer) {
        byte containerType = data[pointer++];
        assert (containerType == CONTAINER_TYPE);

        VLOKObject result = new VLOKObject();
        result.nameLength = readShort(data, pointer);
        pointer += 2;
        result.name = readString(data, pointer, result.nameLength).getBytes();
        pointer += result.nameLength;

        result.size = readInt(data, pointer);
        pointer += 4;

        // Early-out: pointer += result.size - sizeOffset - result.nameLength;

        result.fieldCount = readShort(data, pointer);
        pointer += 2;

        for (int i = 0; i < result.fieldCount; i++) {
            VLOKField field = VLOKField.Deserialize(data, pointer);
            result.fields.add(field);
            pointer += field.getSize();
        }

        result.stringCount = readShort(data, pointer);
        pointer += 2;

        for (int i = 0; i < result.stringCount; i++) {
            VLOKString string = VLOKString.Deserialize(data, pointer);
            result.strings.add(string);
            pointer += string.getSize();
        }

        result.arrayCount = readShort(data, pointer);
        pointer += 2;

        for (int i = 0; i < result.arrayCount; i++) {
            VLOKArray array = VLOKArray.Deserialize(data, pointer);
            result.arrays.add(array);
            pointer += array.getSize();
        }

        return result;
    }

}
