package dev.tjulioh.erpsenior.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "ITENS")
public class Item extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private UUID id;

    @NotBlank(message = "A descrição do produto é obrigatório")
    @Size(min = 2, max = 100)
    private String descricao;

    @NotNull(message = "O valor do item é obrigatório.")
    @Positive
    private BigDecimal valor;

    @NotNull(message = "O item é obrigatório")
    private ItemTipo tipo;

    @NotNull(message = "A situacao é obrigatório")
    private Boolean ativo;

    public Item() {
    }

    public Item(Boolean ativo, String descricao, UUID id, ItemTipo tipo, BigDecimal valor) {
        this.ativo = ativo;
        this.descricao = descricao;
        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
    }

    private Item(Builder builder) {
        this.ativo = builder.ativo;
        this.descricao = builder.descricao;
        this.id = builder.id;
        this.tipo = builder.tipo;
        this.valor = builder.valor;
    }

    public static class Builder {
        private Boolean ativo;
        private String descricao;
        private UUID id;
        private ItemTipo tipo;
        private BigDecimal valor;

        public Builder ativo(Boolean ativo) {
            this.ativo = ativo;
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

        public Builder tipo(ItemTipo tipo) {
            this.tipo = tipo;
            return this;
        }

        public Builder valor(BigDecimal valor) {
            this.valor = valor;
            return this;
        }

        public Item build() {
            Item item = new Item(ativo, descricao, id, tipo, valor);
            validate(item);
            return item;
        }

        public Builder from(Item item) {
            this.ativo = item.getAtivo();
            this.descricao = item.getDescricao();
            this.id = item.getId();
            this.tipo = item.getTipo();
            this.valor = item.getValor();
            return this;
        }

        public void validate(Item item) {
            verificaExisteAlternativa(item);
        }

        private void verificaExisteAlternativa(Item item) {
            if (Objects.isNull(item.getTipo())) {
                throw new ValidationException("Deve existir um tipo definido");
            }
        }
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
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

    public ItemTipo getTipo() {
        return tipo;
    }

    public void setTipo(ItemTipo tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
