package pl.mfconsulting.java.demo.spring.jooq.tools.dbServer;

import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;

import java.sql.SQLException;

@Slf4j
public class TcpH2Server {
    private Server tcpServer;

    public static void main(String[] args) {
        TcpH2Server server = new TcpH2Server();
        try {
            server.start();
            //Keep server running
            Thread.currentThread().join();
        } catch (SQLException | InterruptedException e) {
            log.error("Error has been thrown", e);
        }
    }

    public void start() throws SQLException {
        this.tcpServer = Server.createTcpServer(
                "-tcpPort", "9123",
                "-tcpAllowOthers",
                "-ifNotExists"
        ).start();

        log.info("H2 TCP server started on port: 9123");
    }

    public void stop() {
        if (tcpServer != null) {
            tcpServer.stop();
            log.info("H2 TCP server stopped!");
        }
    }
}
