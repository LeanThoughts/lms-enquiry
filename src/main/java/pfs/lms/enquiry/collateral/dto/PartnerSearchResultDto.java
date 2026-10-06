package pfs.lms.enquiry.collateral.dto;

import java.util.UUID;

/** A business partner found by the partner search of the Resp. Parties tab. */
public class PartnerSearchResultDto {

    private UUID id;
    private Integer partyNumber;
    private String partyName1;
    private String partyName2;
    private String defaultPartnerRole;
    private String defaultPartnerRoleText;
    private String searchTerm1;
    private String searchTerm2;
    private String city;
    private String state;

    public PartnerSearchResultDto() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Integer getPartyNumber() { return partyNumber; }
    public void setPartyNumber(Integer partyNumber) { this.partyNumber = partyNumber; }

    public String getPartyName1() { return partyName1; }
    public void setPartyName1(String partyName1) { this.partyName1 = partyName1; }

    public String getPartyName2() { return partyName2; }
    public void setPartyName2(String partyName2) { this.partyName2 = partyName2; }

    public String getDefaultPartnerRole() { return defaultPartnerRole; }
    public void setDefaultPartnerRole(String defaultPartnerRole) { this.defaultPartnerRole = defaultPartnerRole; }

    public String getDefaultPartnerRoleText() { return defaultPartnerRoleText; }
    public void setDefaultPartnerRoleText(String defaultPartnerRoleText) { this.defaultPartnerRoleText = defaultPartnerRoleText; }

    public String getSearchTerm1() { return searchTerm1; }
    public void setSearchTerm1(String searchTerm1) { this.searchTerm1 = searchTerm1; }

    public String getSearchTerm2() { return searchTerm2; }
    public void setSearchTerm2(String searchTerm2) { this.searchTerm2 = searchTerm2; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}
