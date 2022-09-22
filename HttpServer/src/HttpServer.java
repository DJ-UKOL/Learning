import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
/*
 * Http server for 15 minutes
 * https://habr.com/ru/post/69136/
 */
public class HttpServer {

    private static class SocketProcessor implements Runnable {

        private  Socket s;
        private InputStream is;
        private  OutputStream os;

        private SocketProcessor(Socket s) throws Throwable {
            this.s = s;
            this.is = s.getInputStream();
            this.os = s.getOutputStream();
        }

        @Override
        public void run() {
            try {
                readInputHeaders();
                writeResponse("<html><body><h1>Hello from Habrahabr</h1></body></html>");
            } catch(Throwable t) {

            } finally {
                try{
                    s.close();
                } catch (Throwable t) {

                }
            }
            System.out.println("Client processing finished");
        }

        private void readInputHeaders() throws Throwable {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while(true) {
                String s = br.readLine();
                if(s == null || s.trim().length() == 0) {
                    break;
                }
            }
        }

        private void writeResponse(String s) throws Throwable{
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Server: YarServer/2009-09-09\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + s.length() + "\r\n" +
                    "Connection: close\r\n\r\n";
            String result = response + s;
            os.write(result.getBytes());
            os.flush();
        }
    }

    public static void main(String[] args) throws Throwable{
        ServerSocket ss = new ServerSocket(8080);
        while(true) {
            Socket s = ss.accept();
            System.err.println("Client accepted");
            new Thread(new SocketProcessor(s)).start();
        }
    }
}
