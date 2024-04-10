package dev.tjulioh.erpsenior.controller;

import com.querydsl.core.types.Predicate;
import dev.tjulioh.erpsenior.domain.Pagina;
import dev.tjulioh.erpsenior.domain.Pedido;
import dev.tjulioh.erpsenior.service.PedidoService;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("pedido")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public Pagina<Pedido> findAll(@RequestParam(defaultValue = "0") Long offset,
                                         @RequestParam(defaultValue = "20") Long limit,
                                         @QuerydslPredicate(root = Pedido.class) Predicate filter) {
        return pedidoService.findAll(offset, limit, filter);
    }

    @GetMapping("/info")
    public ResponseEntity<Object> getInfo() {
        return pedidoService.getInfo();
    }

    @GetMapping("/{id}")
    public Pedido findById(@PathVariable("id") UUID id) {
        return pedidoService.findById(id);
    }

    @PostMapping
    public Pedido create(@RequestBody Pedido pedido) {
        return pedidoService.create(pedido);
    }

    @PutMapping
    public Pedido update(@RequestBody Pedido pedido) {
        return pedidoService.update(pedido);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") UUID id) {
        pedidoService.delete(id);
    }
}
