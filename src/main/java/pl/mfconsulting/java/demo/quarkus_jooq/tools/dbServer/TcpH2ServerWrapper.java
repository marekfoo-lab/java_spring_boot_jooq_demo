package pl.mfconsulting.java.demo.quarkus_jooq.tools.dbServer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Slf4j
@Component
public class TcpH2ServerWrapper {

    private final TcpH2Server tcpServer;

    @Autowired
    public TcpH2ServerWrapper() {
        this.tcpServer = new TcpH2Server();
    }

    @PostConstruct
    public void start() throws SQLException {
        tcpServer.start();
    }

    @PreDestroy
    public void stop() {
        tcpServer.stop();
    }
}