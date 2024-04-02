package org.example;

import java.util.ArrayList;

public class Support {
    public static String toStringBytes(byte data, int size) {
        var res = Integer.toString(data, 2);

        return "0".repeat(size - res.length()) + res;
    }

    public static ArrayList<Byte> toByteArrayList(byte[] data) {
        var result = new ArrayList<Byte>();
        for (var i = 0; i < data.length; i++) {
            result.add(data[i]);
        }
        return result;
    }
}
