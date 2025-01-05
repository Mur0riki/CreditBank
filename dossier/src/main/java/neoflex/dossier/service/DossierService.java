package neoflex.dossier.service;

public interface DossierService {
    void sendSes(String receiver);
    void sendMessage(String receiver, String text);
    void sendDocument(String receiver);
}
