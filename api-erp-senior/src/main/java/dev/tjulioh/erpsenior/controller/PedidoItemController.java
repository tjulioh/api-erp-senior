package dev.tjulioh.erpsenior.controller;

import com.querydsl.core.types.Predicate;
import dev.tjulioh.erpsenior.domain.Pagina;
import dev.tjulioh.erpsenior.domain.Pedido;
import dev.tjulioh.erpsenior.domain.PedidoItem;
import dev.tjulioh.erpsenior.service.PedidoItemService;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("pedido-item")
public class PedidoItemController {

    private final PedidoItemService pedidoItemService;

    public PedidoItemController(PedidoItemService pedidoItemService) {
        this.pedidoItemService = pedidoItemService;
    }

    @GetMapping
    public Pagina<PedidoItem> findAll(@RequestParam(defaultValue = "0") Long offset,
                                         @RequestParam(defaultValue = "20") Long limit,
                                         @QuerydslPredicate(root = Pedido.class) Predicate filter) {
        return pedidoItemService.findAll(offset, limit, filter);
    }

    @GetMapping("/{id}")
    public PedidoItem findById(@PathVariable("id") UUID id) {
        return pedidoItemService.findById(id);
    }

    @PostMapping
    public PedidoItem create(@RequestBody PedidoItem pedidoItem) {
        return pedidoItemService.create(pedidoItem);
    }

    @PutMapping
    public PedidoItem update(@RequestBody PedidoItem pedidoItem) {
        return pedidoItemService.update(pedidoItem);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") UUID id) {
        pedidoItemService.delete(id);
    }
}
