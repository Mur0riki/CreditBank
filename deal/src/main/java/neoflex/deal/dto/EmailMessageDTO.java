package neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import neoflex.deal.model.enumFields.Theme;

import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Schema(description = "Письмо на почту")
public class EmailMessageDTO {

    @Schema(description = "Адрес", example = "ivanov@gmail.com")
    private String address;

    @Schema(description = "Название темы", example = "FINISH_REGISTRATION")
    private Theme theme;

    @Schema(description = "ID Кредитного предложения", example = "1")
    private UUID statementId;

    @Schema(description = "Текст сообщения", example = "Завершите регистрацию!")
    private String text;

}
