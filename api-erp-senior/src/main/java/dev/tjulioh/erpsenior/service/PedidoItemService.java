package dev.tjulioh.erpsenior.service;

import dev.tjulioh.erpsenior.domain.PedidoItem;
import dev.tjulioh.erpsenior.repository.BaseRepository;
import org.springframework.stereotype.Service;

@Service
public class PedidoItemService extends AbstractService<PedidoItem> {

    private final BaseRepository repository;

    public PedidoItemService(BaseRepository repository) {
        super(repository, PedidoItem.class);
        this.repository = repository;
    }
}
