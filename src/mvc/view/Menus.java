package mvc.view;

//FAZENDO IMPORTS DE BIBLIOTECAS
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.time.LocalDate;
import mvc.model.Entities.Aeroporto;
import mvc.model.Entities.Voo;
import mvc.model.Entities.Usuario;
import mvc.model.Entities.Passageiro;
import mvc.model.Entities.Ticket;
import mvc.model.Entities.Companhia;
import mvc.model.Entities.Assentos;
import mvc.model.Entities.Checkin;
import mvc.model.Entities.Bagagem;

import mvc.model.DAO.AeroportoDAO;
import mvc.model.DAO.UsuarioDAO;
import mvc.model.DAO.VooDAO;
import mvc.model.DAO.PassageiroDAO;
import mvc.model.DAO.TicketDAO;
import mvc.model.DAO.CompanhiaDAO;
import mvc.model.DAO.AssentosDAO;
import mvc.model.DAO.CheckinDAO;
import mvc.model.DAO.BagagemDAO;

//IMPORT UTIL
import mvc.util.BoardingPass;
import java.util.Scanner;
import mvc.util.Relatorios;

public class Menus {

    Scanner scanner = new Scanner(System.in);
    private AeroportoDAO aeroportoDAO;
    private VooDAO vooDAO;
    private UsuarioDAO usuarioDAO;
    private PassageiroDAO passageiroDAO;
    private TicketDAO ticketDAO;
    private CompanhiaDAO companhiaDAO;
    private AssentosDAO assentosDAO;
    private CheckinDAO checkinDAO;
    private BagagemDAO bagagemDAO;
    private BoardingPass boardingPass;
    private Relatorios relatorios;
    
    public Menus() {
        this.scanner = new Scanner(System.in);

        // 1 — Cria passageiroDAO SEM USUARIO
        this.passageiroDAO = new PassageiroDAO();

        // 2 — Agora cria usuarioDAO passando passageiroDAO
        this.usuarioDAO = new UsuarioDAO(passageiroDAO);

        // 3 — Faz mão dupla para passageiroDAO ter acesso ao usuarioDAO
        this.passageiroDAO.setUsuarioDAO(usuarioDAO);

        // --- resto igual ---
        this.aeroportoDAO = new AeroportoDAO();
        this.vooDAO = new VooDAO();
        this.ticketDAO = new TicketDAO(vooDAO, passageiroDAO);
        this.companhiaDAO = new CompanhiaDAO();
        this.assentosDAO = new AssentosDAO();
        this.checkinDAO = new CheckinDAO();
        this.bagagemDAO = new BagagemDAO();
        this.boardingPass = new BoardingPass();
        this.relatorios = new Relatorios();
    }

    //METODO DE VERIFICAR LOGIN
    private Usuario verificarLoginUsuario() {

        System.out.println("\nInforme seu acesso!!");

        System.out.print("Login: ");
        String login = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Usuario usuarioLogado = usuarioDAO.verificarLogin(login, senha);

        if (usuarioLogado != null) {
            System.out.println("Login realizado com sucesso! Bem-vindo, " + usuarioLogado.getNome() + "!");
            return usuarioLogado; // Retorna o objeto Usuario completo
        } else {
            System.out.println("\nLogin ou senha incorretos!");
            return null;
        }
    }

    //MENU LOGIN
    public void loginOuCadastrar() {
        int opcao = 99;
        System.out.println("\n=== MENU LOGIN ===");

        do {
            System.out.println("\n1. Login");
            System.out.println("2. Cadastrar");
            System.out.println("0. Sair do Sistema");
            System.out.print("Escolha uma opcao: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    Usuario usuarioLogado = verificarLoginUsuario();
                    if (usuarioLogado != null) {
                        long usuarioId = usuarioLogado.getId();

                        if (usuarioDAO.ehAdm(usuarioId)) {
                            System.out.println("Acesso de administrador concedido!");
                            menuADM();
                        } else if (usuarioDAO.ehFuncionario(usuarioId)) {
                            System.out.println("Acesso de funcionario concedido!");
                            menuFuncionario();
                        } else {
                            System.out.println("Acesso de passageiro concedido!");
                            menuUsuario(usuarioLogado); // PASSA O USUÁRIO LOGADO
                        }
                    }
                    break;
                case 2:
                    usuarioDAO.cadastrarUsuario();
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opcao invalida!!");
            }
        } while (opcao != 0);
    }

