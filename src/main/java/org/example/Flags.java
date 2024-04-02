package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class Flags {
    private byte QR;
    private byte Opcode;
    private byte AA;
    private byte TC;
    private byte RD;
    private byte RA;
    private byte Z;
    private byte RCODE;

    public Flags(ArrayList<Byte> bytes) {
        if (bytes.size() < 2) {
            throw new RuntimeException("Flags is very short");
        }


        byte first = bytes.get(0);
        this.RD = (byte) (first % 2);
        first /= 2;
        this.TC = (byte) (first % 2);
        first /= 2;
        this.AA = (byte) (first % 2);
        first /= 2;
        this.Opcode = (byte) (first % 16);
        first /= 16;
        this.QR = (byte) (first % 2);
        first /= 2;

        byte second = bytes.get(1);
        this.RCODE = (byte) (second % 16);
        second /= 16;
        this.Z = (byte) (second % 8);
        second /= 8;
        this.RA = (byte) (second % 2);
        second /= 2;


    }

    public ArrayList<Byte> toBytes() {
        var result = new ArrayList<Byte>();
        result.add((byte)Integer.parseInt(Support.toStringBytes(QR, 1) + Support.toStringBytes(Opcode, 4) + Support.toStringBytes(AA, 1) + Support.toStringBytes(TC, 1) + Support.toStringBytes(RD, 1), 2));
        result.add((byte)Integer.parseInt(Support.toStringBytes(RA, 1) + Support.toStringBytes(Z, 3) + Support.toStringBytes(RCODE, 4), 2));
        return result;
    }
}
