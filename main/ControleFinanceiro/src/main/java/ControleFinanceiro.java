import javax.swing.*;
import java.sql.Connection;
 
public class ControleFinanceiro {
    public static void main(String[] args) {
        Connection conn = null;
 
        // conecta com o banco de dados
        ConectaBanco cb = new ConectaBanco("jdbc:mysql://localhost:xxxx/db_trabalho", "root", "", "com.mysql.cj.jdbc.Driver");
        conn = cb.getConnection();
 
        if (conn != null) {
            TransacaoDAO transacaoDAO = new TransacaoDAO(conn);
            new ProjectUI(transacaoDAO);
        } else {
            JOptionPane.showMessageDialog(null, "Falha ao conectar ao banco de dados.");
        }
    }
}