    //MENU INICIAL
    public void menuInicial(Usuario usuarioLogado) {
        int opcao;

        do {

            vooDAO.listarVoo();

            System.out.println("\n=== SISTEMA DE AEROPORTOS ===");

            System.out.println("1. Tela Login");
            System.out.println("2. Painel de VOOS");
            System.out.println("3. Compra de passagens");
            System.out.println("0. Sair do Sistema");
            System.out.print("Escolha uma opcao: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    loginOuCadastrar();
                    break;
                case 2:
                    vooDAO.listarVoo();
                    break;
                case 3:
                    if (usuarioLogado == null) {
                        usuarioDAO.cadastrarUsuario();
                    } else {
                        menuComprarPassagem(usuarioLogado);
                    }
                    break;

                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opcao invalida!!");
            }

        } while (opcao != 0);

        scanner.close();
    }

    //MENU ADMINISTRATIVO
    public void menuADM() {
        int opcao;

        do {
            System.out.println("\n=== MENU ADMINISTRATIVO ===");
            System.out.println("1. Gerenciar Aeroportos");
            System.out.println("2. Gerenciar Passageiros");
            System.out.println("3. Gerenciar Companhia Aerea");
            System.out.println("4. Gerenciar Voos");
            System.out.println("5. Gerar Relatorios");

            System.out.println("0. DESLOGAR");
            System.out.println("Escolha uma opcao: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    menuAeroportos();
                    break;
                case 2:
                    menuPassageiros();
                    break;
                case 3:
                    menuCompahia();
                    break;
                case 4:
                    menuVoos();
                    break;
                case 5:
                    menuRelatorios();
                    break;

            }
        } while (opcao != 0);
    }

    //MENU FUNCIONARIO
    public void menuFuncionario() {
        int opcao;

        do {
            System.out.println("\n=== MENU FUNCIONARIO ===");
            System.out.println("1. Gerenciar Voos");
            System.out.println("2. Gerenciar Passageiros");
            System.out.println("3. Listar bagagens Despachadas");
            System.out.println("4. Ver Relatorios");
            System.out.println("0. DESLOGAR");
            System.out.print("Escolha uma opcaoo: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    menuVoos();
                    break;
                case 2:
                    menuPassageiros();
                    break;
                case 3:
                    bagagemDAO.listarBagagens();
                    break;
                case 4:
                    menuRelatorios();
                    break;
                case 0:
                    System.out.println("Saindo do menu funcionario...");
                    break;
                default:
                    System.out.println("Opcao invalida!!");
            }
        } while (opcao != 0);
    }

