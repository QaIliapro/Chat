package ru.java_two.chat.server.core;

import ru.java_two.chat.comman.Library;
import ru.java_two.network.ServerSocketThread;
import ru.java_two.network.ServerSocketThreadListener;
import ru.java_two.network.SocketThread;
import ru.java_two.network.SocketThreadListener;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class ChatServer implements ServerSocketThreadListener, SocketThreadListener {
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private ServerSocketThread thread;
    private final ChatServerListener listener;
    private final Vector<SocketThread> clients;


    public ChatServer(ChatServerListener listener) {
        this.listener = listener;
        this.clients = new Vector<>();
    }

    public void start(int port) {
        if (thread != null && thread.isAlive()) {
            putLog("Server already started");
        }else {
            thread = new ServerSocketThread(this,"Thread of server", 8000, 2000);
        }
    }

    public void stop() {
        if (thread == null || thread.isAlive()) {
            putLog("Server is not  running");
        }else {
            thread.interrupt();
        }
    }

    private void putLog(String msg) {
        msg = DATE_FORMAT.format(System.currentTimeMillis()) +
                Thread.currentThread().getName() + ": " + msg;
        listener.onChatServerMassage(msg);
    }
    @Override
    public void onServerStart(ServerSocketThread thread) {
        putLog("Server thread started");
        SqlClient.connect();
    }

    @Override
    public void onServerStop(ServerSocketThread thread) {
        putLog("Server thread stopped");
        SqlClient.disconnect();

    }

    @Override
    public void onServerSocketCreated(ServerSocketThread thread, ServerSocket server) {
        putLog("Server timeout");

    }

    @Override
    public void onServerTimeout(ServerSocketThread thread, ServerSocket server) {

    }
//всесто он сервера должен быть он  сокет
    @Override
    public void onServerAccepted(ServerSocketThread thread, ServerSocket server, Socket socket) {
        putLog("Client connect");
        String name = "SocketThread " + socket.getInetAddress() + ":" + socket.getPort();
        new ClientThread(this, name, socket);

    }

    @Override
    public void onServerException(ServerSocketThread thread, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public synchronized void onSocketStart(SocketThread thread, Socket socket) {
        putLog("Socket created");
    }

    @Override
    public synchronized void onSocketStop(SocketThread thread) {
        putLog("Socket stopped");
    }

    @Override
    public synchronized void onSocketReady(SocketThread thread, Socket socket) {
        putLog("Socket ready");
        clients.add(thread);
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        ClientThread client = (ClientThread) thread;
        if (client.isAuthorized())
            handleAuthMessage(client, msg);
        else
            handleNonAuthMessage(client, msg);
//        for (int i = 0; i < clients.size(); i++) {
//            SocketThread client = clients.get(i);
//            client.sendMessage(message);
//        }
    }
    public void handleNonAuthMessage(ClientThread client,String msg) {
        String[] arr = msg.split(Library.DELIMITER);
        if (arr.length != 3 || !arr[0].equals(Library.AUTH_REQUEST)) {
            client.msgFormatError(msg);
            return;
        }
        String login = arr[1];
        String password = arr[2];
        String nickname = SqlClient.getNickname(login,password);
        if (nickname == null) {
            putLog("Invalid login attempt" + login);
            client.authFail();
            return;
        }
        client.authAccept(nickname);
    }
    public void handleAuthMessage(ClientThread client,String msg) {
        for (int i = 0; i < clients.size(); i++) {
            ClientThread recipient = (ClientThread)clients.get(i);
            if (!recipient.isAuthorized())continue;
            recipient.sendMessage(Library.getTypeBroadcast(client.getNickname(),msg));
        }
    }

    public synchronized void onSocketException(SocketThread thread, Exception exception) {
        exception.printStackTrace();

    }

}
