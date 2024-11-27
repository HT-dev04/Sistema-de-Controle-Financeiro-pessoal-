import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TransacaoDAO {
    private Connection conn;

    public TransacaoDAO(Connection conn) {
        this.conn = conn;
    }

    // Inserir uma nova transação no banco de dados
    public void inserirTransacao(String descricao, double valor, String tipo, String dataInicial, String dataFinal) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO tb_dados (datainicial, datafinal, descricao, valor, tipo) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, dataInicial);
            stmt.setString(2, dataFinal);
            stmt.setString(3, descricao);
            stmt.setDouble(4, valor);
            stmt.setString(5, tipo);
            stmt.executeUpdate();
            System.out.println("Transação inserida com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir transação: " + e.getMessage());
        }
    }

    // Editar uma transação existente no banco de dados
    public void editarTransacao(int id, String descricao, double valor, String tipo, String dataInicial, String dataFinal) {
        String sql = "UPDATE tb_dados SET descricao = ?, valor = ?, tipo = ?, datainicial = ?, datafinal = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, descricao);
            stmt.setDouble(2, valor);
            stmt.setString(3, tipo);
            stmt.setString(4, dataInicial);
            stmt.setString(5, dataFinal);
            stmt.setInt(6, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transação atualizada com sucesso!");
            } else {
                System.out.println("Nenhuma transação encontrada com o ID especificado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Excluir uma transação do banco de dados
    public void excluirTransacao(String descricao, double valor, String tipo, String dataInicial, String dataFinal) {
    String sql = "DELETE FROM tb_dados WHERE descricao = ? AND valor = ? AND tipo = ? AND datainicial = ? AND datafinal = ?";
    
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, descricao);
        stmt.setDouble(2, valor);
        stmt.setString(3, tipo);
        stmt.setString(4, dataInicial);
        stmt.setString(5, dataFinal);

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Transação excluída do banco de dados com sucesso.");
        } else {
            System.out.println("Nenhuma transação encontrada com os dados especificados.");
        }
    } catch (SQLException e) {
        System.out.println("Erro ao excluir transação: " + e.getMessage());
    }
}

    // Consultar todas as transações no banco de dados e atualizar a tabela da interface
    public void consultarTransacoes(DefaultTableModel modeloTabela) {
        String sql = "SELECT id, DATE_FORMAT(datainicial, '%d/%m/%y') AS datainicial, DATE_FORMAT(datafinal, '%d/%m/%y') AS datafinal, descricao, valor, tipo FROM tb_dados";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Limpa os dados da tabela na interface
            modeloTabela.setRowCount(0);

            // Adiciona cada linha de resultado no modelo da tabela
            while (rs.next()) {
                modeloTabela.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("descricao"),
                    rs.getDouble("valor"),
                    rs.getString("tipo"),
                    rs.getString("datainicial"),
                    rs.getString("datafinal")
                });
            }
            System.out.println("Consulta concluída e tabela atualizada!");
        } catch (SQLException e) {
            System.out.println("Erro ao consultar transações: " + e.getMessage());
        }
    }

    // Consultar uma transação específica pelo ID e exibir na interface
    public String[] consultarTransacao(int id) {
        String sql = "SELECT id, descricao, valor, tipo, DATE_FORMAT(datainicial, '%d/%m/%y') AS datainicial, DATE_FORMAT(datafinal, '%d/%m/%y') AS datafinal FROM tb_dados WHERE id = ?";
        String[] transacao = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    transacao = new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("descricao"),
                        String.valueOf(rs.getDouble("valor")),
                        rs.getString("tipo"),
                        rs.getString("datainicial"),
                        rs.getString("datafinal")
                    };
                } else {
                    System.out.println("Transação não encontrada para o ID especificado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transacao;
    }
}
