package neoflex.dossier.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import neoflex.dossier.dto.enums.Theme;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EmailMessage {

    private String address;
    private Theme theme;
    private UUID statementId;
    private String code;
    private List<String> documents;

}
