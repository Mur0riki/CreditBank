package neoflex.deal.mapping;

import neoflex.deal.dto.FinishRegistrationRequestDto;
import neoflex.deal.dto.ScoringDataDto;
import neoflex.deal.model.Statement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ScoringDataMapper {

    @Mapping(target = "amount", source = "statement.appliedOffer.requestedAmount")
    @Mapping(target = "term", source = "statement.appliedOffer.term")
    @Mapping(target = "isInsuranceEnabled", source = "statement.appliedOffer.isInsuranceEnabled")
    @Mapping(target = "isSalaryClient", source = "statement.appliedOffer.isSalaryClient")
    @Mapping(target = "firstName", source = "statement.client.firstName")
    @Mapping(target = "lastName", source = "statement.client.lastName")
    @Mapping(target = "middleName", source = "statement.client.middleName")
    @Mapping(target = "birthdate", source = "statement.client.birthdate")
    @Mapping(target = "passportSeries", source = "statement.client.passport.series")
    @Mapping(target = "passportNumber", source = "statement.client.passport.number")
    @Mapping(target = "passportIssueBranch", source = "finishDto.passportIssueBranch")
    @Mapping(target = "passportIssueDate", source = "finishDto.passportIssueDate")
    @Mapping(target = "gender", source = "finishDto.gender")
    @Mapping(target = "maritalStatus", source = "finishDto.maritalStatus")
    @Mapping(target = "accountNumber", source = "finishDto.accountNumber")
    @Mapping(target = "employment", source = "finishDto.employment.employmentStatus")
    @Mapping(target = "dependentAmount", source = "finishDto.dependentAmount")
    public abstract  ScoringDataDto toScoringDataDto(Statement statement, FinishRegistrationRequestDto finishDto);
}
