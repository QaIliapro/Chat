package ru.java_two.chat.server.core;

import ru.java_two.network.ServerSocketThread;
import ru.java_two.network.ServerSocketThreadListener;
import ru.java_two.network.SocketThread;
import ru.java_two.network.SocketThreadListener;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ChatServer implements ServerSocketThreadListener, SocketThreadListener {
    ServerSocketThread thread;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public void start(int port) {
        if (thread != null && thread.isAlive()) {
            System.out.println("Server already started");
        }else {
            thread = new ServerSocketThread(this,"Thread of server", 8000, 2000);
        }
    }

    public void stop() {
        if (thread == null || thread.isAlive()) {
            System.out.println("Server is not  running");
        }else {
            thread.interrupt();
        }
    }

    private void putLog(String msg) {
        msg = DATE_FORMAT.format(System.currentTimeMillis()) +
                Thread.currentThread().getName() + ": " + msg;
        System.out.println(msg);
    }
    @Override
    public void onServerStart(ServerSocketThread thread) {

    }

    @Override
    public void onServerStop(ServerSocketThread thread) {

    }

    @Override
    public void onServerSocketCreated(ServerSocketThread thread, ServerSocket server) {
        putLog("Server timeout");

    }

    @Override
    public void onServerTimeout(ServerSocketThread thread, ServerSocket server) {

    }

    @Override
    public void onServerAccepted(ServerSocketThread thread, ServerSocket server, Socket socket) {
        putLog("Client connect");

    }

    @Override
    public void onServerException(ServerSocketThread thread, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {
        putLog("Socket created");
    }

    @Override
    public void onSocketStop(SocketThread thread) {
        putLog("Socket stopped");
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        putLog("Socket ready");
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        thread.sendMessage("echo" + msg);
    }

    public synchronized void onSocketException(SocketThread thread, Exception exception) {
        exception.printStackTrace();

    }

}
