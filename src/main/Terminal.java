package main;

import Connection.ConnectionFactory;
import Controller.Cliente_DAO;
import Controller.Especificacao_DAO;
import Controller.Insumo_DAO;
import Controller.PedidoItem_DAO;
import Controller.Pedido_DAO;
import Controller.Produto_DAO;
import Model.Cliente;
import Model.Especificacao;
import Model.Insumo;
import Model.Pedido;
import Model.PedidoItem;
import Model.Produto;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Terminal {

    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        int op;
        do {
            System.out.println();
            System.out.println("==============================================");
            System.out.println("   CASEIROS DA NISE - TESTES NO TERMINAL");
            System.out.println("==============================================");
            System.out.println("1 - Testar conexão com o banco");
            System.out.println("2 - Clientes");
            System.out.println("3 - Insumos");
            System.out.println("4 - Produtos");
            System.out.println("5 - Receitas (Especificação)");
            System.out.println("6 - Pedidos");
            System.out.println("0 - Sair");
            op = lerInt("Escolha: ");

            switch (op) {
                case 1 -> testarConexao();
                case 2 -> menuClientes();
                case 3 -> menuInsumos();
                case 4 -> menuProdutos();
                case 5 -> menuReceitas();
                case 6 -> menuPedidos();
                case 0 -> System.out.println("Encerrando os testes.");
                default -> System.out.println("Opção inválida.");
            }
        } while (op != 0);
        System.exit(0);
    }

    // ---------------- CONEXÃO ----------------
    private static void testarConexao() {
        try {
            Connection con = ConnectionFactory.getConnection();
            System.out.println(">> Conexão OK!");
            System.out.println("   Banco: " + con.getCatalog());
            System.out.println("   Servidor: " + con.getMetaData().getURL());
            System.out.println("   MySQL versão: " + con.getMetaData().getDatabaseProductVersion());
            ConnectionFactory.closeConnection(con);
        } catch (Exception e) {
            System.out.println(">> Falha na conexão: " + e.getMessage());
        }
    }

    // ---------------- CLIENTES ----------------
    private static void menuClientes() {
        int op;
        do {
            System.out.println();
            System.out.println("--- CLIENTES ---");
            System.out.println("1 - Cadastrar  2 - Listar  3 - Alterar  4 - Excluir  0 - Voltar");
            op = lerInt("Escolha: ");
            Cliente_DAO dao = new Cliente_DAO();

            switch (op) {
                case 1 -> {
                    String nome = lerTexto("Nome: ");
                    String tel = lerTexto("Telefone: ");
                    String canal = lerTexto("Canal (WhatsApp/Instagram/Telefone): ");
                    dao.create(new Cliente(nome, tel, canal));
                    listarClientes();
                }
                case 2 -> listarClientes();
                case 3 -> {
                    listarClientes();
                    Cliente c = dao.consul(lerInt("Id do cliente: "));
                    if (c == null) {
                        System.out.println("Cliente não encontrado.");
                        break;
                    }
                    c.setNome(lerTexto("Nome [" + c.getNome() + "]: ", c.getNome()));
                    c.setTelefone(lerTexto("Telefone [" + c.getTelefone() + "]: ", c.getTelefone()));
                    c.setCanalContato(lerTexto("Canal [" + c.getCanalContato() + "]: ", c.getCanalContato()));
                    dao.update(c);
                    listarClientes();
                }
                case 4 -> {
                    listarClientes();
                    Cliente c = new Cliente();
                    c.setId(lerInt("Id do cliente: "));
                    dao.delete(c);
                    listarClientes();
                }
            }
        } while (op != 0);
    }

    private static void listarClientes() {
        System.out.printf("%-4s %-22s %-16s %-12s%n", "ID", "NOME", "TELEFONE", "CANAL");
        for (Cliente c : new Cliente_DAO().read()) {
            System.out.printf("%-4d %-22s %-16s %-12s%n", c.getId(), c.getNome(), c.getTelefone(), c.getCanalContato());
        }
    }

    // ---------------- INSUMOS ----------------
    private static void menuInsumos() {
        int op;
        do {
            System.out.println();
            System.out.println("--- INSUMOS ---");
            System.out.println("1 - Cadastrar  2 - Listar  3 - Alterar  4 - Excluir  5 - Abaixo do mínimo  0 - Voltar");
            op = lerInt("Escolha: ");
            Insumo_DAO dao = new Insumo_DAO();

            switch (op) {
                case 1 -> {
                    String nome = lerTexto("Nome: ");
                    String unidade = lerTexto("Unidade (g/ml/un): ");
                    double preco = lerDouble("Preço por " + unidade + ": ");
                    double estoque = lerDouble("Estoque: ");
                    double minimo = lerDouble("Estoque mínimo: ");
                    dao.create(new Insumo(nome, unidade, preco, estoque, minimo));
                    listarInsumos();
                }
                case 2 -> listarInsumos();
                case 3 -> {
                    listarInsumos();
                    Insumo i = buscarInsumo(lerInt("Id do insumo: "));
                    if (i == null) {
                        System.out.println("Insumo não encontrado.");
                        break;
                    }
                    i.setPreco(lerDouble("Preço [" + i.getPreco() + "]: ", i.getPreco()));
                    i.setQtdEstoque(lerDouble("Estoque [" + i.getQtdEstoque() + "]: ", i.getQtdEstoque()));
                    i.setQtdMinima(lerDouble("Mínimo [" + i.getQtdMinima() + "]: ", i.getQtdMinima()));
                    dao.update(i);
                    listarInsumos();
                }
                case 4 -> {
                    listarInsumos();
                    Insumo i = new Insumo();
                    i.setId(lerInt("Id do insumo: "));
                    dao.delete(i);
                    listarInsumos();
                }
                case 5 -> {
                    System.out.println("Insumos com estoque abaixo do mínimo:");
                    for (Insumo i : dao.readAbaixoDoMinimo()) {
                        System.out.printf("  %-20s estoque %.2f %s (mínimo %.2f)%n",
                                i.getNome(), i.getQtdEstoque(), i.getUnidade(), i.getQtdMinima());
                    }
                }
            }
        } while (op != 0);
    }

    private static void listarInsumos() {
        System.out.printf("%-4s %-20s %-4s %-10s %-10s %-10s%n", "ID", "NOME", "UN", "PREÇO", "ESTOQUE", "MÍNIMO");
        for (Insumo i : new Insumo_DAO().read()) {
            System.out.printf("%-4d %-20s %-4s %-10.4f %-10.2f %-10.2f%n",
                    i.getId(), i.getNome(), i.getUnidade(), i.getPreco(), i.getQtdEstoque(), i.getQtdMinima());
        }
    }

    private static Insumo buscarInsumo(int id) {
        for (Insumo i : new Insumo_DAO().read()) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    // ---------------- PRODUTOS ----------------
    private static void menuProdutos() {
        int op;
        do {
            System.out.println();
            System.out.println("--- PRODUTOS ---");
            System.out.println("1 - Cadastrar  2 - Listar  3 - Alterar  4 - Excluir  0 - Voltar");
            op = lerInt("Escolha: ");
            Produto_DAO dao = new Produto_DAO();

            switch (op) {
                case 1 -> {
                    String nome = lerTexto("Nome: ");
                    String cat = lerTexto("Categoria (BOLO/PAO/DOCE/SALGADO): ").toUpperCase();
                    int tempo = lerInt("Tempo de preparo (min): ");
                    double margem = lerDouble("Margem (%): ");
                    double preco = lerDouble("Preço de venda: ");
                    dao.create(new Produto(nome, cat, tempo, margem, preco));
                    listarProdutos();
                }
                case 2 -> listarProdutos();
                case 3 -> {
                    listarProdutos();
                    Produto p = dao.consul(lerInt("Id do produto: "));
                    if (p == null) {
                        System.out.println("Produto não encontrado.");
                        break;
                    }
                    p.setNome(lerTexto("Nome [" + p.getNome() + "]: ", p.getNome()));
                    p.setMargem(lerDouble("Margem [" + p.getMargem() + "]: ", p.getMargem()));
                    p.setPrecoVenda(lerDouble("Preço [" + p.getPrecoVenda() + "]: ", p.getPrecoVenda()));
                    dao.update(p);
                    listarProdutos();
                }
                case 4 -> {
                    listarProdutos();
                    Produto p = new Produto();
                    p.setId(lerInt("Id do produto: "));
                    dao.delete(p);
                    listarProdutos();
                }
            }
        } while (op != 0);
    }

    private static void listarProdutos() {
        System.out.printf("%-4s %-22s %-10s %-8s %-8s %-10s%n", "ID", "NOME", "CATEGORIA", "PREPARO", "MARGEM", "PREÇO");
        for (Produto p : new Produto_DAO().read()) {
            System.out.printf("%-4d %-22s %-10s %-8s %-8s R$ %.2f%n", p.getId(), p.getNome(), p.getCategoria(),
                    p.getTempoPreparoMin() + "min", String.format("%.0f%%", p.getMargem()), p.getPrecoVenda());
        }
    }

    // ---------------- RECEITAS ----------------
    private static void menuReceitas() {
        listarProdutos();
        Produto p = new Produto_DAO().consul(lerInt("Id do produto da receita: "));
        if (p == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        int op;
        do {
            System.out.println();
            System.out.println("--- RECEITA: " + p.getNome() + " ---");
            System.out.println("1 - Adicionar insumo  2 - Ver receita e preço  3 - Alterar quantidade  4 - Remover insumo  0 - Voltar");
            op = lerInt("Escolha: ");
            Especificacao_DAO dao = new Especificacao_DAO();

            switch (op) {
                case 1 -> {
                    listarInsumos();
                    int insumoId = lerInt("Id do insumo: ");
                    double qtd = lerDouble("Quantidade: ");
                    dao.create(new Especificacao(p.getId(), insumoId, qtd));
                    listarReceita(p);
                }
                case 2 -> listarReceita(p);
                case 3 -> {
                    listarReceita(p);
                    Especificacao e = new Especificacao();
                    e.setId(lerInt("Id da linha da receita: "));
                    e.setQuantidade(lerDouble("Nova quantidade: "));
                    dao.update(e);
                    listarReceita(p);
                }
                case 4 -> {
                    listarReceita(p);
                    Especificacao e = new Especificacao();
                    e.setId(lerInt("Id da linha da receita: "));
                    dao.delete(e);
                    listarReceita(p);
                }
            }
        } while (op != 0);
    }

    private static void listarReceita(Produto p) {
        Especificacao_DAO dao = new Especificacao_DAO();
        System.out.printf("%-4s %-20s %-10s %-4s %-10s%n", "ID", "INSUMO", "QTD", "UN", "CUSTO");
        for (Especificacao e : dao.read(p.getId())) {
            Insumo i = buscarInsumo(e.getInsumoId());
            System.out.printf("%-4d %-20s %-10.2f %-4s R$ %.2f%n",
                    e.getId(), i.getNome(), e.getQuantidade(), i.getUnidade(), e.getQuantidade() * i.getPreco());
        }
        double custo = dao.calcularCusto(p.getId());
        System.out.printf("Custo dos ingredientes: R$ %.2f%n", custo);
        if (p.getMargem() < 100) {
            double sugerido = dao.calcularPrecoSugerido(p.getId(), p.getMargem());
            System.out.printf("Preço sugerido (margem %.0f%%): R$ %.2f%n", p.getMargem(), sugerido);
            System.out.printf("Preço praticado: R$ %.2f%s%n", p.getPrecoVenda(),
                    p.getPrecoVenda() < sugerido ? "  << ABAIXO DO SUGERIDO" : "  (ok)");
        }
    }

    //PEDIDOS -
    private static void menuPedidos() {
        int op;
        do {
            System.out.println();
            System.out.println("--- PEDIDOS ---");
            System.out.println("1 - Novo pedido  2 - Listar  3 - Adicionar item  4 - Ver itens e total");
            System.out.println("5 - Confirmar pedido  6 - Alterar  7 - Excluir  0 - Voltar");
            op = lerInt("Escolha: ");
            Pedido_DAO dao = new Pedido_DAO();

            switch (op) {
                case 1 -> {
                    listarClientes();
                    int clienteId = lerInt("Id do cliente: ");
                    LocalDate entrega = lerData("Data de entrega (dd/mm/aaaa): ");
                    double pago = lerDouble("Valor pago (sinal): ");
                    dao.create(new Pedido(clienteId, LocalDate.now(), entrega, "ORCAMENTO", pago));
                    listarPedidos();
                }
                case 2 -> listarPedidos();
                case 3 -> {
                    listarPedidos();
                    int pedidoId = lerInt("Id do pedido: ");
                    listarProdutos();
                    int produtoId = lerInt("Id do produto: ");
                    double qtd = lerDouble("Quantidade: ");
                    new PedidoItem_DAO().create(new PedidoItem(pedidoId, produtoId, qtd, 0));
                    listarItens(pedidoId);
                }
                case 4 -> listarItens(lerInt("Id do pedido: "));
                case 5 -> {
                    listarPedidos();
                    Pedido p = buscarPedido(lerInt("Id do pedido: "));
                    if (p == null) {
                        System.out.println("Pedido não encontrado.");
                        break;
                    }
                    dao.confirmar(p);
                    listarPedidos();
                }
                case 6 -> {
                    listarPedidos();
                    Pedido p = buscarPedido(lerInt("Id do pedido: "));
                    if (p == null) {
                        System.out.println("Pedido não encontrado.");
                        break;
                    }
                    p.setDataEntrega(lerData("Entrega [" + p.getDataEntrega().format(BR) + "]: ", p.getDataEntrega()));
                    p.setValorPago(lerDouble("Valor pago [" + p.getValorPago() + "]: ", p.getValorPago()));
                    dao.update(p);
                    listarPedidos();
                }
                case 7 -> {
                    listarPedidos();
                    Pedido p = new Pedido();
                    p.setId(lerInt("Id do pedido: "));
                    dao.delete(p);
                    listarPedidos();
                }
            }
        } while (op != 0);
    }

    private static void listarPedidos() {
        System.out.printf("%-4s %-20s %-11s %-11s %-12s %-10s%n", "ID", "CLIENTE", "PEDIDO", "ENTREGA", "SITUAÇÃO", "PAGO");
        Cliente_DAO cdao = new Cliente_DAO();
        for (Pedido p : new Pedido_DAO().read()) {
            Cliente c = cdao.consul(p.getClienteId());
            System.out.printf("%-4d %-20s %-11s %-11s %-12s R$ %.2f%n", p.getId(), c == null ? "?" : c.getNome(),
                    p.getDataPedido().format(BR), p.getDataEntrega().format(BR), p.getSituacao(), p.getValorPago());
        }
    }

    private static void listarItens(int pedidoId) {
        PedidoItem_DAO dao = new PedidoItem_DAO();
        Produto_DAO pdao = new Produto_DAO();
        System.out.printf("%-4s %-22s %-6s %-12s %-10s%n", "ID", "PRODUTO", "QTD", "PREÇO UNIT.", "SUBTOTAL");
        for (PedidoItem i : dao.read(pedidoId)) {
            Produto p = pdao.consul(i.getProdutoId());
            System.out.printf("%-4d %-22s %-6.0f R$ %-9.2f R$ %.2f%n", i.getId(), p == null ? "?" : p.getNome(),
                    i.getQuantidade(), i.getPrecoPraticado(), i.getQuantidade() * i.getPrecoPraticado());
        }
        System.out.printf("Total do pedido: R$ %.2f%n", dao.totalizar(pedidoId));
    }

    private static Pedido buscarPedido(int id) {
        for (Pedido p : new Pedido_DAO().read()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    // ---------------- LEITURA DO TECLADO ----------------
    private static String lerTexto(String msg) {
        System.out.print(msg);
        return sc.nextLine().trim();
    }

    private static String lerTexto(String msg, String atual) {
        String t = lerTexto(msg);
        return t.isEmpty() ? atual : t;
    }

    private static int lerInt(String msg) {
        while (true) {
            try {
                return Integer.parseInt(lerTexto(msg));
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro.");
            }
        }
    }

    private static double lerDouble(String msg) {
        while (true) {
            try {
                return Double.parseDouble(lerTexto(msg).replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Digite um número.");
            }
        }
    }

    private static double lerDouble(String msg, double atual) {
        String t = lerTexto(msg);
        return t.isEmpty() ? atual : Double.parseDouble(t.replace(",", "."));
    }

    private static LocalDate lerData(String msg) {
        while (true) {
            try {
                return LocalDate.parse(lerTexto(msg), BR);
            } catch (Exception e) {
                System.out.println("Use o formato dd/mm/aaaa.");
            }
        }
    }

    private static LocalDate lerData(String msg, LocalDate atual) {
        String t = lerTexto(msg);
        return t.isEmpty() ? atual : LocalDate.parse(t, BR);
    }
}
