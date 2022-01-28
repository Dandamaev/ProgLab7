package network;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.ByteBuffer;

@Slf4j
public class ServerSocket {

    public static final int SOCKET_TIMEOUT = 3000;

    protected DatagramSocket socket;

    public ServerSocket(InetSocketAddress a) throws SocketException {
        socket = new DatagramSocket(a);
        socket.setSoTimeout(SOCKET_TIMEOUT);
    }

    public void sendDatagram(byte[] content, SocketAddress client) throws IOException {
        DatagramPacket packet = new DatagramPacket(content, content.length, client);
        socket.send(packet);

        log.info("Sent datagram from SERVER to " + client);
    }

    public SocketAddress receiveDatagram(ByteBuffer buffer) throws IOException {
        byte[] buf = new byte[buffer.remaining()];

        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        System.out.println("\nReceived datagram in SERVER from " + packet.getSocketAddress());
        log.info("Received datagram in SERVER from " + packet.getSocketAddress());
        buffer.put(buf, 0, packet.getLength());
        return packet.getSocketAddress();
    }

    public void sendResponse(Object response, SocketAddress client) {
        try(ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayStream)) {

            objectStream.writeObject(response);
            System.out.println("send object " + response.toString());
            log.info("send object " + response.toString());

            sendDatagram(byteArrayStream.toByteArray(), client);

        } catch (IOException e) {
            log.error("Problem sending the response", e);
        }
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void disconnect() {
        socket.disconnect();
    }
}
