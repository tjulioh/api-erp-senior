package dev.tjulioh.erpsenior.service;

import dev.tjulioh.erpsenior.domain.AbstractEntity;
import dev.tjulioh.erpsenior.domain.Pagina;
import dev.tjulioh.erpsenior.exception.NotFoundException;
import dev.tjulioh.erpsenior.repository.BaseRepository;
import dev.tjulioh.antlr.querydsl.filter.converter.OrderSpecifierConverter;
import dev.tjulioh.antlr.querydsl.filter.converter.PredicateConverter;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public abstract class AbstractService<T extends AbstractEntity> {

    private final BaseRepository baseRepository;
    private final Class<T> clazz;
    private final PredicateConverter predicateConverter;
    private final OrderSpecifierConverter orderSpecifierConverter;

    AbstractService(BaseRepository baseRepository, Class<T> clazz) {
        this.baseRepository = baseRepository;
        this.clazz = clazz;
        this.predicateConverter = new PredicateConverter(clazz);
        this.orderSpecifierConverter = new OrderSpecifierConverter(clazz);
    }

    public Pagina<T> findAll(Long offset, Long limit, String filter, String sort) {
        return baseRepository.findAll(clazz, offset, limit, predicateConverter.toPredicate(filter), orderSpecifierConverter.toOrderSpecifier(sort));
    }

    public T findById(UUID id) {
        T entity = baseRepository.findById(clazz, id);
        if (Objects.nonNull(entity)) {
            return entity;
        }
        throw new NotFoundException("A entidade com este identificador não foi encontrado");
    }

    public T create(T item) {
        return baseRepository.save(item);
    }

    public T update(T item) {
        return baseRepository.update(item);
    }

    public void delete(UUID id) {
        baseRepository.remove(clazz, id);
    }
}
