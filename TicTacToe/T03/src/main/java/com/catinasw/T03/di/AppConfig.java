package com.catinasw.T03.di;

import com.catinasw.T03.datasource.mapper.DomainDataSourceMapper;
import com.catinasw.T03.datasource.model.Storage;
import com.catinasw.T03.datasource.repository.Repository;
import com.catinasw.T03.domain.service.GameServiceImpl;
import com.catinasw.T03.web.mapper.DomainWebMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.catinasw.T03")
public class AppConfig {

    @Bean
    public Storage getStorage() {
        return new Storage();
    }

    @Bean
    public DomainDataSourceMapper getDomainDSMapper() {
        return new DomainDataSourceMapper();
    }

    @Bean
    public Repository getRepository() {
        return new Repository(getStorage(), getDomainDSMapper());
    }

    @Bean
    public GameServiceImpl getGameRepository() {
        return new GameServiceImpl(getRepository());
    }

    @Bean
    public DomainWebMapper getDomainWebMapper() {
        return new DomainWebMapper();
    }
}
