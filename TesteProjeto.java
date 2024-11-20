package com.mycompany.testeprojeto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
 
public class TesteProjeto {
     public static void main(String[] args) {
        // Criar a janela
        JFrame janela = new JFrame("Tabela de Controle Financeiro");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(400, 300); // Tamanho ajustado para acomodar a JTable
        janela.setResizable(false); // Não permite que a janela seja redimensionada
 
        // Colocar a janela no centro da tela
        janela.setLocationRelativeTo(null);
 
        // Painel para o campo de Descrição
        JPanel painelDescricao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelDescricao = new JLabel("Descrição:");
        JTextField campoDescricao = new JTextField(20); // Define o tamanho do campo
        painelDescricao.add(labelDescricao);
        painelDescricao.add(campoDescricao);
 
        // Painel para o campo de Data Inicial
        JPanel painelData = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelData = new JLabel("Data Inicial:");
        JTextField campoData = new JTextField(20); // Define o tamanho do campo
        painelData.add(labelData);
        painelData.add(campoData);
        
        // Painel para o campo de Tipo de Déposito
        JPanel painelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelTipo = new JLabel("Tipo:");
        painelTipo.add(labelTipo);
        JButton botaoReceita = new JButton("Receita");
        painelTipo.add(botaoReceita);
        JButton botaoDespesa = new JButton("Despesa");
        painelTipo.add(botaoDespesa);
 
        // Painel para os botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botaoConsultar = new JButton("Consultar");
        JButton botaoAlterar = new JButton("Editar");
        JButton botaoInserir = new JButton("Inserir");
        JButton botaoExcluir = new JButton("Excluir");
        painelBotoes.add(botaoInserir);
        painelBotoes.add(botaoAlterar);
        painelBotoes.add(botaoConsultar);
        painelBotoes.add(botaoExcluir);
 
        // Painel principal para colocar os outros paineis
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        
        // Adicionar os painéis ao painel principal
        painelPrincipal.add(painelDescricao);
        painelPrincipal.add(Box.createVerticalStrut(5));
        painelPrincipal.add(painelTipo);
        painelPrincipal.add(Box.createVerticalStrut(5)); // Espaço reduzido entre os campos
        painelPrincipal.add(painelData);
        painelPrincipal.add(Box.createVerticalStrut(10)); // Espaço entre o telefone e os botões
        painelPrincipal.add(painelBotoes);
 
        // Exibir as informações
        String[] nomesColunas = {"Data Inicial", "Data Final", "Descrição", "Renda", "Despesa"};
        DefaultTableModel modeloTabela = new DefaultTableModel(nomesColunas, 0);
        JTable tabelaFinanca = new JTable(modeloTabela);
        JScrollPane painelRolagemTabela = new JScrollPane(tabelaFinanca);
        
        // Adicionando a JTable ao painel principal
        painelPrincipal.add(painelRolagemTabela);
 
        // Adicionar o painel principal na janela
        janela.add(painelPrincipal);
 
        // Mostrar a janela
        janela.setVisible(true);
 
        // Colocando ações nos botões
        botaoInserir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = campoDescricao.getText();
                String telefone = campoData.getText();
                // Lógica para inserir o contato na tabela
                if (!nome.isEmpty() && !telefone.isEmpty()) {
                    modeloTabela.addRow(new Object[]{nome, telefone});
                    campoDescricao.setText(""); // Limpa o campo após inserção
                    campoData.setText(""); // Limpa o campo após inserção
                } else {
                    JOptionPane.showMessageDialog(janela, "Preencha todos os campos.");
                }
            }
        });
 
        // Evento para carregar dados da JTable nos campos de texto ao clicar
        tabelaFinanca.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabelaFinanca.getSelectedRow();
                if (linha != -1) {
                    campoDescricao.setText(modeloTabela.getValueAt(linha, 0).toString());
                    campoData.setText(modeloTabela.getValueAt(linha, 1).toString());
                }
            }
        });
 
        // Ação para alterar contato
        botaoAlterar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linha = tabelaFinanca.getSelectedRow();
                if (linha != -1) {
                    String nome = campoDescricao.getText();
                    String telefone = campoData.getText();
                    if (!nome.isEmpty() && !telefone.isEmpty()) {
                        // Atualiza a linha selecionada com os novos dados
                        modeloTabela.setValueAt(nome, linha, 0);
                        modeloTabela.setValueAt(telefone, linha, 1);
                        campoDescricao.setText("");
                        campoData.setText("");
                    } else {
                        JOptionPane.showMessageDialog(janela, "Preencha todos os campos.");
                    }
                } else {
                    JOptionPane.showMessageDialog(janela, "Selecione um contato para alterar.");
                }
            }
        });
 
        botaoExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linha = tabelaFinanca.getSelectedRow();
                if (linha != -1) {
                    modeloTabela.removeRow(linha);
                    campoDescricao.setText("");
                    campoData.setText("");
                } else {
                    JOptionPane.showMessageDialog(janela, "Selecione um contato para excluir.");
                }
            }
        });
    }
}