    //MENU USUARIO
    public void menuUsuario(Usuario usuarioLogado) {
        Passageiro passageiro = usuarioDAO.obterPassageiroDoUsuario(usuarioLogado);
        if (passageiro == null) {
            System.out.println("Nao linkado com nenhum passageiro.");
            return; // Sai do menu
        }

        int opcao;

        do {
            System.out.println("\n=== MENU USUARIO ===");
            System.out.println("Bem-vindo, " + usuarioLogado.getNome() + "!!");

            System.out.println("\n1. Comprar passagem");
            System.out.println("2. Realizar Check-In");
            System.out.println("3. Ver minhas passagens");
            System.out.println("4. Despachar bagagem");
            System.out.println("5. Imprimir boarding Pass");
            System.out.println("6. Meus dados");
            System.out.println("0. DESLOGAR");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            Ticket ticket;

            switch (opcao) {
                case 1:
                    ticketDAO.comprarPassagem(assentosDAO, passageiro);
                    break;

                case 2:
                    ticket = ticketDAO.obterPassagemDoPassageiro(passageiro);
                    if (ticket == null) {
                        System.out.println("Voce nao possui passagens para fazer check-in.");
                    } else {
                        checkinDAO.realizarCheckin(ticket);
                        System.out.println("Check-in realizado com sucesso!");
                    }
                    break;

                case 3:
                    ticketDAO.listarPassagem(passageiro.getID());
                    break;

                case 4:
                    bagagemDAO.despacharBagagem(passageiro);
                    break;

                case 5:
                    ticket = ticketDAO.obterPassagemDoPassageiro(passageiro);
                    if (ticket == null) {
                        System.out.println("Voce nao possui passagens para imprimir boarding pass.");
                    } else {
                        boardingPass.imprimir(ticket);
                        System.out.println("Boarding pass impresso com sucesso!");
                    }
                    break;

                case 6:
                    System.out.println(passageiro);
                    break;

                case 0:
                    System.out.println("Deslogando...");
                    break;

                default:
                    System.out.println("Opcao invalida!");
            }

        } while (opcao != 0);
    }

