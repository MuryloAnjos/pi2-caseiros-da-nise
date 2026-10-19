
package Model;

public class Produto {
    
    private int id;
    private String nome;
    private String categoria;
    private int tempoPreparoMin;
    private double margem;
    private double precoVenda;

    public Produto() {
    }

    public Produto(String nome, String categoria, int tempoPreparoMin, double margem, double precoVenda) {
        this.nome = nome;
        this.categoria = categoria;
        this.tempoPreparoMin = tempoPreparoMin;
        this.margem = margem;
        this.precoVenda = precoVenda;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getTempoPreparoMin() {
        return tempoPreparoMin;
    }

    public void setTempoPreparoMin(int tempoPreparoMin) {
        this.tempoPreparoMin = tempoPreparoMin;
    }

    public double getMargem() {
        return margem;
    }

    public void setMargem(double margem) {
        this.margem = margem;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    
}
