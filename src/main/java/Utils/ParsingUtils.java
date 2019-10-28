package Utils;

import java.nio.ByteBuffer;

public class ParsingUtils {
    public static byte[] readByteArray(ByteBuffer buffer, int length) {
        byte[] arr = new byte[length];
        buffer.get(arr);
        return arr;
    }

    public static void writeByteArray(ByteBuffer buffer, byte[] bytes) {
        buffer.putInt(bytes.length);
        buffer.put(bytes);
    }

    public static String readString(ByteBuffer buffer) {
        int length = buffer.getInt();
        return new String(readByteArray(buffer, length));
    }

    public static void writeString(ByteBuffer buffer, String str) {
        byte[] bytes = str.getBytes();
        writeByteArray(buffer, bytes);
    }
}
