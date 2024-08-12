package dev.tjulioh.erpsenior.controller;

import dev.tjulioh.erpsenior.domain.AbstractEntity;
import dev.tjulioh.erpsenior.domain.Pagina;
import dev.tjulioh.erpsenior.service.AbstractService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

public abstract class AbstractController<T extends AbstractEntity> {

    private final AbstractService<T> abstractService;

    protected AbstractController(AbstractService<T> abstractService) {
        this.abstractService = abstractService;
    }

    @GetMapping
    public Pagina<T> findAll(@RequestParam(defaultValue = "0") Long offset,
                                   @RequestParam(defaultValue = "20") Long limit,
                                   @RequestParam(required = false) String filter,
                                   @RequestParam(required = false) String sort) {
        return abstractService.findAll(offset, limit, filter, sort);
    }

    @GetMapping("/{id}")
    public T findById(@PathVariable("id") UUID id) {
        return abstractService.findById(id);
    }

    @PostMapping
    public T create(@RequestBody T entity) {
        return abstractService.create(entity);
    }

    @PutMapping
    public T update(@RequestBody T entity) {
        return abstractService.update(entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") UUID id) {
        abstractService.delete(id);
    }
}