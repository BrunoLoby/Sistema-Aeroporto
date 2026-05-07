    package mvc.model.DAO;

import java.time.LocalDate;
import mvc.model.Entities.Usuario;
import java.util.Scanner;
import mvc.model.Entities.Passageiro;

//FASE 2
import mvc.control.ConnectionFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mvc.model.Entities.Voo;

/**
 *
 * @author Trabalho
 *
 */
public class UsuarioDAO {

    Scanner scanner = new Scanner(System.in);
    private PassageiroDAO passageiroDAO;

    //CONSTRUTOR
    public UsuarioDAO(PassageiroDAO passageiroDAO) {
        this.passageiroDAO = passageiroDAO;
        scanner = new Scanner(System.in);
        inserirUsuariosPadraoBD();
    }


    

    private boolean usuarioExisteBD(String login) {
        String sql = "SELECT id FROM usuario WHERE login = ? LIMIT 1";

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //ESTE METODO FOI ELABORADO PARA CRIAR USUARIOS PADRAO P/ TESTES;
    private void inserirUsuariosPadraoBD() {

        // USER 1
        if (!usuarioExisteBD("ADM")) {
            Usuario u1 = new Usuario();
            u1.setNome("Bruno");
            u1.setLogin("ADM");
            u1.setDocumento("525-88");
            u1.setNascimento("19/08/2004");
            u1.setSenha("1001");
            u1.setTipoUsuario(1);
            u1.setDt_criacao(LocalDate.now());
            u1.setDt_modificacao(LocalDate.now());
            adicionarUsuarioBD(u1);
        }

        // USER 2
        if (!usuarioExisteBD("ADM2")) {
            Usuario u2 = new Usuario();
            u2.setNome("Joao Pedro");
            u2.setLogin("ADM2");
            u2.setDocumento("125-02");
            u2.setNascimento("20/05/2005");
            u2.setSenha("2002");
            u2.setTipoUsuario(1);
            u2.setDt_criacao(LocalDate.now());
            u2.setDt_modificacao(LocalDate.now());
            adicionarUsuarioBD(u2);
        }

        // USER 3
        if (!usuarioExisteBD("FUNCIONARIO")) {
            Usuario u3 = new Usuario();
            u3.setNome("Amanda");
            u3.setLogin("FUNCIONARIO");
            u3.setDocumento("125-01");
            u3.setNascimento("22/02/2002");
            u3.setSenha("3003");
            u3.setTipoUsuario(2);
            u3.setDt_criacao(LocalDate.now());
            u3.setDt_modificacao(LocalDate.now());
            adicionarUsuarioBD(u3);
        }
    }

    //FUNCOES PARA VERIFICAR TIPO DE USUARIO
    public int getTipoUsuario(long id) {
        String sql = "SELECT tipoUsuario FROM usuario WHERE id = ?";

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("tipoUsuario");
                }

                } catch (SQLException e)
                {e.printStackTrace();}   

