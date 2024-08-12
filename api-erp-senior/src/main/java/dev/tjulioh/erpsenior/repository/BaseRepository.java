package dev.tjulioh.erpsenior.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import dev.tjulioh.erpsenior.domain.AbstractEntity;
import dev.tjulioh.erpsenior.domain.Pagina;
import dev.tjulioh.erpsenior.util.ConstantUtil;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public class BaseRepository {

    private final EntityManager entityManager;
    private final JPQLTemplates jpqlTemplates;
    private final PathBuilderFactory pathBuilderFactory;

    public BaseRepository(
            EntityManager entityManager,
            JPQLTemplates jpqlTemplates,
            PathBuilderFactory pathBuilderFactory) {
        this.entityManager = entityManager;
        this.jpqlTemplates = jpqlTemplates;
        this.pathBuilderFactory = pathBuilderFactory;
    }

    public <T> JPAQuery<T> query(Class<T> clazz, Predicate where, List<OrderSpecifier<?>> order) {
        JPAQuery<T> query = new JPAQuery<>(this.entityManager, this.jpqlTemplates);
        query.from(this.pathBuilderFactory.create(clazz));

        if (Objects.nonNull(where)) {
            query.where(where);
        }

        if (Objects.nonNull(order)) {
            query.orderBy(order.toArray(OrderSpecifier[]::new));
        }

        return query;
    }

    public <T> JPAQuery<T> query(Class<T> clazz, Predicate where) {
        return this.query(clazz, where, null);
    }

    public <T> JPAQuery<T> query(Class<T> clazz) {
        return this.query(clazz, null, null);
    }

    public <T> Pagina<T> findAll(Class<T> clazz, Long offset, Long limit, Predicate where, List<OrderSpecifier<?>> order) {
        JPAQuery<T> query = this.query(clazz, where, order);

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

    public <T> T findOne(Class<T> clazz, Predicate where) {
        return this.query(clazz).where(where).fetchOne();
    }

    public <T> T findById(Class<T> clazz, UUID id) {
        return this.entityManager.find(clazz, id);
    }

    public <T> long count(Class<T> clazz, Predicate where) {
        return this.query(clazz, where).fetchCount();
    }

    public <T> boolean exists(Class<T> clazz, Predicate where) {
        return Objects.nonNull(this.query(clazz, where).select(Expressions.ONE).limit(1L).fetchOne());
    }

    @Transactional
    public <T> void remove(Class<T> clazz, UUID id) {
        Objects.requireNonNull(id, ConstantUtil.OBJETO_VAZIO);

        Object existing = this.entityManager.find(clazz, id);
        Objects.requireNonNull(existing, ConstantUtil.OBJETO_INEXISTENTE);

        this.entityManager.remove(existing);
    }

    @Transactional
    public <T> T save(T entity) {
        Objects.requireNonNull(entity, ConstantUtil.OBJETO_VAZIO);
        this.entityManager.persist(entity);
        this.entityManager.flush();
        this.entityManager.refresh(entity);
        return entity;
    }

    @Transactional
    public <T extends AbstractEntity> T update(T entity) {
        Objects.requireNonNull(entity, ConstantUtil.OBJETO_VAZIO);

        Object existing = this.entityManager.find(entity.getClass(), entity.getId());
        Objects.requireNonNull(existing, ConstantUtil.OBJETO_INEXISTENTE);

        this.entityManager.merge(entity);
        return entity;
    }
}
