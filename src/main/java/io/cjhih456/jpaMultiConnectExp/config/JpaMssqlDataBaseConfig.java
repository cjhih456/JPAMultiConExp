package io.cjhih456.jpaMultiConnectExp.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "mssqlEntityManager",
        transactionManagerRef = "mssqlTransactionManager",
        basePackages = "io.cjhih456.jpaMultiConnectExp.model.repository.mssql"
)
public class JpaMssqlDataBaseConfig {

    @Bean(name = "mssqlDataSource")
    @ConfigurationProperties(prefix = "spring.mssql.datasource")
    public DataSource mssqlDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mssqlEntityManager")
    public LocalContainerEntityManagerFactoryBean mssqlEntityManagerFactory(EntityManagerFactoryBuilder builder){
        return builder.dataSource(mssqlDataSource())
                .properties(hibernateProperties())
                .packages("io.cjhih456.jpaMultiConnectExp.model.database.mssql")
                .persistenceUnit("mssqlDBPU")
                .build();
    }

    @Bean(name = "mssqlTransactionManager")
    public PlatformTransactionManager mssqlTransactionManager(@Qualifier("mssqlEntityManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map hibernateProperties() {

        Resource resource = new ClassPathResource("hibernateMssql.properties");
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            return properties.entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> e.getKey().toString(),
                            e -> e.getValue())
                    );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}