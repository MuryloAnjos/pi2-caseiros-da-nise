
package Model;

public class Insumo {
    
    private int id;
    private String nome;
    private String unidade;
    private double preco;
    private double qtdEstoque;
    private double qtdMinima;

    public Insumo() {
    }

    public Insumo(String nome, String unidade, double preco, double qtdEstoque, double qtdMinima) {
        this.nome = nome;
        this.unidade = unidade;
        this.preco = preco;
        this.qtdEstoque = qtdEstoque;
        this.qtdMinima = qtdMinima;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getQtdEstoque() {
        return qtdEstoque;
    }

    public void setQtdEstoque(double qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    public double getQtdMinima() {
        return qtdMinima;
    }

    public void setQtdMinima(double qtdMinima) {
        this.qtdMinima = qtdMinima;
    }

    
}
