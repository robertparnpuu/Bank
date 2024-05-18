package com.rparnp.bank.config;

import com.rparnp.bank.enums.CurrencyType;
import com.rparnp.bank.handler.CurrencyTypeHandler;
import com.rparnp.bank.handler.UUIDTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.UUID;

@Configuration
public class MyBatisConfig {
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.getTypeHandlerRegistry().register(UUID.class, new UUIDTypeHandler());
        configuration.getTypeHandlerRegistry().register(CurrencyType.class, new CurrencyTypeHandler());

        sessionFactory.setConfiguration(configuration);

        return sessionFactory.getObject();
    }
}
