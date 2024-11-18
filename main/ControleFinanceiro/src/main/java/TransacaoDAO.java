import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class TransacaoDAO {
    private Connection conn;
    public TransacaoDAO(Connection conn) {
        this.conn = conn;
    }
 
    public void inserir(String nome, String telefone) {
        String query = "INSERT INTO td_trancoes (descricao, valor, tdata) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Dados de transação salvos com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir: " + e.getMessage());
        }
    }
 
    public String consultar(String tdate) {
        String query = "SELECT * FROM td_transacoes WHERE nome = ?";
        StringBuilder resultado = new StringBuilder();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tdate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String valor = rs.getString("valor");
                resultado.append("Data: ").append(tdate).append(", Valor: ").append(valor).append("\n");
            }
            if (resultado.length() == 0) {
                resultado.append("Contato não encontrado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar: " + e.getMessage());
        }
        return resultado.toString();
    }
 
    public void alterar(String nome, String telefone) {
        String query = "UPDATE tb_contatos SET telefone = ? WHERE nome = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, telefone);
            stmt.setString(2, nome);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Contato alterado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Contato não encontrado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar: " + e.getMessage());
        }
    }
 
    public void excluir(String nome) {
        String query = "DELETE FROM tb_contatos WHERE nome = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nome);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Contato excluído com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Contato não encontrado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir: " + e.getMessage());
        }
    }
}
