package neoflex.deal.mapping;

import neoflex.deal.dto.CreditDto;
import neoflex.deal.model.Credit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public abstract class CreditMapper {
    @Mapping(target = "insuranceEnabled", source = "isInsuranceEnabled")
    @Mapping(target = "salaryClient", source = "isSalaryClient")
    public abstract  Credit toCredit(CreditDto creditDto);

    @Mapping(target = "isInsuranceEnabled", source = "insuranceEnabled")
    @Mapping(target = "isSalaryClient", source = "salaryClient")
    public abstract  CreditDto toCreditDto(Credit credit);
}