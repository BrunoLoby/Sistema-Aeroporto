package mvc.model.DAO;

import java.time.LocalDate;
import java.util.Scanner;
import mvc.model.Entities.Ticket;
import mvc.model.Entities.Voo;
import mvc.model.Entities.Passageiro;
import mvc.model.Entities.Assentos;
import mvc.model.Entities.Companhia;

import mvc.control.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author João Pedro Khoury
 */
public class TicketDAO {

    Scanner scanner;
    private VooDAO vooDAO;
    private PassageiroDAO passageiroDAO;

    public TicketDAO(VooDAO vooDAO, PassageiroDAO passageiroDAO) {
        scanner = new Scanner(System.in);
        this.vooDAO = vooDAO;
        this.passageiroDAO = passageiroDAO;
    }

    //METODO PARA ADICIONAR TICKET
    public boolean adicionarTicket(Ticket ticket) {
        String sql = "INSERT INTO ticket (cod, valor, id_voo, id_passageiro, id_assento, dt_criacao, dt_modificacao) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Preenche os valores do ticket
            stmt.setLong(1, ticket.getCod());
            stmt.setDouble(2, ticket.getValor());
            stmt.setLong(3, ticket.getVoo().getId());
            stmt.setLong(4, ticket.getPassageiro().getID());
            stmt.setLong(5, ticket.getAssentos().getId());
            stmt.setDate(6, java.sql.Date.valueOf(ticket.getDt_criacao()));
            stmt.setDate(7, java.sql.Date.valueOf(ticket.getDt_modificacao()));

            stmt.executeUpdate(); // Executa o INSERT

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao adicionar ticket no banco: " + e.getMessage());
            return false;
        }
    }

    //METODO PARA GERAR CODIGO TICKET
    private static int contadorTickets = 1;

    private long gerarCodigoTicket() {
        String codigo = String.format("2025%05d", contadorTickets++);
        return Long.parseLong(codigo);
    }

    //METODO PARA CRIAR TICKET
    public void criarTicket(long idPassageiro, long idVoo, long idAssento, double valor) {
        String sql = "INSERT INTO ticket (id_passageiro, id_voo, id_assento, valor, dt_criacao) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idPassageiro);
            stmt.setLong(2, idVoo);
            stmt.setLong(3, idAssento); // <-- aqui precisa ser o assento escolhido
            stmt.setDouble(4, valor);
            stmt.executeUpdate();
            System.out.println("Passagem criada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao criar passagem: " + e.getMessage());
        }
    }


    //1.METODO PARA COMPRA DE PASSAGEM
    public void comprarPassagem(AssentosDAO assentosDAO, Passageiro passageiroLogado) {
        System.out.println("\n=== COMPRA DE PASSAGEM ===");

        // 1. LISTAR VOOS DO BD
        vooDAO.listarVoo();

        System.out.print("\nInforme o ID do voo desejado: ");
        int idVoo = scanner.nextInt();
        scanner.nextLine();

        // 2. BUSCAR VOO NO BD
        Voo vooSelecionado = vooDAO.buscarPorId(idVoo);

        if (vooSelecionado == null) {
            System.out.println("Voo nao encontrado!");
            return;
        }

        // 3. GERAR ASSENTOS SE NÃO EXISTEM
        if (!assentosDAO.temAssentosGerados(idVoo)) {
            assentosDAO.gerarAssentosParaVoo(idVoo, vooSelecionado.getCapacidade());
        }

        // 4. CHECAR LOTAÇÃO
        if (assentosDAO.vooLotado(idVoo)) {
            System.out.println("Não ha assentos disponiveis!!");
            return;
        }

        // 5. MOSTRAR ASSENTOS DISPONÍVEIS
        assentosDAO.listarAssentosDisponiveis(idVoo);

        System.out.print("Escolha o codigo do assento (ex: A5): ");
        String codigoAssento = scanner.nextLine();

        // 6. OCUPAR O ASSENTO
        boolean sucesso = assentosDAO.ocuparAssento(idVoo, codigoAssento, passageiroLogado.getNome());

        if (!sucesso) {
            System.out.println("Assento invalido ou ja ocupado.");
            return;
        }

        // 7. RECUPERAR O OBJETO DO ASSENTO
        Assentos OBJassento = assentosDAO.pegarAssentoEscolhido(idVoo, passageiroLogado.getNome());

        // 8. CRIA O TICKET (OBJETO)
        Ticket novoTicket = new Ticket();
        novoTicket.setCod(gerarCodigoTicket());
        novoTicket.setValor(1200.00);
        novoTicket.setVoo(vooSelecionado);
        novoTicket.setPassageiro(passageiroLogado);
        novoTicket.setAssentos(OBJassento);
        novoTicket.setDt_criacao(LocalDate.now());
        novoTicket.setDt_modificacao(LocalDate.now());

        // 9. SALVAR O TICKET NO BD
        boolean inserido = adicionarTicket(novoTicket);

        if (inserido) {
            System.out.println("\nCompra finalizada com sucesso!!");
            System.out.println("Passageiro: " + passageiroLogado.getNome());
            System.out.println("Voo: " + vooSelecionado.getOrigem() + " → " + vooSelecionado.getDestino());
            System.out.println("Assento: " + codigoAssento);
        } else {
            System.out.println("Erro ao salvar ticket no banco.");
            return;
        }

        // 10. VINCULAR AO PASSAGEIRO
        passageiroLogado.setTicket(novoTicket);
    }

    // 3. MÉTODO DE LISTAGEM DAS PASSAGENS DO USUÁRIO LOGADO
    public void listarPassagem(long idPassageiro) {

        String sql = """
        SELECT t.cod, t.valor, t.dt_criacao,
               a.cod_assento,
               v.origem, v.destino
        FROM ticket t
        INNER JOIN assentos a ON t.id_assento = a.id
        INNER JOIN voo v ON t.id_voo = v.id
        WHERE t.id_passageiro = ?
        ORDER BY t.dt_criacao DESC
    """;

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idPassageiro);
            ResultSet rs = stmt.executeQuery();

            boolean encontrou = false;

            while (rs.next()) {
                encontrou = true;
                System.out.println("----------------------------------------");
                System.out.println("Codigo: " + rs.getLong("cod"));
                System.out.println("Valor: R$" + rs.getDouble("valor"));
                System.out.println("Assento: " + rs.getString("cod_assento"));
                System.out.println("Origem: " + rs.getString("origem"));
                System.out.println("Destino: " + rs.getString("destino"));
                System.out.println("Data: " + rs.getDate("dt_criacao"));
            }

            if (!encontrou) {
                System.out.println("Nenhuma passagem encontrada!");
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar passagens: " + e.getMessage());
        }
    }

    /////////////////////////////////////

    //METODO PARA LISTAGEM DOS TICKETS
    public void listarTickets() {
        System.out.println("\n=== LISTA DE TICKETS ===");

        String sql = """
        SELECT t.cod, t.valor, t.dt_criacao, a.cod_assento, p.nome AS nome_passageiro, v.origem, v.destino
        FROM ticket t
        INNER JOIN assentos a ON t.id_assento = a.id
        INNER JOIN passageiro p ON t.id_passageiro = p.id
        INNER JOIN voo v ON t.id_voo = v.id

        ORDER BY t.cod ASC;
        """;

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                System.out.println("------------------------------------------------------");
                System.out.println("Codigo: " + rs.getLong("cod"));
                System.out.println("Valor: R$" + rs.getDouble("valor"));
                System.out.println("Assento: " + rs.getString("cod_assento"));
                System.out.println("Passageiro: " + rs.getString("nome_passageiro"));
                System.out.println("Voo: " + rs.getString("origem") + " → " + rs.getString("destino"));
                System.out.println("Data de criacao: " + rs.getDate("dt_criacao"));
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar tickets no banco: " + e.getMessage());
        }
    }

    //METODO PARA DELETAR TICKETS
    public boolean deletarTicket() {
        System.out.println("\n=== DELETAR TICKET ===");
        listarTickets();

        System.out.print("\nDigite o codigo do Ticket que deseja deletar: ");
        long cod = scanner.nextLong();
        scanner.nextLine();

        String sql = "DELETE FROM ticket WHERE cod = ?";

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, cod);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Erro ao deletar ticket: " + e.getMessage());
            return false;
        }

    }

    //METODO PARA OBTER PASSGEIRO
    public Ticket obterPassagemDoPassageiro(Passageiro passageiro) {
        return passageiro.getTicket();
    }

    public void relatorioPassageirosQueDeixaramAeroporto(String nomeAeroporto) {

        String sql = """
        SELECT p.nome, v.destino, t.dt_criacao
        FROM ticket t
        INNER JOIN passageiro p ON p.id = t.id_passageiro
        INNER JOIN voo v ON v.id = t.id_voo
        WHERE v.origem = ?
    """;

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeAeroporto);

            ResultSet rs = stmt.executeQuery();
            boolean encontrou = false;

            while (rs.next()) {
                encontrou = true;
                System.out.println("--------------------------------------");
                System.out.println("Passageiro: " + rs.getString("nome"));
                System.out.println("Destino: " + rs.getString("destino"));
                System.out.println("Data: " + rs.getDate("dt_criacao"));
            }

            if (!encontrou) {
                System.out.println("Nenhum passageiro saiu deste aeroporto.");
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void relatorioPassageirosQueChegaramAeroporto(String nomeAeroporto) {

        String sql = """
        SELECT p.nome, v.origem, t.dt_criacao
        FROM ticket t
        INNER JOIN passageiro p ON p.id = t.id_passageiro
        INNER JOIN voo v ON v.id = t.id_voo
        WHERE v.destino = ?
    """;

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeAeroporto);
            ResultSet rs = stmt.executeQuery();

            boolean encontrou = false;

            while (rs.next()) {
                encontrou = true;
                System.out.println("--------------------------------------");
                System.out.println("Passageiro: " + rs.getString("nome"));
                System.out.println("Origem: " + rs.getString("origem"));
                System.out.println("Data: " + rs.getDate("dt_criacao"));
            }

            if (!encontrou) {
                System.out.println("Nenhuma chegada para esse aeroporto.");
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void relatorioFaturamentoPorCompanhiaEAno(String nomeCompanhia, LocalDate inicio, LocalDate fim) {

        String sql = """
        SELECT SUM(t.valor) AS total
        FROM ticket t
        INNER JOIN voo v ON v.id = t.id_voo
        INNER JOIN companhia c ON c.id = v.id_companhia
        WHERE c.nome = ?
        AND t.dt_criacao BETWEEN ? AND ?
    """;

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeCompanhia);
            stmt.setDate(2, java.sql.Date.valueOf(inicio));
            stmt.setDate(3, java.sql.Date.valueOf(fim));

            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getDouble("total") > 0) {
                System.out.println("Faturamento total: R$ " + rs.getDouble("total"));
            } else {
                System.out.println("Nenhum faturamento encontrado no período.");
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

}
