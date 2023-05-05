import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class localserver {
    public static void main(String[] args) throws IOException {
        int port = 5000;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted connection from " + clientSocket.getRemoteSocketAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String requestLine = in.readLine();
            System.out.println("Received request: " + requestLine);

            OutputStream out = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);

            if (requestLine.startsWith("GET")) {
                String[] parts = requestLine.split(" ");
                String path = parts[1];
                if (path.equals("/") || path.equals("/index.html")) {
                    byte[] content = Files.readAllBytes(Paths.get("index.html"));
                    writer.println("HTTP/1.1 200 OK");
                    writer.println("Content-Type: text/html");
                    writer.println("Content-Length: " + content.length);
                    writer.println();
                    out.write(content);
                } else if (path.endsWith(".css")) {
                    byte[] content = Files.readAllBytes(Paths.get(path.substring(1)));
                    writer.println("HTTP/1.1 200 OK");
                    writer.println("Content-Type: text/css");
                    writer.println("Content-Length: " + content.length);
                    writer.println();
                    out.write(content);
                } else if (path.endsWith(".js")) {
                    byte[] content = Files.readAllBytes(Paths.get(path.substring(1)));
                    writer.println("HTTP/1.1 200 OK");
                    writer.println("Content-Type: text/javascript");
                    writer.println("Content-Length: " + content.length);
                    writer.println();
                    out.write(content);
                } else {
                    writer.println("HTTP/1.1 404 Not Found");
                }
            } else {
                writer.println("HTTP/1.1 400 Bad Request");
            }

            writer.close();
            clientSocket.close();
        }
    }
}
