package Chatt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JTextArea log = new JTextArea();
    private final JPanel paneTop = new JPanel(new GridLayout(2, 3));

    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8000");
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Always on top");
    private final JTextField tfLogin = new JTextField("Ilia");
    private final JPasswordField tfPassword = new JPasswordField("12345");
    private final JButton btnLogin =new JButton("Login");

    private final JPanel paneBottom = new JPanel(new BorderLayout());
    private final JButton btnDisconnect = new JButton("<html><b>Disconnect</b></html>");
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");

    private final JList<String> userList = new JList<>();


    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI();
            }
        });
    }

    private ClientGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH,HEIGHT);
        String[] users = {"user1,\n user2, user3, user4, user5",
        };

        userList.setListData(users);

        paneTop.add(tfIPAddress);
        paneTop.add(tfPort);
        paneTop.add(cbAlwaysOnTop);
        paneTop.add(tfLogin);
        paneTop.add(tfPassword);
        paneTop.add(btnLogin);
        paneBottom.add(btnDisconnect, BorderLayout.WEST);
        paneBottom.add(tfMessage, BorderLayout.CENTER);
        paneBottom.add(btnSend, BorderLayout.EAST);

        add(log, BorderLayout.CENTER);
        add(userList, BorderLayout.EAST);
        add(paneTop, BorderLayout.NORTH);
        add(paneBottom, BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if(true) {

        }else {
            throw new RuntimeException("Unknown source" + src);
        }

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        String msg;
        StackTraceElement [] ste = e.getStackTrace();
        msg = "Exception in" + t.getName() + " " +
                e.getClass().getCanonicalName() + ": " +
                e.getMessage() + "\n\t at " + ste[0];
        JOptionPane.showMessageDialog(this, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        System.exit(1);

    }
}
