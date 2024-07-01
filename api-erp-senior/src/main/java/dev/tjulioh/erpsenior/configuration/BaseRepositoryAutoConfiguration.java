package dev.tjulioh.erpsenior.configuration;

import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAProvider;
import dev.tjulioh.erpsenior.repository.BaseRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseRepositoryAutoConfiguration {

    @Bean
    public BaseRepository baseRepository(EntityManager entityManager, PathBuilderFactory pathBuilderFactory, JPQLTemplates jpqlTemplates) {
        return new BaseRepository(entityManager, jpqlTemplates, pathBuilderFactory);
    }

    @Bean
    public JPQLTemplates jpqlTemplates(EntityManager entityManagerFactory) {
        return JPAProvider.getTemplates(entityManagerFactory);
    }

    @Bean
    public PathBuilderFactory pathBuilderFactory() {
        return new PathBuilderFactory();
    }
}
