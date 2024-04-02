package org.example;


import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;

@Setter
@Getter
public class HeaderSection {
    private ArrayList<Byte> TransactionId;
    private Flags flags;
    private ArrayList<Byte> QDCOUNT;
    private ArrayList<Byte> ANCOUNT;
    private ArrayList<Byte> NSCOUNT;
    private ArrayList<Byte> ARCOUNT;

    public HeaderSection(byte[] header) {
        if (header.length < 12) {
            throw new RuntimeException("header is very short");
        }
        TransactionId = Receiver.getbytes(header, 0, 1);
        this.flags = new Flags(Receiver.getbytes(header, 2, 3));
        QDCOUNT = Receiver.getbytes(header, 4, 5);
        ANCOUNT = Receiver.getbytes(header, 6, 7);
        NSCOUNT = Receiver.getbytes(header, 8, 9);
        ARCOUNT = Receiver.getbytes(header, 10, 11);
    }

    public ArrayList<Byte> toBytes() {
        var result = new ArrayList<Byte>();
        result.addAll(TransactionId);
        result.addAll(flags.toBytes());
        result.addAll(QDCOUNT);
        result.addAll(ANCOUNT);
        result.addAll(NSCOUNT);
        result.addAll(ARCOUNT);
        return result;
    }
}
