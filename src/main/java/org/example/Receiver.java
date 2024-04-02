package org.example;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Receiver {
    public static void main(String[] args) {
        int port = args.length == 0 ? 5555 : Integer.parseInt(args[0]);
        new Receiver().run(port);
    }

    public void run(int port) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(port);
            byte[] receiveData = new byte[512];
            System.out.printf("Listening on udp:%s:%d%n",
                    InetAddress.getLocalHost().getHostAddress(), port);
            DatagramPacket receivePacket = new DatagramPacket(receiveData,
                    receiveData.length);

            while(true)
            {
                serverSocket.receive(receivePacket);
                if (receivePacket.getData().length > 0) {
                    String sentence = new String( receivePacket.getData(), 6,
                            receivePacket.getLength() );
                    for (byte n : receivePacket.getData()) {
                        System.out.print(Integer.toHexString(n) + " ");
                    }

                    var packetData = receivePacket.getData();
                    var configMap = ConfigParser.getConfigMap("config.txt");
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

                    HeaderSection headerSection = new HeaderSection(toByteArray(getbytes(packetData, 0, 11)));
                    System.out.println("\nHEADER: " + printbytes(headerSection.toBytes()));


                    QuerySection querySection = new QuerySection(packetData);

                    headerSection.getFlags().setQR((byte) 1);
                    headerSection.getFlags().setZ((byte) 0);
                    headerSection.getARCOUNT().set(1, (byte) 0);

                    System.out.println("QUERY SECTION: " + printbytes(querySection.getRawSection()));
                    ArrayList<AnswerSection> listOfAnswers = new ArrayList<>();
                    var queryDomain = querySection.getDomainName().endsWith(".") ? querySection.getDomainName().substring(0, querySection.getDomainName().length()-1) : querySection.getDomainName();
                    var domainNames = configMap.getOrDefault(queryDomain, new ArrayList<>());


                    if (domainNames.isEmpty()) {
                        headerSection.getANCOUNT().set(1, (byte) 0);
                        dataOutputStream.write(toByteArray(headerSection.toBytes()));
                        dataOutputStream.write(toByteArray(querySection.getRawSection()));
                    } else {
                        for (var item : domainNames) {
                            listOfAnswers.add(new AnswerSection(item));
                        }
                        headerSection.getANCOUNT().set(1, (byte) domainNames.size());
                        dataOutputStream.write(toByteArray(headerSection.toBytes()));
                        dataOutputStream.write(toByteArray(querySection.getRawSection()));

                        for (var item : listOfAnswers) {
                            dataOutputStream.write(toByteArray(item.toByteArrayList()));
                        }
                    }

                    byte[] dnsFrame = byteArrayOutputStream.toByteArray();
                    DatagramPacket sendPacket = new DatagramPacket(dnsFrame, dnsFrame.length,
                            receivePacket.getAddress(), receivePacket.getPort());
                    System.out.println("Sending: " + dnsFrame.length + " bytes");
                    for (int i = 0; i < dnsFrame.length; i++) {
                        System.out.print(Integer.toHexString(dnsFrame[i]) + " ");

                    }
                    serverSocket.send(sendPacket);
                }

            }
        } catch (IOException e) {
            System.out.println(e);
        }
        // should close serverSocket in finally block
    }

    public static ArrayList<Byte> getbytes(byte[] data, int from, int to) {
        ArrayList<Byte> result = new ArrayList<Byte>();
        for (int i = from; i <= to; i +=1) {
            result.add(data[i]);
        }
        return result;
    }


    public String printbytes(ArrayList<Byte> data) {
        StringBuilder res = new StringBuilder(new String());
        for (int i = 0; i < data.size(); i +=1) {
           res.append(Integer.toHexString(data.get(i))).append(" ");
        }
        return res.toString();
    }

    public static byte[] toByteArray(ArrayList<Byte> data) {
        var result = new byte[data.size()];
        for (var i = 0; i < data.size(); i++) {
            result[i] = data.get(i);
        }
        return result;
    }
}