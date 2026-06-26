# 🏷️ Desenvolvimento do Sistema Moeda Estudantil 👨‍💻
# Pretende-se desenvolver um sistema para estimular o reconhecimento do mérito estudantil através de uma moeda virtual.
---
## 🚧 Status do Projeto
![Java](https://img.shields.io/badge/Java-17-red?logo=openjdk) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?logo=springboot)![Spring Security](https://img.shields.io/badge/Spring_Security-Enabled-6DB33F?logo=springsecurity) ![JPA](https://img.shields.io/badge/Spring_Data_JPA-Enabled-6DB33F) ![Hibernate](https://img.shields.io/badge/Hibernate-59666C?logo=hibernate) ![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql)![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Message_Broker-FF6600?logo=rabbitmq) ![Docker](https://img.shields.io/badge/Docker-Container-2496ED?logo=docker) ![SMTP](https://img.shields.io/badge/SMTP-Brevo-0092FF) ![REST API](https://img.shields.io/badge/REST_API-JSON-005571) ![HTML](https://img.shields.io/badge/HTML5-E34F26?logo=html5) ![CSS](https://img.shields.io/badge/CSS3-1572B6?logo=css3) ![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?logo=javascript&logoColor=black) 
![Bootstrap](https://img.shields.io/badge/Bootstrap-5-7952B3?logo=bootstrap) ![IntelliJ](https://img.shields.io/badge/IntelliJ_IDEA-000000?logo=intellijidea) ![VS Code](https://img.shields.io/badge/VS_Code-007ACC?logo=visualstudiocode) ![Git](https://img.shields.io/badge/Git-F05032?logo=git) ![GitHub](https://img.shields.io/badge/GitHub-181717?logo=github)![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven)
![Status](https://img.shields.io/badge/Status-Production_Ready-brightgreen?style=for-the-badge)
![Version](https://img.shields.io/badge/Version-v3.0-0052CC?style=for-the-badge)

---
## 📚 Índice
- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades Principais](#-funcionalidades-principais)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura)
- [Documentação](#-diagramas)
- [Deploy](#-deploy)
- [Estrutura de Pastas](#-estrutura-de-pastas)
- [Demonstração](#-demonstração)
  - [Aplicativo Mobile](#-aplicativo-mobile)
  - [Aplicação Web](#-aplicação-web)
  - [Exemplo de saída no Terminal (para Back-end, API, CLI)](#-exemplo-de-saída-no-terminal-para-back-end-api-cli)
- [Testes](#-testes)
- [Documentações utilizadas](#-documentações-utilizadas)
- [Autores](#-autores)
- [Contribuição](#-contribuição)
- [Agradecimentos](#-agradecimentos)
- [Licença](#-licença)

---

## 📝 Sobre o Projeto
 Sistema web desenvolvido em Java/Spring Boot para gerenciamento de moedas estudantis, permitindo que professores reconheçam o desempenho de alunos por meio da distribuição de moedas virtuais, que posteriormente podem ser trocadas por vantagens oferecidas por empresas parceiras.
 
---

## ✨ Funcionalidades Principais
Liste as funcionalidades de forma clara e objetiva.

- 🔐 **Autenticação Segura:** Login, Cadastro.  
- ⚙️ **Gerenciamento de CRUD:** Criação, Leitura, Atualização e Deleção de recursos (e.g., Usuários, Itens, Posts).  
- 📨 **Sistema de Notificações:** Envio de alertas por e-mail, push ou notificações internas.

---
## 🛠 Tecnologias Utilizadas

As seguintes ferramentas, frameworks e bibliotecas foram utilizados na construção deste projeto. Recomenda-se o uso das versões listadas (ou superiores) para garantir a compatibilidade.

### 💻 Front-end

* **Framework/Biblioteca:** [Ex: Bootstrap]
* **Linguagem/Superset:** [Ex: JavaScript ES6+]
* **Estilização:** [Ex:CSS]


### 🖥️ Back-end

* **Linguagem/Runtime:** [Ex: Java 17 (JDK)]
* **Framework:** [Ex: Spring Boot 3.x]
* **Banco de Dados:** [Ex: MySQL]
* **ORM / Query Builder:** [Ex: Hibernate/JPA]
* **Autenticação:** [Ex: Spring Security]

### ⚙️ Infraestrutura & DevOps

* **Containerização:** [Ex: Docker]
* **Cloud:** [Ex: Vercel]

---
## 🏗 Arquitetura
# 🎓 Sistema de Moeda Estudantil

Sistema web desenvolvido em Java/Spring Boot para gerenciamento de moedas estudantis, permitindo que professores reconheçam o desempenho de alunos por meio da distribuição de moedas virtuais, que posteriormente podem ser trocadas por vantagens oferecidas por empresas parceiras.

---

# Arquitetura da Solução

A aplicação foi construída utilizando arquitetura em camadas (MVC), microsserviços de mensageria e serviços externos em nuvem.

```
                        Internet
                            │
                            │
                     Render (Cloud)
                  Spring Boot Application
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   ▼                   ▼
 FreeSQLDatabase      CloudAMQP          Brevo SMTP
     MySQL             RabbitMQ         Envio de e-mails
```

Todo o processamento da aplicação ocorre no Render.

Os dados são persistidos em um banco MySQL remoto.

O envio de mensagens é desacoplado através do RabbitMQ.

As notificações são enviadas utilizando SMTP do Brevo.

---

# Arquitetura de Software

O sistema utiliza arquitetura MVC composta pelas seguintes camadas:

## Camada de Apresentação

- HTML5
- CSS3
- JavaScript
- Thymeleaf

Responsável pela interação com o usuário.

---

## Camada de Controle

Controllers Spring Boot responsáveis por:

- Receber requisições HTTP
- Validar dados
- Encaminhar chamadas aos Services

---

## Camada de Negócio

Implementada através dos Services.

Responsável por:

- Cadastro de usuários
- Cadastro de alunos
- Cadastro de professores
- Cadastro de empresas
- Distribuição de moedas
- Resgate de vantagens
- Publicação de mensagens RabbitMQ
- Envio de e-mails

---

## Camada de Persistência

Implementada utilizando:

- Spring Data JPA
- Hibernate

Realiza toda a comunicação com o banco de dados MySQL.

---


## Banco de Dados

MySQL 8

Hospedado em:

FreeSQLDatabase

---

## Mensageria

RabbitMQ

Hospedado em:

CloudAMQP

---

## Serviço de E-mail

Brevo SMTP

Responsável pelo envio automático das notificações para:

- Professor
- Aluno

---

## Deploy

Aplicação hospedada em:🌐 **Deploy:** https://lab-dev-sistema-moeda-estudantil.onrender.com/login

Render

Deploy automatizado através do GitHub.

A aplicação encontra-se publicada no Render.

Durante o deploy foram utilizados serviços externos para:

- Banco de dados MySQL
- RabbitMQ
- SMTP

Todos configurados através de variáveis de ambiente

---

# Diagramas / Documentação do Sistema

## Histórias de usuário
## HU01 - Autenticar usuário
História: Como usuário do sistema, quero realizar login com e-mail e senha, para acessar apenas as funcionalidades permitidas ao meu perfil.
Critérios de aceite:
1.	O sistema deve permitir login para aluno, professor e empresa parceira.
2.	O sistema deve validar e-mail e senha antes de liberar o acesso.
3.	O sistema deve identificar o perfil do usuário autenticado.
4.	Caso os dados estejam incorretos, o sistema deve exibir mensagem de erro.
## HU02 - Cadastrar aluno
História: Como aluno, quero realizar meu cadastro no sistema de mérito estudantil, para participar do programa de moedas.
Critérios de aceite:
5.	O cadastro deve solicitar nome, e-mail, CPF, RG, endereço, instituição de ensino e curso.
6.	A instituição de ensino deve ser selecionada a partir das instituições pré-cadastradas.
7.	O sistema deve impedir cadastro sem os campos obrigatórios.
8.	Após cadastro válido, o aluno deve conseguir acessar o sistema com login e senha.
## HU03 - Cadastrar empresa parceira
História: Como empresa parceira, quero realizar meu cadastro no sistema, para oferecer vantagens aos alunos.
Critérios de aceite:
9.	O cadastro deve solicitar os dados básicos da empresa e credenciais de acesso.
10.	O sistema deve impedir cadastro sem campos obrigatórios.
11.	Após cadastro válido, a empresa deve conseguir acessar a área de gestão de vantagens.
## HU04 - Cadastrar vantagem
História: Como empresa parceira, quero cadastrar vantagens com descrição, custo e foto, para permitir que alunos resgatem benefícios.
Critérios de aceite:
12.	A empresa deve informar descrição da vantagem.
13.	A empresa deve informar o custo em moedas.
14.	A empresa deve adicionar foto do produto ou benefício.
15.	A vantagem cadastrada deve ficar disponível para consulta pelos alunos.
## HU05 - Consultar vantagens disponíveis
História: Como aluno, quero consultar as vantagens cadastradas, para decidir onde utilizar minhas moedas.
Critérios de aceite:
16.	O sistema deve listar as vantagens disponíveis.
17.	O aluno deve conseguir visualizar descrição, custo em moedas e imagem da vantagem.
18.	O sistema deve permitir a seleção de uma vantagem para resgate.
## HU06 - Enviar moedas ao aluno
História: Como professor, quero enviar moedas a um aluno, para reconhecer mérito, participação ou bom comportamento.
Critérios de aceite:
19.	O professor deve estar autenticado.
20.	O professor deve selecionar o aluno destinatário.
21.	O professor deve informar a quantidade de moedas.
22.	O professor deve informar justificativa obrigatória.
23.	O sistema deve validar se o professor possui saldo suficiente.
24.	Após o envio, o saldo do professor deve ser reduzido e o saldo do aluno aumentado.
## HU07 - Receber notificação de moedas
História: Como aluno, quero ser notificado por e-mail quando receber moedas, para saber que fui reconhecido pelo professor.
Critérios de aceite:
25.	Após o envio de moedas, o sistema deve enviar e-mail ao aluno.
26.	O e-mail deve informar o recebimento das moedas.
27.	O sistema deve registrar a transação no extrato do aluno e do professor.
## HU08 - Consultar saldo
História: Como aluno, professor ou empresa parceira, quero consultar meu saldo de moedas, para acompanhar minha situação no sistema.
Critérios de aceite:
28.	O usuário deve estar autenticado.
29.	Aluno deve visualizar o total de moedas disponíveis.
30.	Professor deve visualizar o saldo disponível para distribuição.
31.	O saldo exibido deve refletir envios, recebimentos e resgates realizados.
## HU09 - Consultar extrato
História: Como aluno ou professor, quero consultar meu extrato de transações, para acompanhar movimentações de moedas.
Critérios de aceite:
32.	O sistema deve exibir as transações realizadas pelo usuário.
33.	Para professor, o extrato deve apresentar envios de moedas.
34.	Para aluno, o extrato deve apresentar recebimentos e trocas de moedas.
35.	Cada transação deve conter data, valor, tipo e descrição/motivo quando aplicável.
## HU10 - Receber moedas sem perder saldo anterior
História: Como professor, quero receber 1.000 moedas a cada semestre de forma acumulável, para continuar distribuindo moedas aos alunos.
Critérios de aceite:
36.	A cada semestre, o sistema deve acrescentar 1.000 moedas ao saldo do professor.
37.	Moedas não utilizadas no semestre anterior não devem ser perdidas.
38.	O saldo atualizado deve estar disponível para consulta.
## HU11 - Resgatar vantagem
História: Como aluno, quero trocar moedas por uma vantagem cadastrada, para utilizar benefícios oferecidos por empresas parceiras.
Critérios de aceite:
39.	O aluno deve estar autenticado.
40.	O aluno deve selecionar uma vantagem disponível.
41.	O sistema deve validar se o aluno possui saldo suficiente.
42.	Após confirmação, o sistema deve descontar o custo da vantagem do saldo do aluno.
43.	O sistema deve registrar a transação no extrato.
## HU12 - Gerar cupom de resgate
História: Como aluno, quero receber um cupom com código gerado pelo sistema, para utilizar a vantagem presencialmente.
Critérios de aceite:
44.	Após o resgate, o sistema deve gerar um código único.
45.	O código deve constar no e-mail enviado ao aluno.
46.	O código deve permitir a conferência da troca pela empresa parceira.
## HU13 - Notificar empresa parceira sobre resgate
História: Como empresa parceira, quero receber e-mail quando um aluno resgatar uma vantagem, para conferir a troca presencialmente.
Critérios de aceite:
47.	Após o resgate, o sistema deve enviar e-mail à empresa parceira.
48.	O e-mail deve conter o código gerado pelo sistema.
49.	O e-mail deve conter informações suficientes para conferência da vantagem resgatada.

## Diagrama de Caso de Uso 
<img width="1536" height="1024" alt="diagrama de caso de uso" src="https://github.com/user-attachments/assets/d1f31eb7-ca97-4119-b8b8-fdb9f42c6092" />

## Diagrama de Classe 
<img width="934" height="943" alt="diagrama_classes_moeda_estudantil" src="https://github.com/user-attachments/assets/b90b6bdf-3a7d-49cd-970d-3a9a2bafeeb5" />

## Diagrama de Componentes

<img width="1536" height="1024" alt="diagrama_de_componentes" src="https://github.com/user-attachments/assets/a575d6da-fc3e-4023-9808-a18a57cd759b" />

## Modelo ER
<img width="1808" height="870" alt="MER versao_final" src="https://github.com/user-attachments/assets/1b19ebdf-ae78-4c4a-971a-18ee182145ea" />

## Diagrama de Sequência 
<img width="3600" height="2260" alt="Diagrama_UML_Sequencia" src="https://github.com/user-attachments/assets/29c15ab4-00e5-4809-b1ca-df808127d27d" />

## Diagrama de Comunicação

<img width="1536" height="1024" alt="Diagrama de Comunicacao" src="https://github.com/user-attachments/assets/7f8cfb5b-c324-4ef4-8e39-781c61d184b4" />

# Diagrama de Implantação
<img width="1536" height="1024" alt="DiagramadeImplantação" src="https://github.com/user-attachments/assets/39287325-da84-4626-813f-ad8a6efa8851" />






