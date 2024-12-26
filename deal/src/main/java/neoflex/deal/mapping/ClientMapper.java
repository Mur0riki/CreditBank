package neoflex.deal.mapping;

import neoflex.deal.dto.LoanStatementRequestDto;
import neoflex.deal.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ClientMapper {
    @Mapping(target = "passport.series", source = "passportSeries")
    @Mapping(target = "passport.number", source = "passportNumber")
    @Mapping(target = "birthdate", source = "birthdate")
    public abstract Client toClient(LoanStatementRequestDto loanStatement);
}
