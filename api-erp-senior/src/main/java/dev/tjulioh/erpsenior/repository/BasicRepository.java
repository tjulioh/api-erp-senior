package dev.tjulioh.erpsenior.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import dev.tjulioh.erpsenior.domain.AbstractEntity;
import dev.tjulioh.erpsenior.domain.Pagina;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BasicRepository {

    private static final String OBJETO_VAZIO = "O objeto nao deve estar vazio";
    private static final String OBJETO_INEXISTENTE = "O objeto deve existir";

    private final EntityManager entityManager;
    private final JPQLTemplates jpqlTemplates;
    private final PathBuilderFactory pathBuilderFactory;

    public BasicRepository(
            EntityManager entityManager,
            JPQLTemplates jpqlTemplates,
            PathBuilderFactory pathBuilderFactory) {
        this.entityManager = entityManager;
        this.jpqlTemplates = jpqlTemplates;
        this.pathBuilderFactory = pathBuilderFactory;
    }

    public <T> JPAQuery<T> query(Class<T> entityClass, Predicate... where) {
        JPAQuery<T> query = new JPAQuery<>(this.entityManager, this.jpqlTemplates);
        query.from(this.pathBuilderFactory.create(entityClass));

        if (Objects.nonNull(where)) {
            query.where(where);
        }

        return query;
    }

    public <T> Pagina<T> findAll(Class<T> entityClass, Long offset, Long limit, Predicate... where) {
        return this.findAll(entityClass, PageRequest.of(Math.toIntExact(offset), Math.toIntExact(limit)), where);
    }

    public <T> Pagina<T> findAll(Class<T> entityClass, PageRequest pageRequest, Predicate... where) {
        Long offset = pageRequest.getOffset();
        Long limit = (long) pageRequest.getPageSize();

        JPAQuery<T> query = this.findAll(entityClass, where);

        Long total = query.fetchCount();

        boolean proximo = false;

        if (total.equals(0L)) {
            return Pagina.empty();
        } else if (offset + limit > total) {
            offset = (total / limit) * limit;
        } else if (total <= (offset + limit)) {
            offset = Math.max(offset - limit, 0);
        } else {
            proximo = true;
        }

        query.offset(offset);
        query.limit(limit);
        List<T> resultado = query.fetch();

        return new Pagina.Builder<T>()
                .proximo(proximo)
                .atual(offset)
                .limite(limit)
                .total(total)
                .conteudo(resultado)
                .build();
    }

    public <T> JPAQuery<T> findAll(Class<T> entityClass, Predicate... where) {
        return this.query(entityClass, where);
    }

    public <T> T findOne(Class<T> entityClass, Predicate... where) {
        return this.query(entityClass).where(where).fetchOne();
    }

    public <T> T findById(Class<T> entityClass, UUID id) {
        return this.entityManager.find(entityClass, id);
    }

    public <T> long count(Class<T> entityClass, Predicate... where) {
        return this.query(entityClass, where).fetchCount();
    }

    public <T> boolean exists(Class<T> entityClass, Predicate... where) {
        return Objects.nonNull(this.query(entityClass, where).select(Expressions.ONE).limit(1L).fetchOne());
    }

    @Transactional
    public <T> void remove(Class<T> entityClass, UUID id) {
        Objects.requireNonNull(id, OBJETO_VAZIO);

        Object existing = this.entityManager.find(entityClass, id);
        Objects.requireNonNull(existing, OBJETO_INEXISTENTE);

        this.entityManager.remove(existing);
    }

    @Transactional
    public <T> T save(T entity) {
        Objects.requireNonNull(entity, OBJETO_VAZIO);
        this.entityManager.persist(entity);
        this.entityManager.flush();
        this.entityManager.refresh(entity);
        return entity;
    }

    @Transactional
    public <T extends AbstractEntity> T update(T entity) {
        Objects.requireNonNull(entity, OBJETO_VAZIO);

        Object existing = this.entityManager.find(entity.getClass(), entity.getId());
        Objects.requireNonNull(existing, OBJETO_INEXISTENTE);

        this.entityManager.merge(entity);
        return entity;
    }
}
