CREATE DATABASE IF NOT EXISTS Byteloja;
USE Byteloja;

CREATE TABLE categoria (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    descricao VARCHAR(200),
    PRIMARY KEY (id),
    CONSTRAINT UK_nome_categoria UNIQUE (nome)
);

CREATE TABLE produto (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    estoque INT NOT NULL,
    categoria_id BIGINT NOT NULL,
    imagem VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_categoria_produto FOREIGN KEY (categoria_id) REFERENCES categoria (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT CK_produto_preco CHECK (preco >= 0.01),
    CONSTRAINT CK_produto_estoque CHECK (estoque >= 0)
);

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE produto;
TRUNCATE TABLE categoria;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO categoria (id, nome, descricao) VALUES
(1, 'RPG', 'Jogos de interpretacao de personagens com narrativas epicas.'),
(2, 'Acao e Aventura', 'Combate intenso e exploracao de mundos abertos.'),
(3, 'FPS / Tiro', 'Jogos de tiro em primeira pessoa com acao intensa.'),
(4, 'Estrategia', 'Planejamento tatico e gerenciamento de recursos.');

INSERT INTO produto (id, nome, descricao, preco, estoque, categoria_id, imagem) VALUES
(1, 'The Witcher 3: Wild Hunt', 'Um dos maiores RPGs ja criados. Explore o Continente como Geralt de Rivia, um cacador de monstros profissional.', 59.90, 15, 1, NULL),
(2, 'God of War', 'Kratos e seu filho Atreus embarcam em uma jornada epica pela mitologia nordica cheia de desafios.', 149.90, 8, 2, NULL),
(3, 'Counter-Strike 2', 'O classico FPS competitivo renovado. Jogue com amigos em partidas online intensas e ranqueadas.', 0.01, 999, 3, NULL),
(4, 'Civilization VI', 'Construa um imperio que resistira ao teste do tempo. Lide com diplomacia, ciencia, guerra e exploracao.', 79.90, 20, 4, NULL),
(5, 'Elden Ring', 'Explore as Terras Intermedias em um RPG de acao desafiador criado por FromSoftware e George R.R. Martin.', 199.90, 0, 1, NULL);

SELECT * FROM categoria;

SELECT 
    p.id AS 'ID Jogo',
    p.nome AS 'Nome do Jogo',
    p.preco AS 'Preço (R$)',
    p.estoque AS 'Estoque',
    c.nome AS 'Categoria / Gênero'
FROM produto p
INNER JOIN categoria c ON p.categoria_id = c.id
ORDER BY p.id;
