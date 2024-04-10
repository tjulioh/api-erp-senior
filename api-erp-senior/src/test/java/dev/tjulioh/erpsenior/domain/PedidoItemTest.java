package dev.tjulioh.erpsenior.domain;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PedidoItemTest {

    private Pedido criarPedido() {
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

    private Item criarItem() {
        return new Item.Builder()
                .descricao("Livro Clean Code")
                .valor(BigDecimal.valueOf(65.7))
                .ativo(true)
                .tipo(ItemTipo.PRODUTO)
                .build();
    }

    private Item criarItemDesativado() {
        return new Item.Builder()
                .descricao("Livro Go Horse")
                .valor(BigDecimal.valueOf(65.7))
                .ativo(false)
                .tipo(ItemTipo.PRODUTO)
                .build();
    }

    private PedidoItem criarPedidoItem() {
        return new PedidoItem.Builder()
                .id(UUID.randomUUID())
                .item(criarItem())
                .pedido(criarPedido())
                .quantidade(5)
                .valor(BigDecimal.valueOf(65.7))
                .build();
    }

    @Test
    void deveCriarPedidoItem() {
        PedidoItem pedidoItem = criarPedidoItem();
        assertNotNull(pedidoItem);
    }

    @Test
    void deveExistirQuantidade() {
        PedidoItem pedidoItem = criarPedidoItem();
        assertEquals(pedidoItem.getQuantidade(), 5);
    }

    @Test
    void deveExistirValor() {
        PedidoItem pedidoItem = criarPedidoItem();
        assertEquals(pedidoItem.getValor(), BigDecimal.valueOf(65.7));
    }

    @Test
    void deveExistirItem() {
        PedidoItem pedidoItem = criarPedidoItem();
        assertEquals(pedidoItem.getItem().getDescricao(), "Livro Clean Code");
    }

    @Test
    void deveExistirPedido() {
        PedidoItem pedidoItem = criarPedidoItem();
        assertEquals(pedidoItem.getPedido().getDescricao(), "Pedido com desconto");
    }

    @Test
    void deveExistirId() {
        PedidoItem pedidoItem = criarPedidoItem();
        assertNotNull(pedidoItem.getId());
    }

    @Test
    void naoPermitirItemDesativado() {
        PedidoItem pedidoItem = criarPedidoItem();

        PedidoItem.Builder builder = new PedidoItem.Builder().from(pedidoItem).item(criarItemDesativado());

        assertThrows(ValidationException.class, builder::build);
    }
}