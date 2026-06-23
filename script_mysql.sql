DROP DATABASE IF EXISTS moeda_estudantil;
CREATE DATABASE moeda_estudantil CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE moeda_estudantil;

CREATE TABLE usuarios (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  login VARCHAR(100) NOT NULL UNIQUE,
  senha VARCHAR(100) NOT NULL,
  perfil VARCHAR(20) NOT NULL,
  ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE alunos (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(120) NOT NULL,
  email VARCHAR(120) NOT NULL UNIQUE,
  cpf VARCHAR(20) NOT NULL,
  rg VARCHAR(30),
  endereco VARCHAR(180),
  instituicao_ensino VARCHAR(120),
  curso VARCHAR(120),
  saldo_moedas INT NOT NULL DEFAULT 0,
  usuario_id BIGINT NOT NULL,
  CONSTRAINT fk_aluno_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE empresas_parceiras (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  razao_social VARCHAR(150) NOT NULL,
  nome_fantasia VARCHAR(150),
  cnpj VARCHAR(30) NOT NULL UNIQUE,
  email VARCHAR(120) NOT NULL UNIQUE,
  telefone VARCHAR(30),
  endereco VARCHAR(180),
  descricao_vantagem VARCHAR(255),
  custo_moedas INT,
  usuario_id BIGINT NOT NULL,
  CONSTRAINT fk_empresa_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE professores (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(120) NOT NULL,
  cpf VARCHAR(20) NOT NULL UNIQUE,
  email VARCHAR(120) NOT NULL UNIQUE,
  departamento VARCHAR(120) NOT NULL,
  instituicao VARCHAR(120) NOT NULL DEFAULT 'PUC Minas',
  instituicao_id INT NOT NULL DEFAULT 1,
  instituicao_nome VARCHAR(120) NOT NULL DEFAULT 'PUC Minas',
  saldo_moedas INT NOT NULL DEFAULT 1000,
  usuario_id BIGINT NOT NULL,
  CONSTRAINT fk_professor_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE transacoes_moedas (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  tipo VARCHAR(40) NOT NULL,
  quantidade INT NOT NULL,
  motivo VARCHAR(500) NOT NULL,
  data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  professor_id BIGINT,
  aluno_id BIGINT,
  vantagem_id BIGINT
);

CREATE TABLE emails_simulados (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  remetente VARCHAR(160),
  destinatario VARCHAR(160) NOT NULL,
  assunto VARCHAR(160) NOT NULL,
  mensagem VARCHAR(2000) NOT NULL,
  status_envio VARCHAR(40) NOT NULL DEFAULT 'REGISTRADO',
  erro_envio VARCHAR(1000),
  data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- LAB05S01 - campos para cupom com QR Code único
ALTER TABLE resgates ADD COLUMN IF NOT EXISTS codigo_verificacao VARCHAR(120) UNIQUE;
ALTER TABLE resgates ADD COLUMN IF NOT EXISTS qr_code_base64 LONGTEXT;