        return -1;
            }

    public boolean ehAdm(long id) {
         return getTipoUsuario(id) == 1;
        }

    public boolean ehFuncionario(long id) {
        return getTipoUsuario(id) == 2;
        }

    //2.CADASTRAR
    public void cadastrarUsuario() {
        System.out.println("\n=== CADASTRO ===");

        Usuario novoUsuario = new Usuario();

        System.out.print("Nome: ");
        novoUsuario.setNome(scanner.nextLine());

        System.out.print("Documento (000-00): ");
        novoUsuario.setDocumento(scanner.nextLine());

        System.out.print("Data de Nascimento (dd/mm/yyyy): ");
        novoUsuario.setNascimento(scanner.nextLine());

        System.out.print("Login: ");
        novoUsuario.setLogin(scanner.nextLine());

        System.out.print("Senha: ");
        novoUsuario.setSenha(scanner.nextLine());

        novoUsuario.setDt_criacao(LocalDate.now());
        novoUsuario.setDt_modificacao(LocalDate.now());

        //SETANDO TIPO 3 (PASSAGEIRO)
        novoUsuario.setTipoUsuario(3);

        //CRIANDO PASSAGEIRO NO BANCO DE DADOS
        Passageiro passageiro = passageiroDAO.criarPassageiroAutomaticoBD(novoUsuario);

        if (passageiro == null) {
            System.out.println("ERRO ao criar passageiro!");
            return;
        }

        novoUsuario.setIdPassageiro((int) passageiro.getID());

        if (adicionarUsuarioBD(novoUsuario)) {
            System.out.println("Usuario salvo com sucesso!");
        } else {
            System.out.println("Erro ao salvar usuario!");
        }
    }

    public boolean adicionarUsuarioBD(Usuario usuario) {

        String sql = "INSERT INTO usuario (nome,documento,nascimento,login,senha,idPassageiro,tipoUsuario,dt_criacao,dt_modificacao)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getDocumento());
            stmt.setString(3, usuario.getNascimento());
            stmt.setString(4, usuario.getLogin());
            stmt.setString(5, usuario.getSenha());
            stmt.setInt(6, usuario.getIdPassageiro());
            stmt.setInt(7, usuario.getTipoUsuario());
            stmt.setObject(8, usuario.getDt_criacao());
            stmt.setObject(9, usuario.getDt_modificacao());

            stmt.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //METODO PARA OBTER O PASSAGEIRO DO USUARIO NO BANCO
    public Passageiro obterPassageiroDoUsuario(Usuario usuario) {

        if (usuario == null || usuario.getDocumento() == null) {
            return null;
        }

        String sql = "SELECT * FROM passageiro WHERE documento = ? LIMIT 1";

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getDocumento());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Passageiro p = new Passageiro();

                p.setId(rs.getLong("id"));
                p.setNome(rs.getString("nome"));
                p.setDocumento(rs.getString("documento"));
                p.setNascimento(rs.getString("nascimento"));
                p.setLogin(rs.getString("login"));
                p.setSenha(rs.getString("senha"));
                p.setDt_criacao(rs.getDate("dt_criacao").toLocalDate());
                p.setDt_modificacao(rs.getDate("dt_modificacao").toLocalDate());

                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //METODO PARA VERIFICAR TIPO DE LOGIN
    public Usuario verificarLogin(String login, String senha) {

        String sql = "SELECT * FROM usuario WHERE login = ? AND senha = ? LIMIT 1";

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getLong("id"));
                u.setNome(rs.getString("nome"));
                u.setDocumento(rs.getString("documento"));
                u.setNascimento(rs.getString("nascimento"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                u.setTipoUsuario(rs.getInt("tipoUsuario"));
                u.setDt_criacao(rs.getDate("dt_criacao").toLocalDate());
                u.setDt_modificacao(rs.getDate("dt_modificacao").toLocalDate());
                return u;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //METODOS PARA TRANSFORMAR PASSAGEIROS JA CRIADOS EM USUARIOS
    public void criarUsuarioParaPassageiroBD(Passageiro passageiro) {

        Usuario u = new Usuario();
        u.setNome(passageiro.getNome());
        u.setDocumento(passageiro.getDocumento());
        u.setNascimento(passageiro.pegarNascimento());
        u.setLogin(passageiro.getLogin());
        u.setSenha(passageiro.getSenha());
        u.setTipoUsuario(3);
        u.setIdPassageiro((int) passageiro.getID());
        u.setDt_criacao(LocalDate.now());
        u.setDt_modificacao(LocalDate.now());

        adicionarUsuarioBD(u);
    }

    public boolean existeUsuarioParaPassageiroBD(Passageiro passageiro) {

        String sql = "SELECT id FROM usuario WHERE documento = ? LIMIT 1";

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, passageiro.getDocumento());
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // se achou → true

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
