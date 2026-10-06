package pfs.lms.enquiry.collateral;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** Minimal SMTP server for the workflow tests: accepts every mail and keeps its recipients and text. */
public final class TestSmtpServer implements AutoCloseable {

    /** One received mail. */
    public static final class Mail {
        public final List<String> recipients = new CopyOnWriteArrayList<>();
        public final StringBuilder data = new StringBuilder();
    }

    private final ServerSocket serverSocket;
    private final List<Mail> mails = new CopyOnWriteArrayList<>();
    private final Thread thread;

    public TestSmtpServer() throws IOException {
        serverSocket = new ServerSocket(0);
        thread = new Thread(this::serve, "test-smtp");
        thread.setDaemon(true);
        thread.start();
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public List<Mail> getMails() {
        return mails;
    }

    private void serve() {
        while (!serverSocket.isClosed()) {
            try (Socket socket = serverSocket.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                out.print("220 test SMTP\r\n");
                out.flush();
                Mail mail = new Mail();
                String line;
                boolean inData = false;
                while ((line = in.readLine()) != null) {
                    if (inData) {
                        if (line.equals(".")) {
                            inData = false;
                            mails.add(mail);
                            mail = new Mail();
                            out.print("250 OK\r\n");
                        } else {
                            mail.data.append(line).append('\n');
                        }
                    } else {
                        String command = line.toUpperCase();
                        if (command.startsWith("EHLO") || command.startsWith("HELO")) {
                            out.print("250 test\r\n");
                        } else if (command.startsWith("RCPT TO:")) {
                            mail.recipients.add(line.substring(8).replaceAll("[<> ]", ""));
                            out.print("250 OK\r\n");
                        } else if (command.startsWith("DATA")) {
                            inData = true;
                            out.print("354 End data with <CR><LF>.<CR><LF>\r\n");
                        } else if (command.startsWith("QUIT")) {
                            out.print("221 Bye\r\n");
                            out.flush();
                            break;
                        } else {
                            out.print("250 OK\r\n");
                        }
                    }
                    out.flush();
                }
            } catch (IOException ex) {
                if (serverSocket.isClosed()) {
                    return;
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
