import javax.swing.*;
import java.awt.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;

public class Interface {
    public Interface() {
        JFrame tela = new JFrame("Controle Financeiro");
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha ao clicar no "X"
        tela.setSize(800,600); // Largura e altura
        tela.setLocationRelativeTo(null); // Centraliza a janela
        tela.setLayout(new BorderLayout()); // Define o layout do JFrame como BorderLayout
                     
        // Painel do cabeçalho no topo
        JPanel head = new JPanel(new FlowLayout(FlowLayout.CENTER )); // Painel com FlowLayout
        JLabel cabecalho = new JLabel(" CADASTRO DE TRANSAÇÕES "); // Texto do cabeçalho
        head.add(cabecalho); // Adiciona o texto ao painel
             
        // Painel da descrição 
        JPanel descricao = new JPanel(new FlowLayout(FlowLayout.LEFT )); // FlowLayout à esquerda
        JLabel labelD = new JLabel("Descrição "); // Rótulo para a descrição
        JTextField caixaTxt1 = new JTextField(50); // Campo de texto
        descricao.add(labelD); // Adiciona o rótulo ao painel
        descricao.add(caixaTxt1); // Adiciona o campo ao painel
               
        //Painel do tipo de transação
        JPanel tipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelT = new JLabel("Tipo "); // Rótulo para a descrição
        tipo.add(labelT);//add o label ao painel
        JButton receita = new JButton("Receita");//criando botão para receita
        JButton despesas = new JButton("Despesas");//criando botão para despesas
        //add botões
        tipo.add(receita);
        tipo.add(despesas);
        
        //adicionando o painel de Data
        JPanel data = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelData = new JLabel("Data: ");
        JFormattedTextField campoData = null;
         try {
            // Máscara para formato de data: dd/MM/yyyy
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_'); // Preenche com '_' antes de digitar
            campoData = new JFormattedTextField(mask);
            campoData.setColumns(8); // Tamanho do campo
        } catch (ParseException e) {
            e.printStackTrace();
        }
        data.add(labelData);//add label a data
        data.add(campoData);//add campo para data
      
        //painel para dispor todos os outros
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        //add os paineis ao main
        main.add(head);
        main.add(descricao);
        main.add(tipo);
        main.add(data);
        
        tela.add(main);
        
        // Tornando a janela visível
        tela.setVisible(true);
    }

    public static void main(String[] args) {
        new Interface(); // Cria e exibe a interface
    }
}
