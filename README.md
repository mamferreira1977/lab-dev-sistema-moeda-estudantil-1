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

## Complementação de requisitos do PDF

Foram acrescentados módulos sem substituir o CSS original e sem remover os CRUDs existentes de alunos e empresas:

- `/professores`: cadastro, edição, exclusão, consulta e saldo de professores.
- `/professores/creditar-semestre`: crédito acumulável de 1.000 moedas por semestre para todos os professores.
- `/moedas/enviar`: envio de moedas do professor ao aluno com validação de saldo e motivo obrigatório.
- `/moedas/extrato-professor/{id}`: extrato de moedas enviadas pelo professor.
- `/moedas/extrato-aluno/{id}`: extrato de moedas recebidas e resgatadas pelo aluno.
- `/vantagens`: cadastro de vantagens com empresa parceira, descrição, foto por URL e custo em moedas.
- `/resgates`: resgate de vantagens com desconto do saldo do aluno.
- `/resgates/cupom/{id}`: cupom com código único para conferência presencial.
- `/emails-simulados`: consulta dos e-mails simulados enviados para aluno e empresa parceira.

Login inicial criado automaticamente se não houver professores cadastrados:

- usuário: `professor`
- senha: `123456`

O banco continua sendo atualizado via JPA/Hibernate com `spring.jpa.hibernate.ddl-auto=update`.

## Correção Lab04S01 - Professor e notificações

Esta versão corrige o cadastro de professor e o envio de notificações do Lab04S01:

- O login padrão do professor é o e-mail informado no cadastro.
- O campo de instituição do professor é salvo como texto, corrigindo o erro `Incorrect integer value: 'PUCMINAS' for column 'instituicao_id'`.
- Ao enviar moedas, o aluno recebe uma notificação e o professor também recebe a confirmação do envio.
- As notificações ficam registradas no menu de e-mails do sistema, mesmo sem SMTP externo configurado.
- Para envio real por SMTP, preencha `spring.mail.username` e `spring.mail.password` no arquivo `application.properties`. Sem essas credenciais, a transação não é bloqueada.

Caso o banco antigo já tenha sido criado com a coluna `instituicao_id` como inteiro, a aplicação ajusta automaticamente essa coluna na inicialização.

## Ajustes Lab04S01 nesta versão

- Ao fazer login como **aluno**, o sistema redireciona automaticamente para o **extrato do aluno**, exibindo saldo de moedas e transações.
- Ao fazer login como **professor**, o sistema redireciona para a tela de **envio de moedas**.
- Ao distribuir moedas, o sistema grava a transação, atualiza o saldo do professor e do aluno e gera duas notificações: uma para o aluno e outra para o professor.
- As notificações ficam registradas no banco na tabela `emails_simulados`.
- Para envio real por e-mail, configure no ambiente:

```properties
APP_EMAIL_ENVIO_REAL=true
MAIL_USERNAME=seu_email@gmail.com
MAIL_PASSWORD=sua_senha_de_app_do_gmail
```

Sem essa configuração, o sistema não falha: ele registra as notificações no banco para demonstração do Lab04S01.

## Correção Lab04S01 - e-mail real e card de extrato do aluno

Nesta versão, o envio de moedas chama o `EmailService` para enviar e-mail real ao aluno e ao professor. O sistema também registra a notificação na tabela `emails_simulados` para auditoria.

