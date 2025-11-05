package pl.mfconsulting.java.demo.quarkus_jooq.tools.dbServer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
@ApplicationScoped
public class TcpH2ServerWrapper {

    private final TcpH2Server tcpServer;

    @Inject
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