## lab-dev-sistema-moeda-estudantil-release 01
### Pretende-se desenvolver um sistema para estimular o reconhecimento do mérito estudantil através de uma moeda virtual
#### Modelagem do Sistema
---

<strong>1-Histórias de Usuário:<strong>
#### Autenticar o usuário
#### História: Como usuário do sistema, quero realizar login com e-mail e senha, para acessar apenas as funcionalidades permitidas ao meu perfil.
#### Critérios de aceite:
#### 1.	O sistema deve permitir login para aluno, professor e empresa parceira.
#### 2.	O sistema deve validar e-mail e senha antes de liberar o acesso.
#### 3.	O sistema deve identificar o perfil do usuário autenticado.
#### 4.	Caso os dados estejam incorretos, o sistema deve exibir mensagem de erro.

#### Cadastrar aluno
#### História: Como aluno, quero realizar meu cadastro no sistema de mérito estudantil, para participar do programa de moedas.
#### Critérios de aceite:
#### 5.	O cadastro deve solicitar nome, e-mail, CPF, RG, endereço, instituição de ensino e curso.
#### 6.	A instituição de ensino deve ser selecionada a partir das instituições pré-cadastradas.
#### 7.	O sistema deve impedir cadastro sem os campos obrigatórios.
#### 8.	Após cadastro válido, o aluno deve conseguir acessar o sistema com login e senha.

#### Cadastrar empresa parceira
#### História: Como empresa parceira, quero realizar meu cadastro no sistema, para oferecer vantagens aos alunos.
#### Critérios de aceite:
#### 9.	O cadastro deve solicitar os dados básicos da empresa e credenciais de acesso.
#### 10.O sistema deve impedir cadastro sem campos obrigatórios.
#### 11. Após cadastro válido, a empresa deve conseguir acessar a área de gestão de vantagens.

#### Cadastrar vantagem
#### História: Como empresa parceira, quero cadastrar vantagens com descrição, custo e foto, para permitir que alunos resgatem benefícios.
#### Critérios de aceite:
#### 12.	A empresa deve informar descrição da vantagem.
#### 13.	A empresa deve informar o custo em moedas.
#### 14.	A empresa deve adicionar foto do produto ou benefício.
#### 15.	A vantagem cadastrada deve ficar disponível para consulta pelos alunos.

#### Consultar vantagens disponíveis
#### História: Como aluno, quero consultar as vantagens cadastradas, para decidir onde utilizar minhas moedas.
#### Critérios de aceite:
#### 16.	O sistema deve listar as vantagens disponíveis.
#### 17.	O aluno deve conseguir visualizar descrição, custo em moedas e imagem da vantagem.
#### 18.	O sistema deve permitir a seleção de uma vantagem para resgate.

#### Enviar moedas ao aluno
#### História: Como professor, quero enviar moedas a um aluno, para reconhecer mérito, participação ou bom comportamento.
#### Critérios de aceite:
#### 19.	O professor deve estar autenticado.
#### 20.	O professor deve selecionar o aluno destinatário.
#### 21.	O professor deve informar a quantidade de moedas.
#### 22.	O professor deve informar justificativa obrigatória.
#### 23.	O sistema deve validar se o professor possui saldo suficiente.
#### 24.	Após o envio, o saldo do professor deve ser reduzido e o saldo do aluno aumentado.

#### Receber notificação de moedas
#### História: Como aluno, quero ser notificado por e-mail quando receber moedas, para saber que fui reconhecido pelo professor.
#### Critérios de aceite:
#### 25.	Após o envio de moedas, o sistema deve enviar e-mail ao aluno.
#### 26.	O e-mail deve informar o recebimento das moedas.
#### 27.	O sistema deve registrar a transação no extrato do aluno e do professor.

#### Consultar saldo
#### História: Como aluno, professor ou empresa parceira, quero consultar meu saldo de moedas, para acompanhar minha situação no sistema.
#### Critérios de aceite:
#### 28.	O usuário deve estar autenticado.
#### 29.	Aluno deve visualizar o total de moedas disponíveis.
#### 30.	Professor deve visualizar o saldo disponível para distribuição.
#### 31.	O saldo exibido deve refletir envios, recebimentos e resgates realizados.

