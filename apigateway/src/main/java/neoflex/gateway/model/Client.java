package neoflex.gateway.model;

import lombok.*;
import neoflex.gateway.model.enumFields.Gender;
import neoflex.gateway.model.enumFields.MaritalStatus;
import neoflex.gateway.model.json.Employment;
import neoflex.gateway.model.json.Passport;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Client {

    UUID clientId;

    String lastName;

    String firstName;

    String middleName;

    LocalDate birthdate;

    String email;

    Gender gender;

    MaritalStatus maritalStatus;

    Integer dependentAmount;

    Passport passport;

    Employment employment;

    String accountNumber;
}
