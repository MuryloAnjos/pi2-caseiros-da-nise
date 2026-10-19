package Connection;

import java.sql.Connection;

public class TesteConexao {
    
    public static void main (String[] args){
        try {
            Connection con = ConnectionFactory.getConnection();
            System.out.println("Conectou no banco: " + con.getCatalog());
            ConnectionFactory.closeConnection(con);
            
        }catch (Exception e){
             System.out.println("Erro: " + e.getMessage());
             e.printStackTrace();
        }
        
    }

}
