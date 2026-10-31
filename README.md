# Caseiros da Nise

Sistema de gestão para a confeitaria caseira da Ivanise (Nise), em Guaianases/SP. Ele controla clientes, insumos, produtos, receitas e pedidos.

Projeto Integrador do 2º semestre de Sistemas de Informação do Senac Santo Amaro.
Equipe B: Murylo Anjos, Caio Gonçalves e Michel Vitor.

## O que precisa ter instalado

- **JDK 21**
- **Apache NetBeans** (testado na versão 31)
- **MySQL 8** (ou MariaDB)

As bibliotecas (driver do MySQL e edisoncorSX) já estão na pasta `lib/`. Não precisa baixar nada.

## Passo a passo

### 1. Clonar o repositório

```bash
git clone https://github.com/MuryloAnjos/pi2-caseiros-da-nise.git
```

### 2. Criar o banco de dados

O script fica em `banco/caseiros_da_nise.sql`. Ele cria o banco `caseiros_da_nise`, as 6 tabelas e alguns dados de exemplo.

**Pelo MySQL Workbench:** abra o arquivo (File → Open SQL Script) e execute tudo (ícone do raio).

**Pelo terminal:**

```bash
mysql -u root -p < banco/caseiros_da_nise.sql
```

> O script começa com `DROP DATABASE`. Se você já tem o banco com dados seus, rodar de novo apaga tudo.

### 3. Usuário e senha do banco

O sistema usa por padrão o usuário `root` com a senha `root` no MySQL local.

Se o seu MySQL usa outro usuário ou outra senha:

1. Copie o arquivo `banco.properties.exemplo` para `banco.properties`, na raiz do projeto.
2. Coloque os seus dados nele:

```properties
url=jdbc:mysql://localhost:3306/caseiros_da_nise
usuario=seu_usuario
senha=sua_senha
```

O `banco.properties` está no `.gitignore`, então a sua senha não vai para o GitHub.

### 4. Abrir e rodar

1. No NetBeans: **File → Open Project** e escolha a pasta do projeto.
2. Aperte **F6** (ou clique no play verde).

Abre a tela de carregamento e depois a tela inicial do sistema.

### 5. Testes no terminal (opcional)

Para testar a conexão e o CRUD sem as telas:

1. Abra **Source Packages → main**.
2. Clique com o botão direito em **Terminal.java** → **Run File**.
3. Use o menu que aparece na aba **Output**.

## Estrutura do projeto

```
src/
├─ Connection   conexão com o banco (ConnectionFactory)
├─ Controller   DAOs: inserção, consulta, alteração, exclusão e regras de negócio
├─ Model        classes das tabelas
├─ View         telas Swing
├─ Images       imagens das telas
└─ main         Main (abre o sistema) e Terminal (testes)
banco/          script do banco de dados
lib/            bibliotecas .jar
```

## Problemas comuns

**"Erro na Conexão" ao abrir o sistema**
- Confira se o MySQL está ligado.
- Confira se o banco foi criado (passo 2).
- Confira se o usuário e a senha batem (passo 3).

**No Linux: `ERROR 1698 Access denied for user 'root'@'localhost'`**

O root do MySQL vem configurado para entrar sem senha, só pelo `sudo`. Para colocar a senha `root`:

```bash
sudo mysql
```

```sql
ALTER USER 'root'@'localhost' IDENTIFIED WITH caching_sha2_password BY 'root';
FLUSH PRIVILEGES;
EXIT;
```

**Erro de biblioteca não encontrada no NetBeans**

Clique com o botão direito no projeto → **Resolve Problems** e aponte para os `.jar` da pasta `lib/`.
