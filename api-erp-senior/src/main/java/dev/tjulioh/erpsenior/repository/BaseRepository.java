package dev.tjulioh.erpsenior.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import dev.tjulioh.erpsenior.domain.AbstractEntity;
import dev.tjulioh.erpsenior.domain.Pagina;
import dev.tjulioh.erpsenior.Constants;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BaseRepository {

    private final EntityManager entityManager;
    private final JPQLTemplates jpqlTemplates;
    private final PathBuilderFactory pathBuilderFactory;

    @Autowired
    public BaseRepository(
            EntityManager entityManager,
            JPQLTemplates jpqlTemplates,
            PathBuilderFactory pathBuilderFactory) {
        this.entityManager = entityManager;
        this.jpqlTemplates = jpqlTemplates;
        this.pathBuilderFactory = pathBuilderFactory;
    }

    public <T> JPAQuery<T> query(Class<T> clazz, Predicate where, List<OrderSpecifier<?>> order, List<Expression<?>> select) {
        JPAQuery<T> query = new JPAQuery<>(entityManager, jpqlTemplates);
        query.from(pathBuilderFactory.create(clazz));

        if (Objects.nonNull(select)) {
            query.select(select.toArray(Expression[]::new));
        }

        if (Objects.nonNull(where)) {
            query.where(where);
        }

        if (Objects.nonNull(order)) {
            query.orderBy(order.toArray(OrderSpecifier[]::new));
        }

        return query;
    }

    public <T> JPAQuery<T> query(Class<T> clazz, Predicate where, List<OrderSpecifier<?>> order) {
        return query(clazz, where, order, null);
    }

    public <T> JPAQuery<T> query(Class<T> clazz, Predicate where) {
        return query(clazz, where, null);
    }

    public <T> JPAQuery<T> query(Class<T> clazz) {
        return query(clazz, null, null);
    }

    public <T> List<T> findAll(Class<T> clazz){
       return query(clazz).fetch();
    }

    public <T> Pagina<T> findAll(Class<T> clazz, Long offset, Long limit, Predicate where, List<OrderSpecifier<?>> order) {
        JPAQuery<T> query = query(clazz, where, order);

        Long total = count(query);

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

    public <T> T findOne(JPAQuery<T> query) {
        return query.fetchOne();
    }

    public <T> T findOne(Class<T> clazz, Predicate where) {
        return findOne(query(clazz, where));
    }

    public <T> T findById(Class<T> clazz, UUID id) {
        return entityManager.find(clazz, id);
    }

    public <T> long count(JPAQuery<T> query) {
        return Optional.of(query.clone().select(Expressions.ONE.count())).map(this::findOne).orElse(0L);
    }

    public <T> long count(Class<T> clazz) {
        return count(clazz, null);
    }

    public <T> long count(Class<T> clazz, Predicate where) {
        return count(query(clazz, where));
    }

    public <T> boolean exists(Class<T> clazz, Predicate where) {
        return Objects.nonNull(findOne(query(clazz, where).select(Expressions.ONE)));
    }

    @Transactional
    public <T> void remove(Class<T> clazz, UUID id) {
        Objects.requireNonNull(id, Constants.OBJETO_VAZIO);

        Object existing = entityManager.find(clazz, id);
        Objects.requireNonNull(existing, Constants.OBJETO_INEXISTENTE);

        entityManager.remove(existing);
    }

    @Transactional
    public <T> T save(T entity) {
        Objects.requireNonNull(entity, Constants.OBJETO_VAZIO);
        entityManager.persist(entity);
        entityManager.flush();
        entityManager.refresh(entity);
        return entity;
    }

    @Transactional
    public <T extends AbstractEntity> T update(T entity) {
        Objects.requireNonNull(entity, Constants.OBJETO_VAZIO);

        Object existing = entityManager.find(entity.getClass(), entity.getId());
        Objects.requireNonNull(existing, Constants.OBJETO_INEXISTENTE);

        entityManager.merge(entity);
        return entity;
    }
}
