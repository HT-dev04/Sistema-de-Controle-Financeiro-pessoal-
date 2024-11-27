import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProjectUI extends JFrame {

    private static int contadorId = 1;

    public ProjectUI(TransacaoDAO transacaoDAO) {
        setTitle("Tabela de Controle Financeiro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setResizable(false);
        setLocationRelativeTo(null); // Centraliza a janela
        
        // Paineis de informações
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
        
        // Configuração da tabela
        String[] colunas = {"ID", "Data Inicial", "Data Final", "Descrição", "Valor", "Tipo"};
        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(modeloTabela);
        JScrollPane painelRolagem = new JScrollPane(tabela);
                       
        JPanel painelTotais = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelTotalReceita = new JLabel("Total Receita: 0.00");
        JLabel labelTotalDespesa = new JLabel("Total Despesa: 0.00");
        painelTotais.add(labelTotalReceita);
        painelTotais.add(labelTotalDespesa);
        
        // Painel principal
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
        
        // Calcula o total dos valores
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
        
        // Action listener do botão de inserir
        botaoInserir.addActionListener(e -> {
            SimpleDateFormat formatoData = new SimpleDateFormat("yyyy/MM/dd");
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

                // Salvar no banco de dados
                transacaoDAO.inserirTransacao(descricao, valor, tipo, dataInicial, dataFinal);

                // Adicionar na tabela da interface
                String id = gerarIdSequencial(); // Gera o ID sequencial
                modeloTabela.addRow(new Object[]{id, dataInicial, dataFinal, descricao, valor, tipo});
                atualizarTotais.run();

                // Limpar campos
                campoDescricao.setText("");
                campoValor.setText("");
                grupoTipo.clearSelection();
                dataInicialChooser.setDate(null);
                dataFinalChooser.setDate(null);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido. Use apenas números.");
            }
        });
        
        // Action listener do botão de editar
        botaoEditar.addActionListener(e -> {
    int linhaSelecionada = tabela.getSelectedRow();

    if (linhaSelecionada == -1) {
        JOptionPane.showMessageDialog(null, "Selecione uma transação para editar.");
        return;
    }

    // Obtemos os valores selecionados na tabela
    int id = Integer.parseInt(modeloTabela.getValueAt(linhaSelecionada, 0).toString());
    String descricaoAtual = modeloTabela.getValueAt(linhaSelecionada, 1).toString();
    double valorAtual = Double.parseDouble(modeloTabela.getValueAt(linhaSelecionada, 2).toString());
    String tipoAtual = modeloTabela.getValueAt(linhaSelecionada, 3).toString();
    String dataInicialAtual = modeloTabela.getValueAt(linhaSelecionada, 4).toString();
    String dataFinalAtual = modeloTabela.getValueAt(linhaSelecionada, 5).toString();

    // Criamos campos para edição
    JTextField descricaoCampo = new JTextField(descricaoAtual);
    JTextField valorCampo = new JTextField(String.valueOf(valorAtual));
    JTextField tipoCampo = new JTextField(tipoAtual);
    JTextField dataInicialCampo = new JTextField(dataInicialAtual);
    JTextField dataFinalCampo = new JTextField(dataFinalAtual);

    Object[] message = {
        "Descrição:", descricaoCampo,
        "Valor:", valorCampo,
        "Tipo:", tipoCampo,
        "Data Inicial:", dataInicialCampo,
        "Data Final:", dataFinalCampo,
    };

    int option = JOptionPane.showConfirmDialog(null, message, "Editar Transação", JOptionPane.OK_CANCEL_OPTION);

    if (option == JOptionPane.OK_OPTION) {
        String descricao = descricaoCampo.getText();
        double valor = Double.parseDouble(valorCampo.getText());
        String tipo = tipoCampo.getText();
        String dataInicial = dataInicialCampo.getText();
        String dataFinal = dataFinalCampo.getText();

        // Atualiza no banco de dados
        transacaoDAO.editarTransacao(id, descricao, valor, tipo, dataInicial, dataFinal);

        // Atualiza na tabela
        modeloTabela.setValueAt(descricao, linhaSelecionada, 1);
        modeloTabela.setValueAt(valor, linhaSelecionada, 2);
        modeloTabela.setValueAt(tipo, linhaSelecionada, 3);
        modeloTabela.setValueAt(dataInicial, linhaSelecionada, 4);
        modeloTabela.setValueAt(dataFinal, linhaSelecionada, 5);

        JOptionPane.showMessageDialog(null, "Transação editada com sucesso!");
    }
});
        
        // Action listener do botão de excluir
        botaoExcluir.addActionListener(e -> {
    int linha = tabela.getSelectedRow();
    if (linha != -1) {
        // Capturar os dados da linha selecionada
        String descricao = modeloTabela.getValueAt(linha, 3).toString();
        double valor = Double.parseDouble(modeloTabela.getValueAt(linha, 4).toString());
        String tipo = modeloTabela.getValueAt(linha, 5).toString();
        String dataInicial = modeloTabela.getValueAt(linha, 1).toString();
        String dataFinal = modeloTabela.getValueAt(linha, 2).toString();

        // Confirmar exclusão com o usuário
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir esta transação?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            // Excluir a transação do banco de dados
            transacaoDAO.excluirTransacao(descricao, valor, tipo, dataInicial, dataFinal);

            // Remover a linha da tabela na interface
            modeloTabela.removeRow(linha);

            // Atualizar os totais
            atualizarTotais.run();

            JOptionPane.showMessageDialog(this, "Transação excluída com sucesso!");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Selecione uma linha para excluir.");
    }
});

        
        // Action listener do botão de consultar
        botaoConsultar.addActionListener(e -> {
    String idStr = JOptionPane.showInputDialog("Digite o ID da transação para consultar:");
    if (idStr != null && !idStr.isEmpty()) {
        try {
            int id = Integer.parseInt(idStr); // Converte o ID para inteiro
            String[] transacao = transacaoDAO.consultarTransacao(id); // Consulta no banco de dados

            if (transacao != null) {
                // Exibe os detalhes da transação em um JOptionPane
                JOptionPane.showMessageDialog(null,
                    "ID: " + transacao[0] + "\n" +
                    "Descrição: " + transacao[1] + "\n" +
                    "Valor: " + transacao[2] + "\n" +
                    "Tipo: " + transacao[3] + "\n" +
                    "Data Inicial: " + transacao[4] + "\n" +
                    "Data Final: " + transacao[5],
                    "Detalhes da Transação",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Mensagem de erro caso o ID não seja encontrado
                JOptionPane.showMessageDialog(null, "Transação não encontrada.");
            }
        } catch (NumberFormatException ex) {
            // Mensagem de erro caso o ID seja inválido
            JOptionPane.showMessageDialog(null, "ID inválido. Por favor, digite um número.");
        }
    }
});
    }
    
    // Método para gerar o ID sequencial
    private String gerarIdSequencial() {
        return String.valueOf(contadorId++);
    }
}
