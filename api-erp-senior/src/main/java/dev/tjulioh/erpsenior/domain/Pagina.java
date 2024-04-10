package dev.tjulioh.erpsenior.domain;

import jakarta.validation.ValidationException;

import java.util.List;

public class Pagina<T> {

    private Boolean proximo;
    private Long atual;
    private Long limite;
    private Long total;
    private List<T> conteudo;

    public Pagina() {
    }

    public Pagina(Boolean proximo, Long atual, Long limite, Long total, List<T> conteudo) {
        this.proximo = proximo;
        this.atual = atual;
        this.limite = limite;
        this.total = total;
        this.conteudo = conteudo;
    }

    private Pagina(Builder<T> builder) {
        this.proximo = builder.proximo;
        this.atual = builder.atual;
        this.limite = builder.limite;
        this.total = builder.total;
        this.conteudo = builder.conteudo;
    }

    public static <T> Pagina<T> empty() {
        return new Pagina<>(false, 0L, 0L, 0L, null);
    }

    public static class Builder<T> {
        private Boolean proximo;
        private Long atual;
        private Long limite;
        private Long total;
        private List<T> conteudo;

        public Builder<T> proximo(Boolean proximo) {
            this.proximo = proximo;
            return this;
        }

        public Builder<T> atual(Long atual) {
            this.atual = atual;
            return this;
        }

        public Builder<T> limite(Long limite) {
            this.limite = limite;
            return this;
        }

        public Builder<T> total(Long total) {
            this.total = total;
            return this;
        }

        public Builder<T> conteudo(List<T> conteudo) {
            this.conteudo = conteudo;
            return this;
        }

        public Pagina<T> build() {
            Pagina<T> p = new Pagina<>(this);
            validate(p);
            return p;
        }

        public void validate(Pagina<T> pagina) {
            verificaTotalMenorQueZero(pagina);
        }

        private void verificaTotalMenorQueZero(Pagina<T> pagina) {
            if (pagina.getTotal() < 0) {
                throw new ValidationException("O total nao pode ser menor que ZERO");
            }
        }
    }

    public Boolean getProximo() {
        return proximo;
    }

    public Long getAtual() {
        return atual;
    }

    public Long getLimite() {
        return limite;
    }

    public Long getTotal() {
        return total;
    }

    public List<T> getConteudo() {
        return conteudo;
    }

    public void setProximo(Boolean proximo) {
        this.proximo = proximo;
    }

    public void setAtual(Long atual) {
        this.atual = atual;
    }

    public void setLimite(Long limite) {
        this.limite = limite;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public void setConteudo(List<T> conteudo) {
        this.conteudo = conteudo;
    }
}