
package Model;

public class PedidoItem {
    
    private int id;
    private int pedidoId;
    private int produtoId;
    private double quantidade;
    private double precoPraticado;

    public PedidoItem() {
    }

    public PedidoItem(int pedidoId, int produtoId, double quantidade, double precoPraticado) {
        this.pedidoId = pedidoId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoPraticado = precoPraticado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoPraticado() {
        return precoPraticado;
    }

    public void setPrecoPraticado(double precoPraticado) {
        this.precoPraticado = precoPraticado;
    }

    
}
