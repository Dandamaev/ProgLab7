package network;

import database.Credentials;
import database.CurrentUser;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import network.packets.CommandExecutionPacket;
import utils.AppConstant;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

@Slf4j
public class ClientHandler extends Thread {
    private class ResponseReceiver extends Thread {

        protected volatile Object receivedObject = null;

        @Override
        public void run() {
            while (true) {
                try {
                    receiveData();
                } catch (ClosedChannelException ignored) {
                } catch (EOFException ex) {
                    System.err.println("Reached limit of data to receive");
                    log.error("Reached Limit", ex);
                } catch (IOException | ClassNotFoundException e) {
                    log.error("I/O Problems", e);
                }
            }
        }

        /**
         * Функция для получения данных
         */
        public void receiveData() throws IOException, ClassNotFoundException {
            synchronized (ClientHandler.class) {
                //check if server is online comparing the passed time with the actual from the request sent
                //TODO only restarting connection after the second command, find a way to wait after the send
                //TODO is being like that because in the first send not have time to check the down condition
                if (channel.requestWasSent() && !flagReceived && System.currentTimeMillis() - startRequestTime > 1000) {
                    channel.setConnectionToFalse();
                }
            }

            final ByteBuffer buf = ByteBuffer.allocate(AppConstant.MESSAGE_BUFFER);
            final SocketAddress addressFromServer = channel.receiveDatagram(buf);
            buf.flip();

            byte[] bytes = new byte[buf.remaining()];
            buf.get(bytes);

            if (bytes.length < 1)
                return;

            synchronized (ClientHandler.class) {
                channel.setRequestSent(false);
                if (bytes.length < AppConstant.MESSAGE_BUFFER)
                    receivedObject = processResponse(bytes);
                else
                    throw new EOFException();
            }
        }

        /**
         * Функция для десериализации полученных данных
         * @param petitionBytes - данные
         * @return obj - объект десериализованных данных
         */
        private Object processResponse(byte[] petitionBytes) throws IOException, ClassNotFoundException {
            try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(petitionBytes))) {
                final Object obj = stream.readObject();
                log.info("received object: " + obj);
                if (obj == null)
                    throw new ClassNotFoundException();
                return obj;
            }
        }
    }

    private final ResponseReceiver receiverThread;
    private final ClientUdpChannel channel;
    private final CurrentUser currentUser;
    private volatile long startRequestTime = 0L;
    private volatile boolean flagReceived = false;

    public ClientHandler(ClientUdpChannel channel, CurrentUser user) {
        this.channel = channel;
        this.currentUser = user;
        receiverThread = new ResponseReceiver();
        receiverThread.setName("ClientReceiverThread");
        receiverThread.start();
    }

    public void checkForResponse() throws ClassNotFoundException {
        startRequestTime = System.currentTimeMillis();

        Object received = receiverThread.receivedObject;

        if (received instanceof String && received.equals("connect")) {
            channel.setConnected(true);
            log.info("Successfully connected to the server");
            System.out.println("Successfully connected to the server");
        }

        synchronized (this) {
            if (received != null) {
                flagReceived = true;
                printResponse(received);
            } else {
                flagReceived = false;
            }
            receiverThread.receivedObject = null;
        }

        //System.out.println("wassent: " + channel.requestWasSent() + "  connection: " + channel.connected + "   addr: " + channel.addressServer);
    }

    /**
     * Функция для вывода объектов коллекции
     * @param obj- коллекция с объектами
     */
    public void printResponse(Object obj) throws ClassNotFoundException {
        if (obj instanceof String) {
            System.out.println(obj);
        }
        else if(obj instanceof CommandExecutionPacket){
            Object resObj = ((CommandExecutionPacket) obj).getMessage();
            if (resObj instanceof Credentials) {
                currentUser.setCredentials((Credentials) resObj);
                System.out.println("Current User set to: " + ((Credentials) resObj).username);
            }else if(resObj instanceof String) {
                System.out.println(resObj);
            }
        }
        else
            throw new ClassNotFoundException();
    }

    public void finishReceiver() {
        receiverThread.interrupt();
    }

    public ResponseReceiver getReceiver() {
        return receiverThread;
    }

}
