package ru.java_two.chat.server.core;

public class ChatServer {
    ServerSocketThread thread;

    public void start(int port) {
        if (thread != null && thread.isAlive()) {
            System.out.println("Server already started");
        }else {
            thread = new ServerSocketThread("thread of server", 8000, 2000);
        }
    }
    public void stop() {
        if (thread == null || thread.isAlive()) {
            System.out.println("Server is not  running");
        }else {
            thread.interrupt();
        }
    }
}
