package mvc.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import mvc.control.ConnectionFactory;
import mvc.model.Entities.Voo;
import mvc.model.Entities.Companhia;


/**
 *
 * @author Bruno Lobianco/ João Pedro Khoury
 */

public class VooDAO {

    public Voo[] voos = new Voo[15];
    Scanner scanner;
    private CompanhiaDAO companhiaDAO;
    private long contVoo;
    
    public VooDAO() {

        scanner = new Scanner(System.in);
        companhiaDAO = new CompanhiaDAO();
       
    }


    // 1.FUNCAO PARA ADICIONAR VOO
    public void adicionarVoo() {
        System.out.println("\n=== ADICIONAR VOO ===");

        // Cria o objeto primeiro
        Voo novoVoo = new Voo();

        System.out.print("Cidade origem: ");
        novoVoo.setOrigem(scanner.nextLine());

        System.out.print("Cidade destino: ");
        novoVoo.setDestino(scanner.nextLine());

        //FAZENDO LISTAGEM DAS COMPANHIAS PARA ESCOLHA
        companhiaDAO.listarCompanhias();

        System.out.print("Escolha o ID da companhia do Voo: ");
        int idCompanhia = scanner.nextInt();
        scanner.nextLine();

        Companhia companhiaEscolhida = companhiaDAO.buscaCompanhiaPorId(idCompanhia);
        novoVoo.setCompanhia(companhiaEscolhida);

        System.out.println("Capacidade: ");
        novoVoo.setCapacidade(scanner.nextInt());
        scanner.nextLine();//LIMPEZA DE BUFFER

        System.out.print("Duracao (0h00): ");
        novoVoo.setDuracao(scanner.nextLine());

        System.out.println("Estado (Programado, Embarque, Decolado, Atrasado, Cancelado): ");
        novoVoo.setEstado(scanner.nextLine());

        novoVoo.setDt_criacao(LocalDate.now());
        novoVoo.setDt_modificacao(LocalDate.now());
        
        if (adicionarVooBD(novoVoo)) {
            System.out.println("VOO CADASTRADO NO BANCO!");
        } else {
            System.out.println("ERRO AO CADASTRAR VOO!");
        }

    }
    
   public boolean adicionarVooBD(Voo elemento) {

    String sql = "INSERT INTO voo "
            + "(origem, destino, duracao, id_companhia, estado, capacidade, dt_criacao, dt_modificacao) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    boolean resultado = false;

    try (Connection connection = new ConnectionFactory().getConnection();
         PreparedStatement stmt = connection.prepareStatement(sql)) {

        stmt.setString(1, elemento.getOrigem());
        stmt.setString(2, elemento.getDestino());
        stmt.setString(3, elemento.getDuracao());

        // AQUI ESTA A CORRECAO
        stmt.setLong(4, elemento.getCompanhia().getId());

        stmt.setString(5, elemento.getEstado());
        stmt.setInt(6, elemento.getCapacidade());
        stmt.setObject(7, elemento.getDt_criacao());
        stmt.setObject(8, elemento.getDt_modificacao());

        stmt.execute();
        resultado = true;

       } catch (SQLException e) {

           throw new RuntimeException("ERRO AO ADICIONAR VOO NO BANCO!");
       }

    return resultado;
}


    //2.FUNCAO PARA LISTAR VOOS
    public List<Voo> listarVoo() {

        System.out.println("\n=== LISTA DE VOOS (MYSQL) ===");

        String sql = "SELECT * FROM voo";

        List<Voo> vooList = new ArrayList<>();

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                vooList.add(mapear(rs));

            }

