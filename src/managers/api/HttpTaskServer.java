package managers.api;

import com.sun.net.httpserver.HttpServer;
import managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer httpServer;

    public HttpTaskServer(TaskManager manager) {
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            this.httpServer.createContext("/tasks", new TasksHandler(manager));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void startServer() {
        this.httpServer.start();
    }

    public void stopServer() {
        this.httpServer.stop(0);
    }
}