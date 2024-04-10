package dev.tjulioh.erpsenior.configuration;

import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAProvider;
import dev.tjulioh.erpsenior.repository.BasicRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryAutoConfiguration {

    @Bean
    public BasicRepository basicRepository(EntityManager entityManager, PathBuilderFactory pathBuilderFactory, JPQLTemplates jpqlTemplates) {
        return new BasicRepository(entityManager, jpqlTemplates, pathBuilderFactory);
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
