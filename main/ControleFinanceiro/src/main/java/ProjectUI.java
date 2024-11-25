import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;

public class ProjectUI extends JFrame {
    public ProjectUI(TransacaoDAO transacaoDAO) {
        // configura janela principal
        setTitle("Tabela de Controle Financeiro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setResizable(false);
        setLocationRelativeTo(null); // Centraliza a janela
        
        // paineis de informações
        JPanel painelDescricao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelDescricao = new JLabel("Descrição:");
        JTextField campoDescricao = new JTextField(20);
        painelDescricao.add(labelDescricao);
        painelDescricao.add(campoDescricao);
        
        JPanel painelDatas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelDataInicial = new JLabel("Data Inicial:");
        JDateChooser dataInicialChooser = new JDateChooser();
        JLabel labelDataFinal = new JLabel("Data Final:");
        JDateChooser dataFinalChooser = new JDateChooser();
        painelDatas.add(labelDataInicial);
        painelDatas.add(dataInicialChooser);
        painelDatas.add(labelDataFinal);
        painelDatas.add(dataFinalChooser);
        
        JPanel painelValor = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelValor = new JLabel("Valor:");
        JTextField campoValor = new JTextField(10);
        painelValor.add(labelValor);
        painelValor.add(campoValor);
        
        JPanel painelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelTipo = new JLabel("Tipo:");
        JRadioButton botaoReceita = new JRadioButton("Receita");
        JRadioButton botaoDespesa = new JRadioButton("Despesa");
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(botaoReceita);
        grupoTipo.add(botaoDespesa);
        painelTipo.add(labelTipo);
        painelTipo.add(botaoReceita);
        painelTipo.add(botaoDespesa);
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botaoInserir = new JButton("Inserir");
        JButton botaoEditar = new JButton("Editar");
        JButton botaoConsultar = new JButton("Consultar");
        JButton botaoExcluir = new JButton("Excluir");
        painelBotoes.add(botaoInserir);
        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoConsultar);
        painelBotoes.add(botaoExcluir);
        
        // configura a tabela debaixo 
        String[] colunas = {"ID", "Data Inicial", "Data Final", "Descrição", "Valor", "Tipo"};
        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(modeloTabela);
        JScrollPane painelRolagem = new JScrollPane(tabela);
        
        JPanel painelTotais = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelTotalReceita = new JLabel("Total Receita: 0.00");
        JLabel labelTotalDespesa = new JLabel("Total Despesa: 0.00");
        painelTotais.add(labelTotalReceita);
        painelTotais.add(labelTotalDespesa);
        
        // painel principal do programa
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.add(painelDescricao);
        painelPrincipal.add(painelDatas);
        painelPrincipal.add(painelValor);
        painelPrincipal.add(painelTipo);
        painelPrincipal.add(painelBotoes);
        painelPrincipal.add(painelRolagem);
        painelPrincipal.add(painelTotais);
        add(painelPrincipal);
        setVisible(true);
        
        // calcula o total dos valores
        Runnable atualizarTotais = () -> {
            double totalReceita = 0.0;
            double totalDespesa = 0.0;
            for (int i = 0; i < modeloTabela.getRowCount(); i++) {
                double valor = Double.parseDouble(modeloTabela.getValueAt(i, 4).toString());
                String tipo = modeloTabela.getValueAt(i, 5).toString();
                if ("Receita".equals(tipo)) {
                    totalReceita += valor;
                } else if ("Despesa".equals(tipo)) {
                    totalDespesa += valor;
                }
            }
            labelTotalReceita.setText(String.format("Total Receita: %.2f", totalReceita));
            labelTotalDespesa.setText(String.format("Total Despesa: %.2f", totalDespesa));
        };
        
        // action listener do botão de inserir
        botaoInserir.addActionListener(e -> {
            SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
            String descricao = campoDescricao.getText();
            String valorTexto = campoValor.getText();
            String tipo = botaoReceita.isSelected() ? "Receita" : botaoDespesa.isSelected() ? "Despesa" : null;

            if (descricao.isEmpty() || valorTexto.isEmpty() || tipo == null ||
                dataInicialChooser.getDate() == null || dataFinalChooser.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos corretamente.");
                return;
            }
            try {
                double valor = Double.parseDouble(valorTexto);
                String dataInicial = formatoData.format(dataInicialChooser.getDate());
                String dataFinal = formatoData.format(dataFinalChooser.getDate());

                // salvar no banco de dados
                transacaoDAO.inserirTransacao(descricao, valor, tipo, dataInicial, dataFinal);

                // adicionar na tabela da interface
                String id = gerarIdAleatorio();
                modeloTabela.addRow(new Object[]{id, dataInicial, dataFinal, descricao, valor, tipo});
                atualizarTotais.run();

                // limpar campos
                campoDescricao.setText("");
                campoValor.setText("");
                grupoTipo.clearSelection();
                dataInicialChooser.setDate(null);
                dataFinalChooser.setDate(null);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido. Use apenas números.");
            }
        });
        
        // action listener do botão de editar
        botaoEditar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha != -1) {
                SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
                String descricao = campoDescricao.getText();
                String valorTexto = campoValor.getText();
                String tipo = botaoReceita.isSelected() ? "Receita" : botaoDespesa.isSelected() ? "Despesa" : null;
                if (descricao.isEmpty() || valorTexto.isEmpty() || tipo == null ||
                    dataInicialChooser.getDate() == null || dataFinalChooser.getDate() == null) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos corretamente.");
                    return;
                }
                try {
                    double valor = Double.parseDouble(valorTexto);
                    String dataInicial = formatoData.format(dataInicialChooser.getDate());
                    String dataFinal = formatoData.format(dataFinalChooser.getDate());

                    modeloTabela.setValueAt(dataInicial, linha, 1);
                    modeloTabela.setValueAt(dataFinal, linha, 2);
                    modeloTabela.setValueAt(descricao, linha, 3);
                    modeloTabela.setValueAt(valor, linha, 4);
                    modeloTabela.setValueAt(tipo, linha, 5);
                    atualizarTotais.run();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Valor inválido. Use apenas números.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma linha para editar.");
            }
        });
        
        // action listener do botão de excluir
        botaoExcluir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha != -1) {
                modeloTabela.removeRow(linha);
                atualizarTotais.run();
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma linha para excluir.");
            }
        });

        // action listener do botão de consultar
        botaoConsultar.addActionListener(e -> {
            // criar a janela de consulta
            JPanel painelConsulta = new JPanel();
            painelConsulta.setLayout(new FlowLayout());
            
            JLabel labelId = new JLabel("ID:");
            JTextField campoId = new JTextField(10);
            JButton botaoPesquisar = new JButton("Pesquisar");
            
            painelConsulta.add(labelId);
            painelConsulta.add(campoId);
            painelConsulta.add(botaoPesquisar);
            
            JDialog dialog = new JDialog(this, "Consultar Transação por ID", true);
            dialog.setSize(300, 120);
            dialog.setLayout(new BorderLayout());
            dialog.add(painelConsulta, BorderLayout.CENTER);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
            
            botaoPesquisar.addActionListener(ev -> {
                String id = campoId.getText();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Preencha o ID.");
                    return;
                }

                boolean encontrado = false;
                for (int i = 0; i < modeloTabela.getRowCount(); i++) {
                    String idTabela = modeloTabela.getValueAt(i, 0).toString();
                    if (idTabela.equals(id)) {
                        tabela.setRowSelectionInterval(i, i); // Destaca a linha
                        encontrado = true;
                        break;
                    }
                }

                if (!encontrado) {
                    JOptionPane.showMessageDialog(dialog, "ID não encontrado.");
                }
                dialog.dispose(); // fecha a janela de consulta após a pesquisa
            });
        });
    }

    private String gerarIdAleatorio() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            id.append((int)(Math.random() * 10));  // gera números aleatórios de 0 a 9
        }
        return id.toString();
    }
}

