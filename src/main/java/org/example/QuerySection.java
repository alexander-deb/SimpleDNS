package org.example;

import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Getter
@Setter
public class QuerySection {
    private String domainName;
    private int beginIndex = 12;
    private int endIndex;
    private ArrayList<Byte> QTYPE;
    private ArrayList<Byte> QCLASS;
    private ArrayList<Byte> rawSection;


    public QuerySection(byte[] data) {
        this.domainName = new String();
        var currentIndex = this.beginIndex;
        while (data[currentIndex] != 0) {
            var num_of_octets = (int)data[currentIndex];
//                        System.out.println("Num_of_octets: " + num_of_octets);
            var word = Receiver.getbytes(data, currentIndex +1, currentIndex +num_of_octets);
            this.domainName += new String(Receiver.toByteArray(word), StandardCharsets.UTF_8) + ".";
            currentIndex += num_of_octets + 1;
        }

        System.out.println("domainName: " + this.domainName);
        currentIndex += 1;
        this.QTYPE = Receiver.getbytes(data, currentIndex, currentIndex +1);
        this.QCLASS = Receiver.getbytes(data, currentIndex +2, currentIndex +3);
        this.endIndex = currentIndex +3;
        this.rawSection = Receiver.getbytes(data, this.beginIndex, this.endIndex);
    }
}
