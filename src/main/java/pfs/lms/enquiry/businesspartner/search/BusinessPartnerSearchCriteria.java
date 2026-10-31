package pfs.lms.enquiry.businesspartner.search;

/**
 * Selection of the business partner search (all optional, combined with AND). Text values match anywhere in the
 * column, ignoring case.
 */
public class BusinessPartnerSearchCriteria {

    /** Name 1 and name 2 of the partner */
    private String name;
    /** Registered e-mail */
    private String email;
    private String pan;
    /** Search term 1 or search term 2 */
    private String searchTerm;
    /** Default partner role (exact code, e.g. TR0151) */
    private String partnerRole;
    /** Business partner number range: only From = that number; only To = up to that number */
    private String partnerNumberFrom;
    private String partnerNumberTo;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPan() { return pan; }
    public void setPan(String pan) { this.pan = pan; }
    public String getSearchTerm() { return searchTerm; }
    public void setSearchTerm(String searchTerm) { this.searchTerm = searchTerm; }
    public String getPartnerRole() { return partnerRole; }
    public void setPartnerRole(String partnerRole) { this.partnerRole = partnerRole; }
    public String getPartnerNumberFrom() { return partnerNumberFrom; }
    public void setPartnerNumberFrom(String partnerNumberFrom) { this.partnerNumberFrom = partnerNumberFrom; }
    public String getPartnerNumberTo() { return partnerNumberTo; }
    public void setPartnerNumberTo(String partnerNumberTo) { this.partnerNumberTo = partnerNumberTo; }
}
