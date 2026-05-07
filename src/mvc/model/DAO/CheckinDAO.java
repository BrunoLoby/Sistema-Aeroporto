package mvc.model.DAO;

import mvc.model.Entities.Checkin;
import mvc.model.Entities.Ticket;
import java.time.LocalDate;
import java.util.Scanner;

public class CheckinDAO {
    private Checkin[] checkins = new Checkin[100];
    private Scanner scanner = new Scanner(System.in);

    public boolean realizarCheckin(Ticket ticket) {
        System.out.println("\n=== REALIZAR CHECK-IN ===");
        System.out.print("Informe o documento do passageiro: ");
        String documento = scanner.nextLine();

        if (ticket == null) {
            System.out.println("Erro: ticket inexistente.");
            return false;
        }

        if (ticket.getPassageiro() == null) {
            System.out.println("Erro: ticket sem passageiro vinculado.");
            return false;
        }

        if (!ticket.getPassageiro().getDocumento().equals(documento)) {
            System.out.println("Documento incorreto!");
            return false;
        }

        Checkin c = new Checkin();
        //c.setId(System.currentTimeMillis());
        c.setDocumento(documento);
        c.setDt_criacao(LocalDate.now());
        c.setDt_modificacao(LocalDate.now());

        int i = proximaPosicaoLivre();
        if (i == -1) {
            System.out.println("Erro: limite de check-ins atingido.");
            return false;
        }

        checkins[i] = c;
        System.out.println("Check-in realizado com sucesso!");
        return true;
    }

    private int proximaPosicaoLivre() {
        for (int i = 0; i < checkins.length; i++) {
            if (checkins[i] == null) return i;
        }
        return -1;
    }

    public void listarCheckins() {
        System.out.println("\n=== LISTA DE CHECK-INS ===");
        for (Checkin c : checkins) {
            if (c != null)
                System.out.println(c);
        }
    }
}
