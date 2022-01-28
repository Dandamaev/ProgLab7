package network;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ClientUdpChannel {

    protected DatagramChannel channel;
    protected SocketAddress addressServer;
    protected volatile boolean connected;
    protected volatile boolean requestSent;

    public ClientUdpChannel() throws IOException {
        channel = DatagramChannel.open();
        channel.configureBlocking(true);
        channel.bind(null);
        addressServer = null;
    }

    /**
     * Функция для подключения к серверу по адресу
     * @param addressServer - адрес сервера
     */
    public void tryToConnect(InetSocketAddress addressServer) {
        this.addressServer = addressServer;
        sendCommand("connect");
        System.out.println("Trying to reach the server...");
        log.info("Trying to reach the server...");
    }

    /**
     * Функция для отправки байт-буфера
     * @param content - байт-буфер
     */
    public void sendDatagram(ByteBuffer content) throws IOException {
        channel.send(content, addressServer);
        this.requestSent = true;
        log.info("sent datagram to " + addressServer);
    }

    /**
     * Функция для получения датаграммы и записи ее в буфер
     * @param buffer - буфер, в который записывается датаграмма
     * @return ret - адрес сервера
     */
    public SocketAddress receiveDatagram(ByteBuffer buffer) throws IOException {
        SocketAddress ret;
        ret = channel.receive(buffer);
        return ret;
    }

    /**
     * Функция для сериализации и отправки команды
     * @param command - отправляемая команда
     */
    public void sendCommand(Object command) {
        try(ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayStream)) {

            objectStream.writeObject(command);
            log.info("send object " + command);
            final ByteBuffer objectBuffer = ByteBuffer.wrap(byteArrayStream.toByteArray());

            sendDatagram(objectBuffer);
            Thread.sleep(500);
        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            log.error("", e);
        }
    }

    /**
     * Функция для отключения от сервера
     */
    public void disconnect() {
        try {
            channel.close();
        } catch (IOException e) {
            log.error("Error trying to disconnect, doing a forced out", e);
            System.exit(-1);
        }
    }
    /**
     * Функция для проверки подключения к серверу
     */
    public boolean isConnected() {
        return addressServer != null && connected;
    }

    /**
     * Функция для задания подключения/отключения к серверу
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * Функция для задания отключения от сервера
     */
    public void setConnectionToFalse() {
        this.addressServer = null;
        this.connected = false;
    }

    /**
     * Функция получения информации о том, был ли отправлен ответ
     * @return boolean requestSent
     */
    public boolean requestWasSent() {
        return requestSent;
    }
    public void setRequestSent(boolean requestSent) {
        this.requestSent = requestSent;
    }
}