#### Consultar extrato
#### História: Como aluno ou professor, quero consultar meu extrato de transações, para acompanhar movimentações de moedas.
#### Critérios de aceite:
#### 32.	O sistema deve exibir as transações realizadas pelo usuário.
#### 33.	Para professor, o extrato deve apresentar envios de moedas.
#### 34.	Para aluno, o extrato deve apresentar recebimentos e trocas de moedas.
#### 35.	Cada transação deve conter data, valor, tipo e descrição/motivo quando aplicável.

#### Receber moedas sem perder saldo anterior
#### História: Como professor, quero receber 1.000 moedas a cada semestre de forma acumulável, para continuar distribuindo moedas aos alunos.
#### Critérios de aceite:
#### 36.	A cada semestre, o sistema deve acrescentar 1.000 moedas ao saldo do professor.
#### 37.	Moedas não utilizadas no semestre anterior não devem ser perdidas.
#### 38.	O saldo atualizado deve estar disponível para consulta.

#### Resgatar vantagem
#### História: Como aluno, quero trocar moedas por uma vantagem cadastrada, para utilizar benefícios oferecidos por empresas parceiras.
#### Critérios de aceite:
#### 39.	O aluno deve estar autenticado.
#### 40.	O aluno deve selecionar uma vantagem disponível.
#### 41.	O sistema deve validar se o aluno possui saldo suficiente.
#### 42.	Após confirmação, o sistema deve descontar o custo da vantagem do saldo do aluno.
#### 43.	O sistema deve registrar a transação no extrato.

#### Gerar cupom de resgate
#### História: Como aluno, quero receber um cupom com código gerado pelo sistema, para utilizar a vantagem presencialmente.
#### Critérios de aceite:
#### 44.	Após o resgate, o sistema deve gerar um código único.
#### 45.	O código deve constar no e-mail enviado ao aluno.
#### 46.	O código deve permitir a conferência da troca pela empresa parceira.

#### Notificar empresa parceira sobre resgate
#### História: Como empresa parceira, quero receber e-mail quando um aluno resgatar uma vantagem, para conferir a troca presencialmente.
#### Critérios de aceite:
#### 47.	Após o resgate, o sistema deve enviar e-mail à empresa parceira.
#### 48.	O e-mail deve conter o código gerado pelo sistema.
#### 49.	O e-mail deve conter informações suficientes para conferência da vantagem resgatada.

---
#### 2- Diagrama de Casos de Uso
<img width="1536" height="1024" alt="diagrama de caso de uso" src="https://github.com/user-attachments/assets/8eeeac4d-70a9-48ef-be4a-3e6592c8289d" />

---
#### 3- Diagrama de Classes
<img width="934" height="943" alt="diagrama_classes_moeda_estudantil" src="https://github.com/user-attachments/assets/4633ced6-6845-4530-a0a9-433acb1ee0de" />

---
### 4-Diagrama de Componentes

<img width="1536" height="1024" alt="diagrama_de_componentes" src="https://github.com/user-attachments/assets/08e4d13a-c6b0-412d-b077-7f348e7ea494" />

---
### 5 - Arquitetura 

### O Sistema foi desenvolvido em arquitetura MVC em camadas. Houve a separação entre front end , regras de negócio e persistência.

<img width="517" height="423" alt="image" src="https://github.com/user-attachments/assets/cb5eeb1f-93f6-4aa5-abc1-ca37bc830197" />

### Justificativa da arquitetura : MVC melhora organização do código, JPA reduz complexidade de persistência, Hibernate automatiza ORM,Spring Boot acelera desenvolvimento
---
### 6 Tecnologias Utilizadas
### 6.1. Front end: HTML , CSS E JS 
### 6.2. Backend : Spring Boot, Spring Data JPA, Hibernate, Java 17
### 6.3. Banco de Dados: Mysql
---
### 7. Visão Geral das Camadas

