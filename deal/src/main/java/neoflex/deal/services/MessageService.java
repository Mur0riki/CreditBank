package neoflex.deal.services;

import java.util.UUID;

public interface MessageService {
    void finishRegistration(UUID statementId);
    void send(UUID statementId);
    void sign(UUID statementId);
    void code(UUID statementid, String sesCode);
}
