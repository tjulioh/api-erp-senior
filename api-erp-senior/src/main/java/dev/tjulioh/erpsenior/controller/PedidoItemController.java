package dev.tjulioh.erpsenior.controller;

import dev.tjulioh.erpsenior.domain.PedidoItem;
import dev.tjulioh.erpsenior.service.PedidoItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pedido-item")
public class PedidoItemController extends AbstractController<PedidoItem>{

    private final PedidoItemService pedidoItemService;

    public PedidoItemController(PedidoItemService pedidoItemService) {
        super(pedidoItemService);
        this.pedidoItemService = pedidoItemService;
    }
}
