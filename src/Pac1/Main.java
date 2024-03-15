package Pac1;

public class Main {
    private static class MyThread extends Thread {
        public MyThread(String name) {
            super(name);
            start();
        }
    }

    private static int a = 0;
    private static int b = 0;
    private static int c = 0;

    private static Object mon = new Object();

    private synchronized static void incAllVars() {
        for (int i = 0; i < 1_000_000; i++) {
            a = a + 1;
            b = b + 1;
            c = c + 1;
        }
        String vars = String.format("a=%d, b=%d, c=%d", a, b, c);
        System.out.println(vars);
    }

    public static void main(String[] args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                incAllVars();
            }
        };
        new Thread(r, "Thread-0").start();
    }
}

