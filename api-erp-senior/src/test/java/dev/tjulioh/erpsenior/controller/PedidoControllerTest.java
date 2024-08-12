package dev.tjulioh.erpsenior.controller;

import dev.tjulioh.erpsenior.domain.Pedido;
import dev.tjulioh.erpsenior.domain.PedidoSituacao;
import dev.tjulioh.erpsenior.repository.BaseRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PedidoControllerTest {

    @Mock
    private BaseRepository repository;

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

    @Test
    void deveRetornarPedido() throws Exception {
        Pedido pedido = criarPedido();
        when(repository.findById(Pedido.class, pedido.getId())).thenReturn(pedido);
        mockMvc.perform(get("/pedido/" + pedido.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}