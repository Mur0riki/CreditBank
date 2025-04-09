package neoflex.deal.model.enumFields;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Индикатор того в каком состоянии находится кредит")
public enum Status {

    PREAPPROVAL,
    APPROVED,
    CC_DENIED,
    CC_APPROVED,
    PREPARE_DOCUMENTS,
    DOCUMENT_CREATED,
    CLIENT_DENIED,
    DOCUMENT_SIGNED,
    CREDIT_ISSUED,
}
