package dev.tjulioh.erpsenior.service;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import dev.tjulioh.erpsenior.domain.AbstractEntity;
import dev.tjulioh.erpsenior.domain.Pagina;
import dev.tjulioh.erpsenior.exception.NotFoundException;
import dev.tjulioh.erpsenior.repository.BaseRepository;
import dev.tjulioh.erpsenior.util.OrderSpecifierConverter;
import dev.tjulioh.erpsenior.util.PredicateConverter;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public abstract class AbstractService<T extends AbstractEntity> {

    protected final BaseRepository repository;
    private final Class<T> clazz;
    private final PredicateConverter predicateConverter;
    private final OrderSpecifierConverter orderSpecifierConverter;

    public AbstractService(BaseRepository repository, Class<T> clazz) {
        this.repository = repository;
        this.clazz = clazz;
        this.predicateConverter = new PredicateConverter(clazz);
        this.orderSpecifierConverter = new OrderSpecifierConverter(clazz);
    }

    public Pagina<T> findAll(Long offset, Long limit, String filter, String sort) {
        return repository.findAll(clazz, offset, limit, toPredicate(filter), toOrderSpecifier(sort));
    }

    public T findById(UUID id) {
        T entity = repository.findById(clazz, id);
        if (Objects.nonNull(entity)) {
            return entity;
        }
        throw new NotFoundException("A entidade com este identificador não foi encontrado");
    }

    public T create(T item) {
        return repository.save(item);
    }

    public T update(T item) {
        return repository.update(item);
    }

    public void delete(UUID id) {
        repository.remove(clazz, id);
    }

    public Predicate toPredicate(String filter) {
        if (Objects.nonNull(filter)) {
            return predicateConverter.toPredicate(filter);
        }
        return null;
    }

    public OrderSpecifier<?>[] toOrderSpecifier(String sort) {
        if (Objects.nonNull(sort)) {
            return orderSpecifierConverter.toOrderSpecifier(sort);
        }
        return null;
    }
}
