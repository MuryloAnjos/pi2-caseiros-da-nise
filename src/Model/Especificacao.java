
package Model;

public class Especificacao {
    
    private int id;
    private int produtoId;
    private int insumoId;
    private double quantidade;

    public Especificacao() {
    }

    public Especificacao(int produtoId, int insumoId, double quantidade) {
        this.produtoId = produtoId;
        this.insumoId = insumoId;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public int getInsumoId() {
        return insumoId;
    }

    public void setInsumoId(int insumoId) {
        this.insumoId = insumoId;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    
}
