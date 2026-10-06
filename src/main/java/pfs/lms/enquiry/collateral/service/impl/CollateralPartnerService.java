package pfs.lms.enquiry.collateral.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleType;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerRoleTypeRepository;
import pfs.lms.enquiry.collateral.dto.PartnerSearchResultDto;
import pfs.lms.enquiry.collateral.dto.ValueEntryDto;
import pfs.lms.enquiry.collateral.service.ICollateralPartnerService;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.exception.LmsException;
import pfs.lms.enquiry.repository.PartnerRepository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CollateralPartnerService implements ICollateralPartnerService {

    /** Minimum length of a text criterion. */
    private static final int MIN_TEXT_LENGTH = 2;

    private final PartnerRepository partnerRepository;
    private final BusinessPartnerRoleTypeRepository roleTypeRepository;

    public CollateralPartnerService(PartnerRepository partnerRepository,
                                    BusinessPartnerRoleTypeRepository roleTypeRepository) {
        this.partnerRepository = partnerRepository;
        this.roleTypeRepository = roleTypeRepository;
    }

    @Override
    public List<PartnerSearchResultDto> search(String name1, String name2, String defaultPartnerRole,
                                               String searchTerm1, String searchTerm2) {
        String n1 = clean(name1);
        String n2 = clean(name2);
        String role = clean(defaultPartnerRole);
        String s1 = clean(searchTerm1);
        String s2 = clean(searchTerm2);
        if (n1 == null && n2 == null && role == null && s1 == null && s2 == null) {
            throw new LmsException("Enter at least one search criterion.", HttpStatus.PRECONDITION_FAILED);
        }
        for (String text : new String[]{n1, n2, s1, s2}) {
            if (text != null && text.length() < MIN_TEXT_LENGTH) {
                throw new LmsException("Enter at least " + MIN_TEXT_LENGTH + " characters per search criterion.",
                        HttpStatus.PRECONDITION_FAILED);
            }
        }

        Specification<Partner> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.isNotNull(root.get("partyNumber")));
            if (n1 != null) {
                predicates.add(builder.like(builder.lower(root.get("partyName1")), contains(n1)));
            }
            if (n2 != null) {
                predicates.add(builder.like(builder.lower(root.get("partyName2")), contains(n2)));
            }
            if (role != null) {
                predicates.add(builder.equal(root.get("defaultPartnerRole"), role));
            }
            if (s1 != null) {
                predicates.add(builder.like(builder.lower(root.get("searchTerm1")), contains(s1)));
            }
            if (s2 != null) {
                predicates.add(builder.like(builder.lower(root.get("searchTerm2")), contains(s2)));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };

        Map<String, String> roles = roleTexts();
        return partnerRepository.findAll(specification,
                        PageRequest.of(0, MAX_RESULTS, Sort.by("partyName1").and(Sort.by("partyNumber"))))
                .stream()
                .map(partner -> toDto(partner, roles))
                .collect(Collectors.toList());
    }

    @Override
    public List<ValueEntryDto> getPartnerRoles() {
        return roleTypeRepository.findAll().stream()
                .filter(role -> role.getCode() != null)
                .sorted(Comparator.comparing(BusinessPartnerRoleType::getCode))
                .map(role -> new ValueEntryDto(role.getCode(), role.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> getNames(Collection<String> partyNumbers) {
        Map<String, String> names = new LinkedHashMap<>();
        for (String number : partyNumbers) {
            Partner partner = find(number);
            if (partner != null) {
                names.put(number, name(partner));
            }
        }
        return names;
    }

    @Override
    public boolean exists(String partyNumber) {
        return find(partyNumber) != null;
    }

    private Partner find(String partyNumber) {
        if (partyNumber == null || !partyNumber.trim().matches("\\d{1,10}")) {
            return null;
        }
        try {
            return partnerRepository.findByPartyNumber(Integer.valueOf(partyNumber.trim()));
        } catch (NumberFormatException ex) {
            return null;
        } catch (RuntimeException ex) {
            // more than one partner with the number: it exists
            return partnerRepository.findAll((root, query, builder) ->
                            builder.equal(root.get("partyNumber"), Integer.valueOf(partyNumber.trim())),
                    PageRequest.of(0, 1)).stream().findFirst().orElse(null);
        }
    }

    private Map<String, String> roleTexts() {
        Map<String, String> roles = new LinkedHashMap<>();
        for (BusinessPartnerRoleType role : roleTypeRepository.findAll()) {
            if (role.getCode() != null) {
                roles.putIfAbsent(role.getCode(), role.getValue());
            }
        }
        return roles;
    }

    private static PartnerSearchResultDto toDto(Partner partner, Map<String, String> roles) {
        PartnerSearchResultDto dto = new PartnerSearchResultDto();
        dto.setId(partner.getId());
        dto.setPartyNumber(partner.getPartyNumber());
        dto.setPartyName1(partner.getPartyName1());
        dto.setPartyName2(partner.getPartyName2());
        dto.setDefaultPartnerRole(partner.getDefaultPartnerRole());
        dto.setDefaultPartnerRoleText(partner.getDefaultPartnerRole() != null ? roles.get(partner.getDefaultPartnerRole()) : null);
        dto.setSearchTerm1(partner.getSearchTerm1());
        dto.setSearchTerm2(partner.getSearchTerm2());
        dto.setCity(partner.getCity());
        dto.setState(partner.getState());
        return dto;
    }

    private static String name(Partner partner) {
        String name = (Objects.toString(partner.getPartyName1(), "") + " " + Objects.toString(partner.getPartyName2(), "")).trim();
        return name.isEmpty() ? String.valueOf(partner.getPartyNumber()) : name;
    }

    private static String contains(String text) {
        String escaped = text.toLowerCase().replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
        return "%" + escaped + "%";
    }

    private static String clean(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
