--liquibase formatted sql

--changeset thiago.julio:1 context:teste,producao
create table PEDIDOS (id uuid DEFAULT gen_random_uuid() primary key, descricao varchar(250), numero integer, situacao integer,criado timestamp,
                      desconto decimal(10,2));

create table ITENS (id uuid DEFAULT gen_random_uuid() primary key, descricao varchar(250),valor decimal(10,2),ativo boolean, tipo integer,validade timestamp);

create table PEDIDO_ITENS (id uuid DEFAULT gen_random_uuid() primary key,id_item uuid REFERENCES ITENS(id) NOT NULL,
                           id_pedido uuid REFERENCES PEDIDOS(id) NOT NULL,quantidade integer,valor decimal(10,2));
--rollback not required
