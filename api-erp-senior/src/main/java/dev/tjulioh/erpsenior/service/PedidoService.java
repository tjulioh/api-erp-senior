package dev.tjulioh.erpsenior.service;

import dev.tjulioh.erpsenior.domain.Item;
import dev.tjulioh.erpsenior.domain.Pedido;
import dev.tjulioh.erpsenior.domain.PedidoSituacao;
import dev.tjulioh.erpsenior.domain.QPedido;
import dev.tjulioh.erpsenior.repository.BaseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class PedidoService extends AbstractService<Pedido> {

    private final BaseRepository repository;

    public PedidoService(BaseRepository repository) {
        super(repository, Pedido.class);
        this.repository = repository;
    }

    public ResponseEntity<Object> getInfo() {
        HashMap<String, String> info = new HashMap<>();
        info.put("quantidadePedidos", Long.toString(repository.count(Pedido.class)));
        info.put("quantidadePedidosAbertos", Long.toString(repository.count(Pedido.class, QPedido.pedido.situacao.eq(PedidoSituacao.ABERTO))));
        info.put("quantidadePedidosFechados", Long.toString(repository.count(Pedido.class, QPedido.pedido.situacao.eq(PedidoSituacao.FECHADO))));
        info.put("quantidadeItens", Long.toString(repository.count(Item.class)));
        info.put("valorTotalPedidos", repository.findAll(Pedido.class).stream().map(Pedido::getValorTotal).reduce(BigDecimal.ZERO, BigDecimal::add).toString());
        info.put("valorTotalDescontos", repository.findAll(Pedido.class).stream().map(Pedido::getValorTotalDesconto).reduce(BigDecimal.ZERO, BigDecimal::add).toString());
        return ResponseEntity.ok(info);
    }
}