    //MENU RELATORIOS
    public void menuRelatorios() {
        int opcao = 99;

        this.aeroportoDAO.listarAeroportos();

        do {
            System.out.println("\n=== RELATORIOS ===");
            System.out.println("1. Passageiros que deixaram um aeroporto");
            System.out.println("2. Passageiros que chegaram a um aeroporto");
            System.out.println("3. Valor arrecadado por companhia aerea");
            System.out.println("4. PDF passageiros");
            System.out.println("5. Importar Passageiros.txt");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1 -> {
                    System.out.print("\nDigite o nome do aeroporto de origem: ");
                    String aeroportoOrigem = scanner.nextLine();
                    ticketDAO.relatorioPassageirosQueDeixaramAeroporto(aeroportoOrigem);
                }

                case 2 -> {
                    System.out.print("\nDigite o nome do aeroporto de destino: ");
                    String aeroportoDestino = scanner.nextLine();
                    ticketDAO.relatorioPassageirosQueChegaramAeroporto(aeroportoDestino);
                }

                case 3 -> {
                    System.out.print("\nDigite o nome da companhia aerea: ");
                    String nomeCompanhia = scanner.nextLine();
                    System.out.print("Digite a data inicial (dd/MM/yyyy): ");
                    String dataInicio = scanner.nextLine();
                    System.out.print("Digite a data final (dd/MM/yyyy): ");
                    String dataFim = scanner.nextLine();

                    ticketDAO.relatorioFaturamentoPorCompanhiaEAno(nomeCompanhia, LocalDate.now(), LocalDate.now());
                }
                case 4 -> {
                    System.out.println("\nGerando PDF de passageiros...");

                    try {
                        relatorios.gerarPDF("RelatorioPassageiros.pdf");
                        System.out.println("PDF gerado com sucesso! (pasta reports/)");
                    } catch (DocumentException | IOException e) {
                        System.out.println("Erro ao gerar PDF: " + e.getMessage());
                    }
}
                case 5 -> {
                    passageiroDAO.importarPassageirosDeArquivo();
                }


                case 0 -> {
                    System.out.println("Retornando ao menu anterior...");
                }

                default -> {
                    System.out.println("Opcaoo invalida! Tente novamente.");
                }
            }

        } while (opcao != 0);
    }

    //MENU COMPRA DA PASSAGEM
    public void menuComprarPassagem(Usuario usuarioLogado) {
        if (usuarioLogado.getTipoUsuario() != 3) {
            System.out.println("Apenas passageiros podem comprar passagens!");
            return;
        }

        Passageiro passageiro = usuarioDAO.obterPassageiroDoUsuario(usuarioLogado);
        if (passageiro == null) {
            System.out.println("Passageiro nao encontrado para este usuário!");
            return;
        }

        System.out.println("\nCOMPRAR PASSAGEM - Passageiro: " + passageiro.getNome());

        // Listar voos disponíveis
        vooDAO.listarVoo();

        System.out.print("Digite o ID do voo: ");
        long idVoo = scanner.nextLong();
        scanner.nextLine();

        // Escolher assento disponível
        System.out.print("Digite o ID do assento: ");
        long idAssentoEscolhido = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Digite o valor: ");
        double valorEscolhido = scanner.nextDouble();
        scanner.nextLine();

        // Criar o ticket passando o ID do passageiro corretamente
        ticketDAO.criarTicket(passageiro.getID(), idVoo, idAssentoEscolhido, valorEscolhido);

        System.out.println("Passagem comprada com sucesso!");
    }


    //1.GERENCIAR AEROPORTOS 
    public void menuAeroportos() {
        int opcao;

        do {
            System.out.println("\n===MENU AEROPORTOS===");
            System.out.println("1. Listar aeroportos");
            System.out.println("2. Adcionar aeroportos");
            System.out.println("3. Alterar aeroporto");
            System.out.println("4. Deletar aeroporto");
            System.out.println("0. Retornar ao menu principal");
            System.out.println("Escolha uma opcao: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    aeroportoDAO.listarAeroportos();
                    break;
                case 2:
                    aeroportoDAO.adicionarAeroporto();
                    break;
                case 3:
                    aeroportoDAO.alterarAeroporto();
                    break;
                case 4:
                    aeroportoDAO.deletarAeroporto();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opcao invalida!!");
            }
            if (opcao != 0) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine();
            }

        } while (opcao != 0);
    }

    //2.GERENCIAR PASSAGEIROS
    public void menuPassageiros() {
        int opcao;

        do {
            System.out.println("\n=== MENU PASSAGEIROS ===");
            System.out.println("1. Listar Passageiros");
            System.out.println("2. Adicionar Passageiro");
            System.out.println("3. Alterar Passageiro");
            System.out.println("4. Deletar Passageiro");
            System.out.println("0. Retornar ao menu principal");
            System.out.println("Escolha um opcao: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    passageiroDAO.listarPassageirosBD();
                    break;
                case 2: {

                    passageiroDAO.adicionarPassageiro();
                }
                break;

                case 3:
                    passageiroDAO.alterarPassageiro();
                    break;
                case 4:
                    passageiroDAO.deletarPassageiro();
                    break;

            }

        } while (opcao != 0);
    }

    //3.GERENCIAR VOOS
    public void menuVoos() {
        int opcao;

        do {
            System.out.println("\n=== MENU VOOS ===");
            System.out.println("1. Listar voo");
            System.out.println("2. Adcionar voo");
            System.out.println("3. Alterar voo");
            System.out.println("4. Deletar voo");
            System.out.println("0. Retornar ao menu principal");
            System.out.println("Escolha uma opcao: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    vooDAO.listarVoo();
                    break;
                case 2:
                    vooDAO.adicionarVoo();
                    break;
                case 3:
                    vooDAO.alterarVoo();
                    break;
                case 4:
                    vooDAO.deletarVoo();
                    break;

            }

            if (opcao != 0) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine();
            }

        } while (opcao != 0);
    }

    //4.MENU COMPANHIA AEREA
    public void menuCompahia() {
        int opcao;

        do {
            System.out.println("\n=== MENU COMPANHIA AEREA ===");
            System.out.println("1. Listar Companhias");
            System.out.println("2. Adcionar Companhia");
            System.out.println("3. Alterar Companhia");
            System.out.println("4. Deletar Companhia");
            System.out.println("0. Retornar ao menu principal");
            System.out.println("Escolha uma opcao");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    companhiaDAO.listarCompanhias();
                    break;
                case 2:
                    companhiaDAO.adicionarCompanhia();
                    break;
                case 3:
                    companhiaDAO.alterarCompanhia();
                    break;
                case 4:
                    companhiaDAO.deletarCompanhia();
                    break;

            }
            if (opcao != 0) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine();
            }
        } while (opcao != 0);
    }

}
