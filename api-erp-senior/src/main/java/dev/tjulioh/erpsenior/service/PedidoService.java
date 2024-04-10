package dev.tjulioh.erpsenior.service;

import com.querydsl.core.types.Predicate;
import dev.tjulioh.erpsenior.domain.Pagina;
import dev.tjulioh.erpsenior.domain.*;
import dev.tjulioh.erpsenior.domain.QPedido;
import dev.tjulioh.erpsenior.repository.BasicRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

@Service
public class PedidoService {

    private final BasicRepository repository;

    public PedidoService(BasicRepository repository) {
        this.repository = repository;
    }

    public Pagina<Pedido> findAll(Long offset, Long limit, Predicate filter) {
        return repository.findAll(Pedido.class, offset, limit, filter);
    }

    public Pedido findById(UUID id) {
        return repository.findById(Pedido.class, id);
    }

    public Pedido create(Pedido pedido) {
        return repository.save(pedido);
    }

    public Pedido update(Pedido pedido) {
        return repository.update(pedido);
    }

    public void delete(UUID id) {
        repository.remove(Pedido.class, id);
    }

    public ResponseEntity<Object> getInfo() {
        HashMap<String, String> info = new HashMap<>();
        info.put("quantidadePedidos", Long.toString(repository.count(Pedido.class)));
        info.put("quantidadePedidosAbertos", Long.toString(repository.count(Pedido.class , QPedido.pedido.situacao.eq(PedidoSituacao.ABERTO))));
        info.put("quantidadePedidosFechados", Long.toString(repository.count(Pedido.class , QPedido.pedido.situacao.eq(PedidoSituacao.FECHADO))));
        info.put("quantidadeItens", Long.toString(repository.count(Item.class)));
        info.put("valorTotalPedidos", repository.query(Pedido.class).fetch().stream().map(Pedido::getValorTotal).reduce(BigDecimal.ZERO, BigDecimal::add).toString());
        info.put("valorTotalDescontos", repository.query(Pedido.class).fetch().stream().map(Pedido::getValorTotalDesconto).reduce(BigDecimal.ZERO, BigDecimal::add).toString());
        return ResponseEntity.ok(info);
    }
}
