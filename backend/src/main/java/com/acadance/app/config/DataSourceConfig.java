package com.acadance.app.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DataSourceConfig {

    // Render gives you one connection string like:
    // postgres://user:password@host:5432/databasename
    @Value("${DATABASE_URL}")
    private String databaseUrl;

    @Bean
    public DataSource dataSource() {
        URI uri = URI.create(databaseUrl);

        String[] userInfo = uri.getUserInfo().split(":", 2);
        String username = userInfo[0];
        String password = userInfo.length > 1 ? userInfo[1] : "";

        int port = uri.getPort() == -1 ? 5432 : uri.getPort();

        String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + port + uri.getPath()
                + "?sslmode=require";

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("org.postgresql.Driver");

        return dataSource;
    }
}
