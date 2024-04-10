package dev.tjulioh.erpsenior.domain;

public enum PedidoSituacao {
    ABERTO(1,"Aberto"),
    FECHADO(2,"Fechado");

    private final Integer valor;
    private  final String descricao;

    PedidoSituacao(Integer valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public Integer getValor() {
        return valor;
    }

    public  String getDescricao() {
        return descricao;
    }
}