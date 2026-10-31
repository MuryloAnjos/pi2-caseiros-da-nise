DROP DATABASE IF EXISTS caseiros_da_nise;
CREATE DATABASE caseiros_da_nise;
USE caseiros_da_nise;

CREATE TABLE cliente (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(120) NOT NULL,
  telefone VARCHAR(20),
  canal_contato VARCHAR(20)
);

CREATE TABLE insumo (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(120) NOT NULL UNIQUE,
  unidade VARCHAR(5) NOT NULL,
  preco DECIMAL(12,6) NOT NULL,
  qtd_estoque DECIMAL(12,3) NOT NULL DEFAULT 0,
  qtd_minima DECIMAL(12,3) NOT NULL DEFAULT 0
);

CREATE TABLE produto (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(120) NOT NULL UNIQUE,
  categoria VARCHAR(20) NOT NULL,
  tempo_preparo_min INT,
  margem DECIMAL(6,2) NOT NULL,
  preco_venda DECIMAL(10,2) NOT NULL
);

CREATE TABLE especificacao (
  id INT AUTO_INCREMENT PRIMARY KEY,
  produto_id INT NOT NULL,
  insumo_id INT NOT NULL,
  quantidade DECIMAL(12,3) NOT NULL,
  FOREIGN KEY (produto_id) REFERENCES produto(id),
  FOREIGN KEY (insumo_id) REFERENCES insumo(id),
  UNIQUE (produto_id, insumo_id)
);

CREATE TABLE pedido (
  id INT AUTO_INCREMENT PRIMARY KEY,
  cliente_id INT NOT NULL,
  data_pedido DATE NOT NULL,
  data_entrega DATE NOT NULL,
  situacao VARCHAR(20) NOT NULL DEFAULT 'ORCAMENTO',
  valor_pago DECIMAL(10,2) NOT NULL DEFAULT 0,
  FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE pedido_item (
  id INT AUTO_INCREMENT PRIMARY KEY,
  pedido_id INT NOT NULL,
  produto_id INT NOT NULL,
  quantidade DECIMAL(12,3) NOT NULL,
  preco_praticado DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (pedido_id) REFERENCES pedido(id),
  FOREIGN KEY (produto_id) REFERENCES produto(id)
);

INSERT INTO cliente (nome, telefone, canal_contato) VALUES
('Ana Prado', '(11) 98876-4412', 'WhatsApp'),
('Marta Alves', '(11) 97654-1122', 'Indicação'),
('Júlio Meireles', '(11) 96543-8890', 'Instagram');

INSERT INTO insumo (nome, unidade, preco, qtd_estoque, qtd_minima) VALUES
('Farinha de trigo', 'g', 0.011600, 2000, 1000),
('Chocolate', 'g', 0.034000, 1500, 1000),
('Ovo', 'un', 0.900000, 30, 24);

INSERT INTO produto (nome, categoria, tempo_preparo_min, margem, preco_venda) VALUES
('Bolo de chocolate', 'BOLO', 120, 60.00, 85.00),
('Bolo de cenoura', 'BOLO', 90, 60.00, 70.00),
('Pão caseiro', 'PAO', 150, 60.00, 18.00);

INSERT INTO especificacao (produto_id, insumo_id, quantidade) VALUES
(1, 1, 250),
(1, 2, 250),
(1, 3, 5);

INSERT INTO pedido (cliente_id, data_pedido, data_entrega, situacao, valor_pago) VALUES
(1, '2026-10-14', '2026-10-17', 'ENTREGUE', 85.00),
(2, '2026-10-16', '2026-10-22', 'CONFIRMADO', 35.00),
(3, '2026-10-18', '2026-10-23', 'ORCAMENTO', 0.00);

INSERT INTO pedido_item (pedido_id, produto_id, quantidade, preco_praticado) VALUES
(1, 1, 1, 85.00),
(2, 2, 1, 70.00),
(3, 3, 2, 18.00);
