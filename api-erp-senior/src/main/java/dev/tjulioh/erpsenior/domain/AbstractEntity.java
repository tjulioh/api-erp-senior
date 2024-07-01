package dev.tjulioh.erpsenior.domain;

import java.util.UUID;

public abstract class AbstractEntity {

    private UUID id;

    public UUID getId() {
        return id;
    }
}
