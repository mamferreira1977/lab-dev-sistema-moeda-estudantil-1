# LAB03S02 - Sistema de Moeda Estudantil

Projeto Spring Boot MVC com MySQL, login e CRUD completo de alunos e empresas parceiras.

## Antes de rodar
1. Abra o MySQL/HeidiSQL.
2. Execute o arquivo `script_mysql.sql`.
3. Confira a senha do MySQL em `src/main/resources/application.properties`.

Se sua senha do MySQL não for `123456`, altere:

```properties
spring.datasource.password=SUA_SENHA
```

## Rodar
```bash
mvn clean spring-boot:run
```

## Acesso
- Login: http://localhost:8081
- Cadastrar aluno: http://localhost:8081/alunos/novo
- Cadastrar empresa: http://localhost:8081/empresas/nova

Após cadastrar aluno ou empresa, o sistema grava:
- dados de acesso na tabela `usuarios`
- dados cadastrais na tabela `alunos` ou `empresas_parceiras`

Depois use o login e senha cadastrados para entrar.
