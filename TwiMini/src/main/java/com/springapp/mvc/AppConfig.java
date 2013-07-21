package com.springapp.mvc;

import java.beans.PropertyVetoException;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.OperationType;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages = "com.springapp.mvc")
@PropertySource(value = "classpath:/application.properties")
@EnableWebMvc
@EnableTransactionManagement
public class AppConfig{

    @Bean
    public JdbcTemplate jdbcTemplate() throws PropertyVetoException {
        PGPoolingDataSource source = new PGPoolingDataSource();

        source.setDataSourceName("Datasource");
        source.setServerName("172.16.155.82");
        source.setPortNumber(5432);
        source.setDatabaseName("vivek");
        source.setUser("vivek");
        source.setPassword("123");
        source.setMaxConnections(30);
        JdbcTemplate ret=new JdbcTemplate(source);
        System.out.println("hello");
        return ret;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertiesConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public Keyspace keyspace(){
        CassandraHostConfigurator configurator = new CassandraHostConfigurator("127.0.0.1");
        configurator.setPort(9160);
        configurator.setMaxActive(100);
        configurator.setAutoDiscoverHosts(true);
        configurator.setRunAutoDiscoveryAtStartup(true);
        configurator.setCassandraThriftSocketTimeout(100);

        Cluster cluster = HFactory.getOrCreateCluster("Test Cluster", configurator);
        Keyspace keyspace = HFactory.createKeyspace("MiniTwitter", cluster);

        keyspace.setConsistencyLevelPolicy(new ConsistencyLevelPolicy() {
            @Override
            public HConsistencyLevel get(OperationType operationType) {
                return HConsistencyLevel.ONE;
            }

            @Override
            public HConsistencyLevel get(OperationType operationType, String s) {
                return HConsistencyLevel.ONE;
            }
        });
        return keyspace;
    }

}
