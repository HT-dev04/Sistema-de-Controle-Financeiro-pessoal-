//Esse ainda ta com erro, não ta salvando no banco de dados. Vou postar quando conseguir resolver

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO {
    private Connection conn;

    public TransacaoDAO(Connection conn) {
        this.conn = conn;
    }

    public void inserirTransacao(String descricao, double valor, String tipo, String dataInicial, String dataFinal) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO tb_dados (datainicial, datafinal, descricao, valor, tipo) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, dataInicial);
            stmt.setString(2, dataFinal);
            stmt.setString(3, descricao);
            stmt.setDouble(4, valor);
            stmt.setString(5, tipo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir transação: " + e.getMessage());
        }
    }

    public void editarTransacao(int id, String descricao, double valor, String tipo, String dataInicial, String dataFinal) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "UPDATE tb_dados SET datainicial = ?, datafinal = ?, descricao = ?, valor = ?, tipo = ? WHERE id = ?")) {
            stmt.setString(1, dataInicial);
            stmt.setString(2, dataFinal);
            stmt.setString(3, descricao);
            stmt.setDouble(4, valor);
            stmt.setString(5, tipo);
            stmt.setInt(6, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao editar transação: " + e.getMessage());
        }
    }

    public void excluirTransacao(int id) {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM tb_dados WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao excluir transação: " + e.getMessage());
        }
    }

    public List<String[]> consultarTransacoes() {
        List<String[]> transacoes = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tb_dados")) {
            while (rs.next()) {
                transacoes.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("datainicial"),
                        rs.getString("datafinal"),
                        rs.getString("descricao"),
                        String.valueOf(rs.getDouble("valor")),
                        rs.getString("tipo")
                });
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar transações: " + e.getMessage());
        }
        return transacoes;
    }
}
