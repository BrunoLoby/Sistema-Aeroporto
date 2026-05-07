package mvc.util;

import mvc.model.Entities.Ticket;
import mvc.model.Entities.Assentos;

public class BoardingPass {
    public void imprimir(Ticket ticket) {
        if (ticket == null || ticket.getPassageiro() == null) {
            System.out.println("Erro: ticket invalido.");
            return;
        }
        System.out.println("\n=== BOARDING PASS ===");
        System.out.println("Passageiro: " + ticket.getPassageiro().getNome());
        System.out.println("Documento: " + ticket.getPassageiro().getDocumento());
        System.out.println("Voo:\n" + ticket.getVoo());
        System.out.println("\n======================");
    }
}
