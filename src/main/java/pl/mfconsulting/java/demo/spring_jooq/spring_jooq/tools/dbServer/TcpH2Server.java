package pl.mfconsulting.java.demo.spring_jooq.spring_jooq.tools.dbServer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.h2.tools.Server;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class TcpH2Server {

    private Server tcpServer;

    @PostConstruct
    public void start() throws SQLException {
        this.tcpServer = Server.createTcpServer(
                "-tcpPort", "9123",
                "-tcpAllowOthers",
                "-ifNotExists"
        ).start();

        System.out.println("H2 TCP server started on port 9123");
    }

    @PreDestroy
    public void stop() {
        if (tcpServer != null) {
            tcpServer.stop();
            System.out.println("H2 TCP server stopped");
        }
    }
}