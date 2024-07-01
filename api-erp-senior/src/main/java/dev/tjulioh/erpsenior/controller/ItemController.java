package dev.tjulioh.erpsenior.controller;

import dev.tjulioh.erpsenior.domain.Item;
import dev.tjulioh.erpsenior.service.ItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("item")
public class ItemController extends AbstractController<Item> {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        super(itemService);
        this.itemService = itemService;
    }
}
