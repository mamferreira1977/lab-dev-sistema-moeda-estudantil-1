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

Aplicação hospedada em:

Render

Deploy automatizado através do GitHub.

A aplicação encontra-se publicada no Render.

Durante o deploy foram utilizados serviços externos para:

- Banco de dados MySQL
- RabbitMQ
- SMTP

Todos configurados através de variáveis de ambiente








