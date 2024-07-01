package dev.tjulioh.erpsenior.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "PEDIDO_ITENS")
public class PedidoItem extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private UUID id;

    @NotNull(message = "O item é obrigatório")
    @OneToOne
    @JoinColumn(name = "id_item")
    private Item item;

    @NotNull(message = "O pedido é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    @JsonBackReference
    private Pedido pedido;

    @NotNull(message = "A quantidade é obrigatória.")
    private Integer quantidade;

    @NotNull(message = "O valor é obrigatório.")
    @Positive
    private BigDecimal valor;

    public PedidoItem() {
    }

    public PedidoItem(UUID id, Item item, Pedido pedido, Integer quantidade, BigDecimal valor) {
        this.id = id;
        this.item = item;
        this.pedido = pedido;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    private PedidoItem(Builder builder) {
        this.id = builder.id;
        this.item = builder.item;
        this.pedido = builder.pedido;
        this.quantidade = builder.quantidade;
        this.valor = builder.valor;
    }

    public static class Builder {
        private UUID id;
        private Item item;
        private Pedido pedido;
        private Integer quantidade;
        private BigDecimal valor;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder item(Item item) {
            this.item = item;
            return this;
        }

        public Builder pedido(Pedido pedido) {
            this.pedido = pedido;
            return this;
        }

        public Builder quantidade(Integer quantidade) {
            this.quantidade = quantidade;
            return this;
        }

        public Builder valor(BigDecimal valor) {
            this.valor = valor;
            return this;
        }

        public PedidoItem build() {
            PedidoItem pedidoItem = new PedidoItem(this);
            validate(pedidoItem);
            return pedidoItem;

        }

        public Builder from(PedidoItem pedidoItem) {
            this.id = pedidoItem.id;
            this.item = pedidoItem.item;
            this.pedido = pedidoItem.pedido;
            this.quantidade = pedidoItem.quantidade;
            this.valor = pedidoItem.valor;
            return this;
        }

        public void validate(PedidoItem pedidoItem) {
            naoPermitirItemDesativado(pedidoItem);
        }

        private void naoPermitirItemDesativado(PedidoItem pedidoItem) {
            if (Objects.nonNull(pedidoItem.getItem()) && pedidoItem.getItem().getAtivo().equals(false)) {
                throw new ValidationException("O item não está pode estar DESATIVADO");
            }
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
