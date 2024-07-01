package dev.tjulioh.erpsenior.service;

import com.querydsl.core.types.dsl.Expressions;
import dev.tjulioh.erpsenior.domain.Item;
import dev.tjulioh.erpsenior.domain.Pedido;
import dev.tjulioh.erpsenior.domain.PedidoSituacao;
import dev.tjulioh.erpsenior.domain.QPedido;
import dev.tjulioh.erpsenior.repository.BaseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class PedidoService extends AbstractService<Pedido> {

    private final BaseRepository repository;

    public PedidoService(BaseRepository repository) {
        super(repository, Pedido.class);
        this.repository = repository;
    }

    public ResponseEntity<Object> getInfo() {
        HashMap<String, String> info = new HashMap<>();
//        info.put("quantidadePedidos", Long.toString(repository.count(Pedido.class)));
//        info.put("quantidadePedidosAbertos", Long.toString(repository.count(Pedido.class , QPedido.pedido.situacao.eq(PedidoSituacao.ABERTO))));
//        info.put("quantidadePedidosFechados", Long.toString(repository.count(Pedido.class , QPedido.pedido.situacao.eq(PedidoSituacao.FECHADO))));
//        info.put("quantidadeItens", Long.toString(repository.count(Item.class)));
//        info.put("valorTotalPedidos", repository.query(Pedido.class).fetch().stream().map(Pedido::getValorTotal).reduce(BigDecimal.ZERO, BigDecimal::add).toString());
//        info.put("valorTotalDescontos", repository.query(Pedido.class).fetch().stream().map(Pedido::getValorTotalDesconto).reduce(BigDecimal.ZERO, BigDecimal::add).toString());
        info.put("descricao",
//                repository.query(Pedido.class)
//                        .where(Expressions.path(UUID.class, "id").eq(UUID.fromString("68a3234e-3cc3-4e0d-a495-9589366da749")))
//                        .fetch().getFirst().getDescricao()
                        repository.query(Pedido.class)
                        .where(QPedido.pedido.itens.any().id.eq(UUID.fromString("9a5a872c-572f-4883-afbb-5eddd4ba9e6b")))
                        .fetch().getFirst().getDescricao()
        );
        return ResponseEntity.ok(info);
//        return ResponseEntity.accepted().build();
    }
}
