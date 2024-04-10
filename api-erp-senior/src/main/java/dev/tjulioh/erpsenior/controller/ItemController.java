package dev.tjulioh.erpsenior.controller;

import com.querydsl.core.types.Predicate;
import dev.tjulioh.erpsenior.domain.Item;
import dev.tjulioh.erpsenior.domain.Pagina;
import dev.tjulioh.erpsenior.service.ItemService;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public Pagina<Item> findAll(@RequestParam(defaultValue = "0") Long offset,
                                       @RequestParam(defaultValue = "20") Long limit,
                                       @QuerydslPredicate(root = Item.class) Predicate filter) {
        return itemService.findAll(offset, limit, filter);
    }

    @GetMapping("/{id}")
    public Item findById(@PathVariable("id") UUID id) {
        return itemService.findById(id);
    }

    @PostMapping
    public Item create(@RequestBody Item pedido) {
        return itemService.create(pedido);
    }

    @PutMapping
    public Item update(@RequestBody Item pedido) {
        return itemService.update(pedido);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") UUID id) {
        itemService.delete(id);
    }
}
