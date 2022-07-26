package com.harry.carrental.harrycarrental.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Car rental system spring configuration
 * @date 2022/7/24
 * @author harryzhu
 */
@Slf4j
@Configuration
public class CarRentalConfiguration {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();

        return new DruidDataSource();
    }
}
