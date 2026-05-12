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
