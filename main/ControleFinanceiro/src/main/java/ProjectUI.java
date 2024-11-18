import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
 
public class ProjectUI extends JFrame {
    private JTextField data;
    private JTextField desc;
    private JTextArea tipoTransacao;
    private JTextArea valor;
    private TransacaoDAO transacaoDAO;
 
    public ProjectUI(TransacaoDAO transacaoDAO) {
        this.transacaoDAO = transacaoDAO;
 
        setTitle("Controle de Financias");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
 
     
        descField = new JTextField(15);
        valorField = new JTextField(15);
        dataArea = new JTextArea(10, 30);
        resultadoArea.setEditable(false);
 
    
        JButton consultarButton = new JButton("Consultar");
        JButton inserirButton = new JButton("Inserir");
        JButton alterarButton = new JButton("Alterar");
        JButton excluirButton = new JButton("Excluir");
        JButton confirmarButton = new JButton("Confirmar");
        JButton receitaButton = new JButton("Receita");
        JButton despesaButton = new JButton("Despesa");
    
        add(new JLabel("Data:"));
        add(dataField);
        add(new JLabel("Valor:"));
        add(valorField);
        add(new JLabel("Descrição:"));
        add(descField);
        add(receitaButton);
        add(despesaButton);
        add(consultarButton);
        add(inserirButton);
        add(alterarButton);
        add(excluirButton);
        add(new JScrollPane(resultadoArea));
 
        // Ações dos botões
        consultarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultar();
            }
        });
 
        inserirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inserir();
            }
        });
 
        alterarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              dataField.setEditable(true);
              descField.setEditable(true);
              valorField.setEditable(true);
              tipotranField.setEditable(true);
                alterarButton.setEnabled(false);
                confirmarButton.setVisible(true);
            }
        });
        
        confirmarButton.addActionListener(new ActionListener(){
            confirmar();
        }
        receitaButton.addActionListener(new ActionListener()){
            receitaSelecionada = (String) receitaComboBox.getSelectedItem();
                salvarReceita();
        
    }        
 
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluir();
            }
        });
 
        setVisible(true);
    }
 
    private void consultar() {
        String nome = data.getText();
        String resultado = transacaoDAO.consultar(nome);
        resultadoArea.setText(resultado);
    }
 
    private void inserir() {
        String nome = nomeField.getText();
        String telefone = telefoneField.getText();
        transacaoDAO.inserir(nome, telefone);
    }
 
    private void alterar() {
        Int data = dataField.getText();
        transacaoDAO.alterar(data, descricao, valor, tipotran);
    }
 
    private void excluir() {
        String nome = nomeField.getText(); 
        transacaoDAO.excluir(nome);
    }
}
