
package Model;

public class Cliente {
    
    private int id;
    private String nome;
    private String telefone;
    private String canalContato;

    public Cliente() {
    }

    public Cliente(String nome, String telefone, String canalContato) {
        this.nome = nome;
        this.telefone = telefone;
        this.canalContato = canalContato;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCanalContato() {
        return canalContato;
    }

    public void setCanalContato(String canalContato) {
        this.canalContato = canalContato;
    }

    
}
