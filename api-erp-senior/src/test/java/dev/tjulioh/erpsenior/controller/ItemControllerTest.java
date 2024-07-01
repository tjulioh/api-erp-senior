package dev.tjulioh.erpsenior.controller;

import dev.tjulioh.erpsenior.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
                .id(UUID.randomUUID())
                .descricao("Livro Clean Code")
                .valor(BigDecimal.valueOf(65.7))
                .tipo(ItemTipo.PRODUTO)
                .ativo(true)
                .build();
    }

    @Test
    void naoExcluirItemEmUso() throws Exception {
        Item item = criarItem();
        Pedido pedido = criarPedido();

        pedido.addItem(item, 5, item.getValor());

        mockMvc.perform(delete("/item/" + item.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }
}