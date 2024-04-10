package dev.tjulioh.erpsenior.service;

import com.querydsl.core.types.Predicate;
import dev.tjulioh.erpsenior.domain.Item;
import dev.tjulioh.erpsenior.domain.Pagina;
import dev.tjulioh.erpsenior.repository.BasicRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ItemService {

    private final BasicRepository repository;

    public ItemService(BasicRepository repository) {
        this.repository = repository;
    }

    public Pagina<Item> findAll(Long offset, Long limit, Predicate filter) {
        return repository.findAll(Item.class, offset, limit, filter);
    }

    public Item findById(UUID id) {
        return repository.findById(Item.class, id);
    }

    public Item create(Item item) {
        return repository.save(item);
    }

    public Item update(Item item) {
        return repository.update(item);
    }

    public void delete(UUID id) {
         repository.remove(Item.class, id);
    }
}