Para o envio real funcionar, configure uma conta SMTP. Exemplo usando Gmail:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=seu_email@gmail.com
spring.mail.password=sua_senha_de_app_do_gmail
app.email.envio-real=true
```

Também é possível configurar por variáveis de ambiente:

```bash
MAIL_USERNAME=seu_email@gmail.com
MAIL_PASSWORD=sua_senha_de_app_do_gmail
APP_EMAIL_ENVIO_REAL=true
```

A senha deve ser senha de app do Gmail, não a senha normal da conta Google. Se o SMTP não estiver configurado, o sistema exibirá erro na tela de envio de moedas em vez de informar sucesso falso.

O extrato do aluno agora possui cards com: saldo de moedas, total recebido e total utilizado em trocas.

## Correção atual - SMTP genérico, sem restrição de Gmail

Nesta versão o envio real de e-mails foi refeito para funcionar com **qualquer servidor SMTP válido**, não apenas Gmail. O sistema pode enviar para destinatários de qualquer domínio, desde que o e-mail do professor e do aluno esteja cadastrado corretamente.

Configure no arquivo `src/main/resources/application.properties` ou por variáveis de ambiente:

```properties
APP_EMAIL_ENVIO_REAL=true
MAIL_HOST=smtp.seuprovedor.com
MAIL_PORT=587
MAIL_USERNAME=usuario@seudominio.com
MAIL_PASSWORD=sua_senha_smtp
MAIL_FROM=usuario@seudominio.com
MAIL_STARTTLS_ENABLE=true
MAIL_SSL_ENABLE=false
MAIL_SMTP_AUTH=true
```

Exemplos de provedores:

```properties
# Outlook/Hotmail
MAIL_HOST=smtp.office365.com
MAIL_PORT=587
MAIL_STARTTLS_ENABLE=true
MAIL_SSL_ENABLE=false

# Yahoo
MAIL_HOST=smtp.mail.yahoo.com
MAIL_PORT=587
MAIL_STARTTLS_ENABLE=true
MAIL_SSL_ENABLE=false

# Servidor institucional/empresa/hospedagem
MAIL_HOST=mail.seudominio.com
MAIL_PORT=587
MAIL_STARTTLS_ENABLE=true
MAIL_SSL_ENABLE=false
```

Observação: para envio real, sempre será necessário um **servidor SMTP remetente**. O sistema não limita o domínio, mas o provedor usado precisa permitir autenticação SMTP.


## LAB04S01 - envio automático de e-mails

Nesta versão, o professor não precisa configurar ou digitar e-mails na tela de envio de moedas.

Ao enviar moedas:

1. o sistema identifica o professor selecionado/logado;
2. identifica o aluno selecionado;
3. atualiza o saldo dos dois;
4. registra a transação no extrato;
5. envia automaticamente um e-mail para o aluno informando quantidade recebida e saldo disponível;
6. envia automaticamente um e-mail para o professor informando quantidade enviada e saldo disponível.

Não há restrição para Gmail. O aluno e o professor podem ter qualquer e-mail válido.

Observação técnica: para o envio real sair para a internet, o sistema precisa de um servidor SMTP remetente. Isso não é uma limitação do código; é uma exigência dos provedores de e-mail para evitar spam. O SMTP pode ser de qualquer provedor: Gmail, Outlook/Hotmail, Yahoo, institucional, Locaweb, Hostinger etc.

Configuração do remetente do sistema em `src/main/resources/application.properties`:

```properties
APP_EMAIL_ENVIO_REAL=true
MAIL_HOST=smtp.office365.com
MAIL_PORT=587
MAIL_USERNAME=email_remetente_do_sistema@dominio.com
MAIL_PASSWORD=senha_do_email_ou_senha_de_app
MAIL_FROM=email_remetente_do_sistema@dominio.com
```

O destinatário é automático: o sistema usa `aluno.email` e `professor.email` salvos no banco.

## Correção importante do envio real de e-mail

Nesta versão, o sistema **não usa mais o e-mail do aluno/professor como servidor SMTP**.

Fluxo correto:

- `aluno.email` = destinatário da notificação do aluno;
- `professor.email` = destinatário da confirmação do professor;
- `MAIL_USERNAME` / `MAIL_PASSWORD` = conta remetente do sistema;
- `MAIL_HOST=auto` = o sistema tenta detectar automaticamente o SMTP pelo domínio do remetente.

Exemplo para remetente Hotmail/Outlook:

```properties
MAIL_USERNAME=seu_email@hotmail.com
MAIL_PASSWORD=sua_senha_ou_token_smtp
MAIL_HOST=auto
MAIL_PORT=587
APP_EMAIL_ENVIO_REAL=true
```

Atenção: `MAIL_HOST` **não pode ser preenchido com e-mail**. Não use `MAIL_HOST=marcomferreira1977@hotmail.com`. Se quiser informar manualmente, use:

```properties
MAIL_HOST=smtp.office365.com
MAIL_PORT=587
```
