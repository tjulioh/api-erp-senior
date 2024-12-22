package dev.tjulioh.erpsenior.service;

import dev.tjulioh.erpsenior.domain.Item;
import dev.tjulioh.erpsenior.repository.BaseRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemService extends AbstractService<Item> {

    public ItemService(BaseRepository repository) {
        super(repository, Item.class);
    }
}
