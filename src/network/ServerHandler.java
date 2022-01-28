package network;

import commands.AbstractCommand;
import database.CollectionDBManager;
import database.Credentials;
import database.DatabaseController;
import exceptions.InvalidValueException;
import lombok.extern.slf4j.Slf4j;
import managers.CollectionManager;
import managers.ConsoleManager;
import network.packets.CommandExecutionPacket;
import network.packets.CommandPacket;
import utils.AppConstant;

import java.io.*;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ServerHandler {

    private class RequestReceiver extends Thread {

        @Override
        public void run() {
            while (true) {
                receiveData();
            }
        }

        /**
         * Функция для получения данных
         */
        public void receiveData() {
            SocketAddress addressFromClient = null;
            try {
                final ByteBuffer buf = ByteBuffer.allocate(AppConstant.MESSAGE_BUFFER);
                addressFromClient = socket.receiveDatagram(buf);
                buf.flip();
                final byte[] petitionBytes = new byte[buf.remaining()];
                buf.get(petitionBytes);

                if (petitionBytes.length > 0)
                    processRequest(petitionBytes, addressFromClient);

            } catch (SocketTimeoutException ignored) {
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Weird errors, check log");
                log.error("Weird errors processing the received data", e);
                executeObj("Weird errors, check log. " + e.getMessage(), addressFromClient);
            }
        }

        /**
         * Функция для десериализации данных
         * @param petitionBytes - полученные данные
         */
        private void processRequest(byte[] petitionBytes, SocketAddress addressFromClient) throws IOException, ClassNotFoundException {
            try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(petitionBytes))) {
                final Object obj = stream.readObject();
                log.info("received object: " + obj);
                if (obj == null)
                    throw new ClassNotFoundException();
                executeObj(obj, addressFromClient);
            }
        }
    }

    private final ServerSocket socket;
    private final RequestReceiver requestReceiver;
    private final ForkJoinPool executor;
    private final ConsoleManager consoleManager;
    private final CollectionManager collectionManager;
    private final DatabaseController databaseController;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public ServerHandler(ServerSocket socket, CollectionManager collectionManager, DatabaseController databaseController) {
        this.socket = socket;
        this.consoleManager = new ConsoleManager(new InputStreamReader(System.in), new OutputStreamWriter(outputStream), false);
        this.collectionManager = collectionManager;
        this.databaseController = databaseController;

        requestReceiver = new RequestReceiver();
        requestReceiver.setName("ServerReceiverThread");
        executor = new ForkJoinPool();
    }

    public void receiveFromWherever() {
        requestReceiver.start();
    }

    private void executeObj(Object obj, SocketAddress addressFromClient) {
        Future<Object> resulted = executor.submit(() -> {
            Object responseExecution;
            if (obj instanceof String)
                responseExecution = obj;
            else {
                AbstractCommand command = ((CommandPacket) obj).getCommand();
                Credentials credentials = ((CommandPacket) obj).getCredentials();
                try {
                    outputStream.reset();
                    Object retObj = command.execute(consoleManager, collectionManager, databaseController, credentials);
                    if(retObj instanceof Credentials){
                        responseExecution = new CommandExecutionPacket(retObj);
                    }else if(retObj != null){
                        responseExecution = new CommandExecutionPacket(retObj);
                    }else {
                        responseExecution = new CommandExecutionPacket(new String(outputStream.toByteArray()));
                    }
                } catch (InvalidValueException ex) {
                    responseExecution = ex.getMessage();
                    log.error(ex.getMessage());
                }
            }
            socket.sendResponse(responseExecution, addressFromClient);
            return responseExecution;
        });

        try {
            System.out.println("Object gotten from executor: \n" + resulted.get().toString());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error getting result from executor", e);
            System.out.println("Error getting result from executor: " + e.getMessage());
        }
    }

    public void disconnect() {
        log.info("Disconnecting the server...");
        System.out.println("Disconnecting the server...");
        try {
            executor.shutdown();
            executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("Interrupted executor during shutdown",e);
            System.out.println("Interrupted during finishing the queued tasks");
        }
        socket.getSocket().disconnect();
        requestReceiver.interrupt();
    }
}
