--liquibase formatted sql

--changeset thiago.julio:1 context:teste,producao
INSERT INTO public.pedidos (id, descricao, numero, situacao, criado, desconto)
VALUES ('68a3234e-3cc3-4e0d-a495-9589366da749', 'PEDIDO SEM DESCONTO', 1, 0, '2024-04-05 09:11:35.379093',
        null);
INSERT INTO public.pedidos (id, descricao, numero, situacao, criado, desconto)
VALUES ('2be6266f-c0c2-4124-b26a-0b8cc6340b48', 'PEDIDO COM DESCONTO', 2, 0, '2024-04-05 09:11:35.379093',
        10.0);
INSERT INTO public.pedidos (id, descricao, numero, situacao, criado, desconto)
VALUES ('bb11e1a1-2f9e-4d4a-85e2-5b23512fe309', 'PEDIDO ITEM INATIVO', 3, 0, '2024-04-05 09:11:35.379093',
        null);
INSERT INTO public.pedidos (id, descricao, numero, situacao, criado, desconto)
VALUES ('ebb5db5c-6ca4-467d-9c38-85ad2fd09d8d', 'PEDIDO FECHADO', 4, 1, '2024-04-05 09:11:35.379093',
        null);
INSERT INTO public.pedidos (id, descricao, numero, situacao, criado, desconto)
VALUES ('5c93df1a-c74d-4f53-b4ad-97c11fed1d9a', 'PEDIDO 1', 4, 1, '2024-04-05 09:11:35.379093',
        null);
INSERT INTO public.pedidos (id, descricao, numero, situacao, criado, desconto)
VALUES ('b196f6c3-1618-4b79-8bee-73820cebaec8', 'PEDIDO 2', 4, 1, '2024-04-05 09:11:35.379093',
        null);
INSERT INTO public.pedidos (id, descricao, numero, situacao, criado, desconto)
VALUES ('1d78ce6c-8e2d-4adf-a35a-597587b51f7e', 'PEDIDO 3', 4, 1, '2024-04-05 09:11:35.379093',
        null);
INSERT INTO public.pedidos (id, descricao, numero, situacao, criado, desconto)
VALUES ('51757b06-1ab4-455b-a3b0-dfbf670c4607', 'PEDIDO 4', 4, 1, '2024-04-05 09:11:35.379093',
        null);
INSERT INTO public.pedidos (id, descricao, numero, situacao, criado, desconto)
VALUES ('c2c1ddeb-54ae-4b5b-8f4e-d181aec15ad2', 'PEDIDO 5', 4, 1, '2024-04-05 09:11:35.379093',
        null);
INSERT INTO public.pedidos (id, descricao, numero, situacao, criado, desconto)
VALUES ('61df4e01-bb30-433c-98c1-5ebaa2abb781', 'PEDIDO 6', 4, 1, '2024-04-05 09:11:35.379093',
        null);
INSERT INTO public.pedidos (id, descricao, numero, situacao, criado, desconto)
VALUES ('d574258a-538b-4b45-9b38-4629bfe3ce57', 'PEDIDO 7', 4, 1, '2024-04-05 09:11:35.379093',
        null);
--rollback not required

--changeset thiago.julio:2 context:teste,producao
INSERT INTO public.itens (id, descricao, valor, ativo, tipo)
VALUES ('7d03ff2d-4a2a-4150-8138-92bd718302ee', 'LIVRO CLEAN CODE', 59.99, true, 1);
INSERT INTO public.itens (id, descricao, valor, ativo, tipo)
VALUES ('f74a1378-1982-403f-a758-6b563003727d', 'LIVRO GO HORSE', 77.77, false, 1);
INSERT INTO public.itens (id, descricao, valor, ativo, tipo)
VALUES ('ee50d252-f96f-42a0-b23d-fb3eb4ba3081', 'SERVICO DE IA', 299.99, true, 0);
--rollback not required

--changeset thiago.julio:3 context:teste,producao
INSERT INTO public.pedido_itens (id, id_item, id_pedido, quantidade, valor)
VALUES ('9a5a872c-572f-4883-afbb-5eddd4ba9e6b', '7d03ff2d-4a2a-4150-8138-92bd718302ee',
        '68a3234e-3cc3-4e0d-a495-9589366da749', 5, 59.99);
INSERT INTO public.pedido_itens (id, id_item, id_pedido, quantidade, valor)
VALUES ('9c652a96-d6e7-41aa-97a8-be6b7da6b141', 'f74a1378-1982-403f-a758-6b563003727d',
        '2be6266f-c0c2-4124-b26a-0b8cc6340b48', 10, 77.77);
INSERT INTO public.pedido_itens (id, id_item, id_pedido, quantidade, valor)
VALUES ('3a248232-3815-4848-baec-ab3a09785068', 'ee50d252-f96f-42a0-b23d-fb3eb4ba3081',
        '2be6266f-c0c2-4124-b26a-0b8cc6340b48', 1, 299.99);
--rollback not required


