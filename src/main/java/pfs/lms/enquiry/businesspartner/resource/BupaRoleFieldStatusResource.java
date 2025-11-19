package pfs.lms.enquiry.businesspartner.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleEntityFieldStatus;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleEntitySetFieldStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BupaRoleFieldStatusResource {
    List<BupaRoleEntityFieldStatus> bupaRoleEntityFieldStatusList;
    List<BupaRoleEntitySetFieldStatus> bupaRoleEntitySetFieldStatusList;
}