            for (Voo v : vooList) {
                System.out.println(v);
            }

        } catch (SQLException e) {
            throw new RuntimeException("ERRO AO LISTAR VOOS");

        }
        return vooList;
    }

    public Voo buscarPorId(long id) {
        String sql = "SELECT * FROM voo WHERE id = ?";

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapear(rs);
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar voo por ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


    //FUNCAO DE MAPEAMENTO DA LINHA DO BANCO DE DADOS
    private Voo mapear(ResultSet rs) throws SQLException {

        Voo v = new Voo();
        v.setId(rs.getLong("id"));
        v.setOrigem(rs.getString("origem"));
        v.setDestino(rs.getString("destino"));
        v.setDuracao(rs.getString("duracao"));

        v.setEstado(rs.getString("estado"));
        v.setCapacidade(rs.getInt("capacidade"));

        v.setDt_criacao(rs.getDate("dt_criacao").toLocalDate());
        v.setDt_modificacao(rs.getDate("dt_modificacao").toLocalDate());

        long idCompanhia = rs.getLong("id_companhia");

        
        Companhia companhia = companhiaDAO.buscaCompanhiaPorId((int) idCompanhia);
        v.setCompanhia(companhia);

        
        return v;
    }

    //3.ALTERAR VOO
    public void alterarVoo() {

        System.out.println("\n=== ALTERAR VOO ===");

        this.listarVoo();

        //PEDE ID PARA ALTERACAO DE DADOS
        System.out.print("\nInforme o ID do Voo: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // limpeza de buffer

        Voo vooEncontrado = null;
        for (Voo v : voos) {
            if (v != null && v.getId() == id) {
                vooEncontrado = v;
                break;
            }
        }

        if (vooEncontrado == null) {
            System.out.println("Voo com ID " + id + " não encontrado!");
            return;
        }

        System.out.println("\nInforme os novos dados");

        //ORIGEM
        System.out.print("Origem: ");
        String novaOrigem = scanner.nextLine();
        if (!novaOrigem.trim().isEmpty()) {
            vooEncontrado.setOrigem(novaOrigem);
        }

        //DESTINO
        System.out.print("Destino: ");
        String novoDestino = scanner.nextLine();
        if (!novoDestino.trim().isEmpty()) {
            vooEncontrado.setDestino(novoDestino);
        }

        //COMPANHIA
        System.out.println("\nCompanhias disponiveis:");
        companhiaDAO.listarCompanhias(); // apenas imprime no console

        System.out.print("Digite o ID da nova companhia (ou 0 para manter a atual - "
                + (vooEncontrado.getCompanhia() != null ? vooEncontrado.getCompanhia().getNome() : "N/A") + "): ");
        long idCompanhia = scanner.nextLong();
        scanner.nextLine(); // limpar buffer

        if (idCompanhia != 0) {
            Companhia companhiaEscolhida = null;
            for (Companhia c : companhiaDAO.companhias) { // usa o vetor diretamente
                if (c != null && c.getId() == idCompanhia) {
                    companhiaEscolhida = c;
                    break;
                }
            }

            if (companhiaEscolhida != null) {
                vooEncontrado.setCompanhia(companhiaEscolhida);
            } else {
                System.out.println("Companhia nao encontrada. Mantida anterior.");
            }
        }

        //DURACAO
        System.out.print("Duracao (0h00): ");
        String novaDuracao = scanner.nextLine();
        if (!novaDuracao.trim().isEmpty()) {
            vooEncontrado.setDuracao(novaDuracao);
        }

        //ESTADO
        System.out.print("Estado (Programado, Embarque, Decolado, Atrasado, Cancelado):  ");
        String novoEstado = scanner.nextLine();
        if (!novoEstado.trim().isEmpty()) {
            vooEncontrado.setEstado(novoEstado);
        }

        //CAPACIDADE
        System.out.print("Capacidade: ");
        int novaCapacidade = scanner.nextInt();
        {
            vooEncontrado.setCapacidade(novaCapacidade);
        }

        //ARUALIZA DATA DE MODIFICACAO
        vooEncontrado.setDt_modificacao(LocalDate.now());

        System.out.println("\nVoo alterado com sucesso!");
    }

    //4.FUNCAO PARA DELETAR VOO
    public boolean deletarVoo() {
        System.out.println("\n=== LISTA DE VOO ===");
        this.listarVoo();

        System.out.print("\nDigite o ID do voo que deseja deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM voo WHERE id = ?";
        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("ERRO AO DELETAR VOO!!", e);
        }

       
    }

}
