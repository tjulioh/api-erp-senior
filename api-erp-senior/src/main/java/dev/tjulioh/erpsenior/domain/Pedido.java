package dev.tjulioh.erpsenior.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "PEDIDOS")
public class Pedido implements AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private UUID id;

    @NotBlank(message = "A descrição do produto é obrigatório")
    @Size(min = 2, max = 100)
    private String descricao;

    @NotNull(message = "O numero é obrigatório")
    @Positive
    private Integer numero;

    @NotNull(message = "A data de criação é obrigatória")
    private LocalDateTime criado;

    @NotNull(message = "A situacao é obrigatório")
    private PedidoSituacao situacao;

    private BigDecimal desconto;

    @JsonManagedReference
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<PedidoItem> itens;

    public Pedido() {
        this.criado = LocalDateTime.now();
        this.situacao = PedidoSituacao.ABERTO;
        this.itens = new ArrayList<>();
    }

    public Pedido(LocalDateTime criado, BigDecimal desconto, String descricao, UUID id, List<PedidoItem> itens, Integer numero, PedidoSituacao situacao) {
        this.criado = criado;
        this.desconto = desconto;
        this.descricao = descricao;
        this.id = id;
        this.itens = itens;
        this.numero = numero;
        this.situacao = situacao;
    }

    private Pedido(Builder builder) {
        this.criado = builder.criado;
        this.desconto = builder.desconto;
        this.descricao = builder.descricao;
        this.id = builder.id;
        this.itens = builder.itens;
        this.numero = builder.numero;
        this.situacao = builder.situacao;
    }

    public static class Builder {
        private LocalDateTime criado;
        private BigDecimal desconto;
        private String descricao;
        private UUID id;
        private List<PedidoItem> itens;
        private Integer numero;
        private PedidoSituacao situacao;

        public Builder criado(LocalDateTime criado) {
            this.criado = criado;
            return this;
        }

        public Builder desconto(BigDecimal desconto) {
            this.desconto = desconto;
            return this;
        }

        public Builder descricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder itens(List<PedidoItem> itens) {
            this.itens = itens;
            return this;
        }

        public Builder numero(Integer numero) {
            this.numero = numero;
            return this;
        }

        public Builder situacao(PedidoSituacao situacao) {
            this.situacao = situacao;
            return this;
        }

        public Pedido build() {
            Pedido pedido = new Pedido(criado, desconto, descricao, id, itens, numero, situacao);
            validate(pedido);
            return pedido;
        }

        public Builder from(Pedido pedido) {
            this.criado = pedido.criado;
            this.desconto = pedido.desconto;
            this.descricao = pedido.descricao;
            this.id = pedido.id;
            this.itens = pedido.itens;
            this.numero = pedido.numero;
            this.situacao = pedido.situacao;
            return this;
        }

        public void validate(Pedido pedido) {
            verificaExisteCriado(pedido);
            descontoSomenteSeAberto(pedido);
        }

        private void verificaExisteCriado(Pedido pedido) {
            if (Objects.isNull(pedido.getCriado())) {
                throw new ValidationException("Deve existir uma data de criacao");
            }
        }

        private void descontoSomenteSeAberto(Pedido pedido) {
            if (Objects.nonNull(pedido.getDesconto()) && !pedido.getSituacao().equals(PedidoSituacao.ABERTO)) {
                throw new ValidationException("Nao é possivel aplicar o desconto pois a situacao é diferente de ABERTO");
            }
        }
    }

    public LocalDateTime getCriado() {
        return criado;
    }

    public void setCriado(LocalDateTime criado) {
        this.criado = criado;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<PedidoItem> getItens() {
        return itens;
    }

    public void setItens(List<PedidoItem> itens) {
        this.itens = itens;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public PedidoSituacao getSituacao() {
        return situacao;
    }

    public void setSituacao(PedidoSituacao situacao) {
        this.situacao = situacao;
    }

    public void addItem(Item item, Integer quantidade, BigDecimal valor) {
        PedidoItem pedidoItem = new PedidoItem.Builder()
                .item(item)
                .pedido(this)
                .quantidade(quantidade)
                .valor(valor)
                .build();
        this.itens.add(pedidoItem);
    }

    public BigDecimal getValorTotal() {
        if (Objects.nonNull(this.itens)) {
            return this.itens.stream()
                    .map(item -> item.getValor().multiply(BigDecimal.valueOf(item.getQuantidade())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getValorTotalComDesconto() {
        if (Objects.nonNull(this.desconto)) {
            return getValorTotal().subtract(getValorTotalDesconto());
        }
        return getValorTotal();
    }

    public BigDecimal getValorTotalDesconto() {
        if (Objects.nonNull(this.desconto)) {
            return this.itens.stream()
                    .filter(item -> item.getItem().getTipo().equals(ItemTipo.PRODUTO))
                    .map(item -> item.getValor().multiply(BigDecimal.valueOf(item.getQuantidade())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add).multiply(this.desconto).divide(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }
}
