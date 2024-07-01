package dev.tjulioh.erpsenior.controller;

import dev.tjulioh.erpsenior.domain.Pedido;
import dev.tjulioh.erpsenior.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pedido")
public class PedidoController extends AbstractController<Pedido> {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        super(pedidoService);
        this.pedidoService = pedidoService;
    }

    @GetMapping("/info")
    public ResponseEntity<Object> getInfo() {
        return pedidoService.getInfo();
    }
}
