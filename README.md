# ERP Senior
### Requisito

- Java ≅v21
- Apache Maven ≅v3.6
- Docker

### Configuração de Teste

Executar empacotamento do maven e criar imagem no Docker:
1. `mvn clean install -U compile generate-sources`
2. `docker-compose up -d --force-recreate --build`

Iniciar somente o PostgreSQL:
1. `docker-compose up -d postgres`

Parar a execução removendo os volumes e imagens:
1. `docker-compose down --rmi all -v`

### Configuração em Produção

Para baixar a imagem e executar:
1. `docker-compose pull`
2. `docker-compose up -d`

### Estrutura
Para uso da API Rest foi criado requições exemplos no Postman:

[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://www.postman.com/tjulioh/workspace/api-erp-senior/overview)