###  Camada de Apresentação (Views/Thymeleaf): Arquivos HTML localizados em templates, responsável pela interação com professor e aluno
###  Telas de login, cadastro, envio de moedas e vantagens.
###  Camada Controller: Encaminha dados para Services, Controla fluxo entre frontend e backend,Exemplos: AuthController, AlunoController, ProfessorController.
### Camada Service: Processamento de envio de moedas, Controle de saldo.
### Camada Repository: Implementada com Spring Data JPA, Responsável pelo acesso ao banco de dados.
### Camada de Persistência (MySQL) : Entidades representam tabelas do banco e Hibernate gerencia sincronização Java/MySQL. Banco MySQL configurado no application.properties
### Uso de JDBC , spring.jpa.hibernate.ddl-auto=update.
---
### 8. Modelos de Entidades
###  Principais entidades:
### Usuario
### Aluno
### Professor
### TransacaoMoeda
### Vantagem e Resgate
### EmailSimulado
---
### 9. Diagramas de Sequencias Geral
<img width="3600" height="2260" alt="Diagrama_UML_Sequencia" src="https://github.com/user-attachments/assets/d0de2a7b-64f6-4512-817a-ad4bfb05b29f" />

---
### 10. Diagrama de Sequências por caso de uso

### 10.1 Autenticar usuário

<img width="1800" height="900" alt="UC01_Autenticar_Login" src="https://github.com/user-attachments/assets/073d9aff-dc8a-4795-9a16-489a05e42a32" />

### 10.2 Cadastrar no sistema

<img width="1800" height="900" alt="UC04_Aluno_Cadastrar_Se" src="https://github.com/user-attachments/assets/eecc6d94-4b0b-45ee-a082-365cfbf77312" />

### 10.3 Consultar Saldo de Moedas

<img width="1800" height="900" alt="UC05_Aluno_Consultar_Saldo" src="https://github.com/user-attachments/assets/afc341cd-5e80-474c-912c-4251bd3ecd28" />

### 10.4 Consultar Extrato de Transação

<img width="1800" height="900" alt="UC06_Aluno_Consultar_Extrato" src="https://github.com/user-attachments/assets/b93b74b8-f558-48e7-af9c-e6a0b045154f" />

### 10.5 Trocar moedas por vantagens

<img width="1800" height="900" alt="UC07_Aluno_Trocar_Moedas_Por_Vantagens" src="https://github.com/user-attachments/assets/863c6053-6f59-4e5e-943e-1da2fd76d3fd" />

### 10.6  Consultar Saldo de Moedas

<img width="1800" height="900" alt="UC14_Professor_Consultar_Saldo" src="https://github.com/user-attachments/assets/17fa721a-aeec-41f1-b5e1-72be70d37504" />

### 10.7 Consultar Extrato de Transação

<img width="1800" height="900" alt="UC15_Professor_Consultar_Extrato" src="https://github.com/user-attachments/assets/77c713b3-3c2a-4c7b-978a-c875efb36992" />

### 10.8 Enviar moedas para aluno

<img width="1800" height="900" alt="UC16_Professor_Enviar_Moedas" src="https://github.com/user-attachments/assets/886ba59b-c124-448e-a987-f916e336317c" />

### 10.9 Cadastrar no sistema
<img width="1800" height="900" alt="UC21_Empresa_Cadastrar_Se" src="https://github.com/user-attachments/assets/07bc8f78-593e-4e55-8674-a7605d32bb4d" />

### 10.10 Cadastrar / Gerenciar Vantagens
<img width="1800" height="900" alt="UC22_Cadastrar_Gerenciar_Vantagens" src="https://github.com/user-attachments/assets/b7996cab-4c11-40eb-984d-30e904231da1" />

### 10.11 Consultar Trocas de Cupons
<img width="1800" height="900" alt="UC23_Consultar_Trocas_Cupons" src="https://github.com/user-attachments/assets/dc1b4a54-eef9-43bf-bf10-1c8a4bb7fafc" />











