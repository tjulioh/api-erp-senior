package dev.tjulioh.erpsenior.domain;

public enum ItemTipo {
    PRODUTO(1, "Produto"),
    SERVICO(2, "Servico");

    private final Integer valor;
    private final String descricao;

    ItemTipo(Integer valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public Integer getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }
}