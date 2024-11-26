import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 public class ConectaBanco {
     private final String url ;
    private final String usuario;
    private final String senha;
    private final String drv;
     public ConectaBanco(String url, String usuario, String senha, String drv) {
        this.url = url;
        this.usuario = usuario;
        this.senha = senha;
        this.drv = drv;
    }
     public Connection getConnection() {
        Connection conexao = null;
        try {
            Class.forName(drv);
            conexao = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexão estabelecida com sucesso!");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return conexao;
    }
}
