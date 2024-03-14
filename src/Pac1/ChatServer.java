package Pac1;

public class ChatServer {
    ServerSocketThread thread;
        public void start(int port) {
            thread = new ServerSocketThread("thread of server", 8000, 2000);
        }
        public void stop() {
            thread.interrupt();
        }
}
