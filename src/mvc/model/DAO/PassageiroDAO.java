package mvc.model.DAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;
import mvc.model.Entities.Passageiro;
import mvc.model.Entities.Usuario;

//IMPORTS FASE 2
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
 * @author Bruno Lobianco/ João Pedro Khoury
 */
public class PassageiroDAO {

    public Passageiro[] passageiros = new Passageiro[40];
    Scanner scanner = new Scanner(System.in);
    private UsuarioDAO usuarioDAO;
    private int contPassageiro;
    private static final String ARQUIVO_IMPORTACAO = "passageiros.txt";

    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public PassageiroDAO() {

    }

    public void importarPassageirosDeArquivo() {
        String caminhoArquivo = "importacao/passageiros.txt"; // caminho fixo
        File arquivo = new File(ARQUIVO_IMPORTACAO);

        if (!arquivo.exists()) {
            System.out.println("Arquivo de importação não encontrado!");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_IMPORTACAO))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }

                String[] dados = linha.split(";");

                if (dados.length < 5) {
                    System.out.println("Linha inválida: " + linha);
                    continue;
                }

                Passageiro p = new Passageiro();

                p.setNome(dados[0]);
                p.setDocumento(dados[1]);
                p.setNascimento(dados[2]);
                p.setLogin(dados[3]);
                p.setSenha(dados[4]);

                // FOTO É OPCIONAL
                if (dados.length >= 6) {
                    p.setFoto(dados[5]);
                } else {
                    p.setFoto(null);
                }

                p.setDt_criacao(LocalDate.now());
                p.setDt_modificacao(LocalDate.now());

                boolean ok = adicionarPassageiroBD(p);
                if (ok) {
                    System.out.println("Passageiro importado: " + p.getNome());
                } else {
                    System.out.println("Erro ao importar: " + p.getNome());
                }
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo de importação!");
            e.printStackTrace();
        }
    }



    public boolean adicionarPassageiroViaImport(String nome, String documento, LocalDate nascimento,
            String login, String senha, String foto) {

        String sql = "INSERT INTO passageiro "
                + "(nome, documento, nascimento, login, senha, dt_criacao, dt_modificacao, foto) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, documento);
            stmt.setString(3, nascimento.toString());
            stmt.setString(4, login);
            stmt.setString(5, senha);
            stmt.setObject(6, LocalDate.now());
            stmt.setObject(7, LocalDate.now());
            stmt.setString(8, foto);

            stmt.executeUpdate();
            System.out.println("Importado: " + nome);
            return true;

        } catch (Exception e) {
            System.out.println("ERRO AO INSERIR IMPORTADO: " + e.getMessage());
            return false;
        }
    }

    //METODO BUSCA POR ID
    public Passageiro buscarPassageiroPorIdBD(int id) {

        String sql = "SELECT * FROM passageiro WHERE id = ?";

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Passageiro p = new Passageiro();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setDocumento(rs.getString("documento"));
                p.setNascimento(rs.getString("nascimento"));
                p.setFoto(rs.getString("foto"));
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

    public void adicionarPassageiro() {
        Passageiro elemento = new Passageiro();

        System.out.println("Nome: ");
        elemento.setNome(scanner.nextLine());

        System.out.println("Informe documento(000-00): ");
        elemento.setDocumento(scanner.nextLine());

        System.out.println("Informe data do Nascimento(dd/mm/aaaa): ");
        elemento.setNascimento(scanner.nextLine());

        System.out.println("Informe Login: ");
        elemento.setLogin(scanner.nextLine());

        System.out.println("Informe senha: ");
        elemento.setSenha(scanner.nextLine());

        elemento.setDt_criacao(LocalDate.now());
        elemento.setDt_modificacao(LocalDate.now());

        boolean ok = adicionarPassageiroBD(elemento);

        if (ok) {
            System.out.println("PASSAGEIRO CADASTRADO COM SUCESSO");
        } else {
            System.out.println("ERRO AO CADASTRAR");
        }

    }

    public boolean adicionarPassageiroBD(Passageiro elemento) {

        // ESTE SQL NAO DEVE TER COLUNA TICKET POIS TICKET NAO PERTENCE A TABELA PASSAGEIROS
        String sql = "INSERT INTO passageiro"
                + "(nome, nascimento, login, senha, documento, dt_criacao, dt_modificacao) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        boolean resultado = false;

        try (Connection connection = new ConnectionFactory().getConnection(); PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) {

            // SETANDO OS VALORES DO OBJETO
            stmt.setString(1, elemento.getNome());
            stmt.setString(2, elemento.pegarNascimento());
            stmt.setString(3, elemento.getLogin());
            stmt.setString(4, elemento.getSenha());
            stmt.setString(5, elemento.getDocumento());
            stmt.setObject(6, elemento.getDt_criacao());
            stmt.setObject(7, elemento.getDt_modificacao());

            stmt.execute();
            resultado = true;

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                elemento.setId(idGerado);
            }

            if (usuarioDAO != null) {
                usuarioDAO.criarUsuarioParaPassageiroBD(elemento);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultado;
    }

    public boolean criarUsuarioParaPassageiro(Passageiro passageiro) {

        // CRIA OBJETO USUARIO A PARTIR DO PASSAGEIRO
        Usuario u = new Usuario();
        u.setNome(passageiro.getNome());
        u.setDocumento(passageiro.getDocumento());
        u.setNascimento(passageiro.pegarNascimento());
        u.setLogin(passageiro.getLogin());
        u.setSenha(passageiro.getSenha());
        u.setTipoUsuario(3);
        u.setIdPassageiro((int) passageiro.getID());
        u.setDt_criacao(passageiro.getDt_criacao());
        u.setDt_modificacao(passageiro.getDt_modificacao());

        // FAZ O INSERT NO BANCO
        return usuarioDAO.adicionarUsuarioBD(u);
    }

    //2.LISTAR PASSAGEIROS
    public List<Passageiro> listarPassageirosBD() {

        System.out.println("\n=== LISTA DE PASSAGEIROS ===");

        String sql = "SELECT * FROM passageiro";

        List<Passageiro> p = new ArrayList<>();

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                p.add(mapear(rs));
            }

            for (Passageiro passageiro : p) {
                System.out.println(passageiro);
            }

        } catch (SQLException e) {
            System.out.println("ERRO AO LISTAR PASSAGEIROS");
        }
        return p;
    }

    //METODO DE MAPEAMENTO
    private Passageiro mapear(ResultSet rs) throws SQLException {

        Passageiro p = new Passageiro();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setDocumento(rs.getString("documento"));
        p.setNascimento(rs.getString("nascimento"));
        p.setLogin(rs.getString("login"));
        p.setSenha(rs.getString("senha"));
        p.setFoto(rs.getString("foto"));

        //LENDO AS DATAS DO BANCO DE DADOS
        p.setDt_criacao(rs.getDate("dt_criacao").toLocalDate());
        p.setDt_modificacao(rs.getDate("dt_modificacao").toLocalDate());

        return p;
    }

    //3.ALTERAR PASSAGEIRO
    public void alterarPassageiro() {
        System.out.println("\n=== ALTERAR PASSAGEIRO ===");

        //LISTAGEM DOS PASSAGEIROS
        this.listarPassageirosBD();

        //PEDE ID PARA ALTERACAO
        System.out.println("\nInforme o ID do Passageiro: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Passageiro passageiroEncontrado = buscarPassageiroPorIdBD(id);

        if (passageiroEncontrado == null) {
            System.out.println("Passageiro com ID " + id + " nao encontrado!!");
            return;
        }

        //FAZENDO PEDIDO DE ALTERACAO DOS DADOS
        System.out.println("Informe os novos dados");

        //NOME
        System.out.println("Novo nome: ");
        String novoNome = scanner.nextLine();
        //TRIM REMOVE ESPACOS EM BRANCO// IS EMPTY VERIFICA SE FICOU VAZIO
        if (!novoNome.trim().isEmpty()) {
            passageiroEncontrado.setNome(novoNome);
        }

        //NASCIMENTO
        System.out.println("Data Nascimeto(dd/mm/aaaa): ");
        String novoNascimento = scanner.nextLine();
        if (!novoNascimento.trim().isEmpty()) {
            passageiroEncontrado.setNascimento(novoNascimento);
        }

        //DOCUMENTO
        System.out.println("Documento(000-00): ");
        String novoDocumento = scanner.nextLine();

        if (!novoDocumento.trim().isEmpty()) {
            passageiroEncontrado.setDocumento(novoDocumento);
        }

        //LOGIN
        System.out.println("Login: ");
        String novoLogin = scanner.nextLine();

        //TRIM REMOVE ESPACOS EM BRANCO// IS EMPTY VERIFICA SE FICOU VAZIO
        if (!novoLogin.trim().isEmpty()) {
            passageiroEncontrado.setLogin(novoLogin);
        }

        //SENHA
        System.out.println("Senha: ");
        String novoSenha = scanner.nextLine();

        //TRIM REMOVE ESPACOS EM BRANCO// IS EMPTY VERIFICA SE FICOU VAZIO
        if (!novoSenha.trim().isEmpty()) {
            passageiroEncontrado.setSenha(novoSenha);
        }

        //ALTERANDO DATA MODIFICACAO
        passageiroEncontrado.setDt_modificacao(LocalDate.now());

        String sqlPass = "UPDATE passageiro SET nome=?, documento=?, nascimento=?, login=?, senha=?, dt_modificacao=? WHERE id=?";

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlPass)) {

            stmt.setString(1, passageiroEncontrado.getNome());
            stmt.setString(2, passageiroEncontrado.getDocumento());
            stmt.setString(3, passageiroEncontrado.pegarNascimento());
            stmt.setString(4, passageiroEncontrado.getLogin());
            stmt.setString(5, passageiroEncontrado.getSenha());
            stmt.setObject(6, passageiroEncontrado.getDt_modificacao());
            stmt.setInt(7, (int) passageiroEncontrado.getID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar passageiro!");
            e.printStackTrace();
            return;
        }

        String sqlUser = "UPDATE usuario SET nome=?, documento=?, nascimento=?, login=?, senha=?, dt_modificacao=? WHERE documento=?";

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlUser)) {

            stmt.setString(1, passageiroEncontrado.getNome());
            stmt.setString(2, passageiroEncontrado.getDocumento());
            stmt.setString(3, passageiroEncontrado.pegarNascimento());
            stmt.setString(4, passageiroEncontrado.getLogin());
            stmt.setString(5, passageiroEncontrado.getSenha());
            stmt.setObject(6, passageiroEncontrado.getDt_modificacao());
            stmt.setString(7, passageiroEncontrado.getDocumento());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar usuário vinculado!");
            e.printStackTrace();
            return;
        }

        System.out.println("Passageiro e usuário atualizados com sucesso!");

    }

    //4.DELETAR PASSAGEIRO
    public void deletarPassageiro() {

        System.out.println("\n=== DELETAR PASSAGEIRO ===");
        listarPassageirosBD();

        System.out.print("\nInforme o ID do passageiro: ");
        int idDelete = scanner.nextInt();
        scanner.nextLine();

        String sqlDelUsuario = "DELETE FROM usuario WHERE idPassageiro = ?";

        String sqlDelPassageiro = "DELETE FROM passageiro WHERE id = ?";

        try (Connection connection = new ConnectionFactory().getConnection()) {

            // 1. Deletar usuário associado
            PreparedStatement stmtUsr = connection.prepareStatement(sqlDelUsuario);
            stmtUsr.setInt(1, idDelete);
            stmtUsr.executeUpdate(); // pode retornar 0 (caso não exista)

            // 2. Deletar passageiro
            PreparedStatement stmtPass = connection.prepareStatement(sqlDelPassageiro);
            stmtPass.setInt(1, idDelete);
            int linhas = stmtPass.executeUpdate();

            if (linhas > 0) {
                System.out.println("PASSAGEIRO E USUÁRIO DELETADOS COM SUCESSO!");
            } else {
                System.out.println("ERRO: Passageiro não encontrado!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //METODO DE CRIAR PASSAGEIRO AUTOMATICAMENTE AO CADASTRAR USUARIO
    public Passageiro criarPassageiroAutomaticoBD(Usuario usuario) {

        String sql = "INSERT INTO passageiro (nome, documento, nascimento, login, senha, dt_criacao, dt_modificacao) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getDocumento());
            stmt.setString(3, usuario.getNascimento());
            stmt.setString(4, usuario.getLogin());
            stmt.setString(5, usuario.getSenha());
            stmt.setObject(6, usuario.getDt_criacao());
            stmt.setObject(7, usuario.getDt_modificacao());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);

                Passageiro p = new Passageiro();
                p.setId(idGerado);
                p.setNome(usuario.getNome());
                p.setDocumento(usuario.getDocumento());
                p.setNascimento(usuario.getNascimento());
                p.setLogin(usuario.getLogin());
                p.setSenha(usuario.getSenha());
                p.setDt_criacao(usuario.getDt_criacao());
                p.setDt_modificacao(usuario.getDt_modificacao());

                return p;  // RETORNA O PASSAGEIRO COMPLETO
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
