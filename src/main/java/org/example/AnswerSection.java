package org.example;

import lombok.Getter;
import lombok.Setter;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

@Getter
@Setter
public class AnswerSection {
    private ArrayList<Byte> AnswerBegin;
    private ArrayList<Byte> QTYPE;
    private ArrayList<Byte> QCLASS;
    private int ttl;
    private short RDLENGTH;
    private byte[] ip;

    public AnswerSection(String ip) throws UnknownHostException {
        this.AnswerBegin = new ArrayList<Byte>();
        this.AnswerBegin.add((byte) 0b11000000);
        this.AnswerBegin.add((byte) 0b00001100);

        this.QTYPE = new ArrayList<Byte>();
        this.QTYPE.add((byte) 0);
        this.QTYPE.add((byte) 1);
        this.QCLASS = new ArrayList<Byte>();
        this.QCLASS.add((byte) 0);
        this.QCLASS.add((byte) 1);


        this.ttl = 300;
        this.RDLENGTH = 4;
        this.ip = InetAddress.getByName(ip).getAddress();
    }

    public ArrayList<Byte> toByteArrayList() {
        var res = new ArrayList<Byte>();
        res.addAll(AnswerBegin);
        res.addAll(QTYPE);
        res.addAll(QCLASS);
        byte[] ttlBytes = ByteBuffer.allocate(4).putInt(ttl).array();
        res.addAll(Support.toByteArrayList(ttlBytes));
        byte[] RDLENGTHBytes = ByteBuffer.allocate(2).putShort(RDLENGTH).array();
        res.addAll(Support.toByteArrayList(RDLENGTHBytes));
        res.addAll(Support.toByteArrayList(ip));
        return res;
    }
}
