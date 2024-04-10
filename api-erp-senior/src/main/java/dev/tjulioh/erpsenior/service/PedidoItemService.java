package dev.tjulioh.erpsenior.service;

import com.querydsl.core.types.Predicate;
import dev.tjulioh.erpsenior.domain.Pagina;
import dev.tjulioh.erpsenior.domain.PedidoItem;
import dev.tjulioh.erpsenior.repository.BasicRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PedidoItemService {

    private final BasicRepository repository;

    public PedidoItemService(BasicRepository repository) {
        this.repository = repository;
    }

    public Pagina<PedidoItem> findAll(Long offset, Long limit, Predicate filter) {
        return repository.findAll(PedidoItem.class, offset, limit, filter);
    }

    public PedidoItem findById(UUID id) {
        return repository.findById(PedidoItem.class, id);
    }

    public PedidoItem create(PedidoItem pedidoItem) {
        return repository.save(pedidoItem);
    }

    public PedidoItem update(PedidoItem pedidoItem) {
        return repository.update(pedidoItem);
    }

    public void delete(UUID id) {
         repository.remove(PedidoItem.class, id);
    }
}
