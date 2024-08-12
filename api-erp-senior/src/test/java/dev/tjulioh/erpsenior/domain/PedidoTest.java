package dev.tjulioh.erpsenior.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PedidoTest {

    private Pedido criarPedido(){
        return new Pedido.Builder()
                .id(UUID.randomUUID())
                .criado(LocalDateTime.now())
                .desconto(BigDecimal.valueOf(5.0))
                .situacao(PedidoSituacao.ABERTO)
                .descricao("Pedido com desconto")
                .itens(new ArrayList<>())
                .numero(1)
                .build();
    }

    private Item criarItem(){
        return new Item.Builder()
                .descricao("Livro Clean Code")
                .valor(BigDecimal.valueOf(65.7))
                .tipo(ItemTipo.PRODUTO)
                .ativo(true)
                .build();
    }

    @Test
     void deveCriarPedido(){
        Pedido pedido = criarPedido();
        assertNotNull(pedido);
    }

    @Test
     void deveCriarPedidoSituacaoAberto(){
        Pedido pedido = criarPedido();
        assertEquals(PedidoSituacao.ABERTO, pedido.getSituacao());
    }

    @Test
     void deveAdicionarItemAoPedido(){
        Pedido pedido = criarPedido();
        Item item = criarItem();

        pedido.addItem(item, 5,item.getValor());

        assertEquals(item, pedido.getItens().getFirst().getItem());
    }

    @Test
     void deveAplicarDesconto(){
        Pedido pedido = criarPedido();
        Item item = criarItem();

        pedido.addItem(item, 5, BigDecimal.valueOf(5));

        assertEquals(pedido.getValorTotalComDesconto(), BigDecimal.valueOf(23.75));
    }

    @Test
    void deveRetornarTotal(){
        Pedido pedido = criarPedido();
        Item item = criarItem();

        pedido.addItem(item, 5, BigDecimal.valueOf(5));

        assertEquals(pedido.getValorTotal(), BigDecimal.valueOf(25));
    }

    @Test
     void deveExistirDesconto(){
        Pedido pedido = criarPedido();
        assertEquals(pedido.getDesconto(), BigDecimal.valueOf(5.0));
    }

    @Test
     void deveExistirDescricao(){
        Pedido pedido = criarPedido();
        assertEquals("Pedido com desconto", pedido.getDescricao());
    }

    @Test
     void deveExistirNumero(){
        Pedido pedido = criarPedido();
        assertEquals(1, pedido.getNumero());
    }

}