package pfs.lms.enquiry.businesspartner.search;

import java.util.ArrayList;
import java.util.List;

/** One page of the business partner search. */
public class BusinessPartnerSearchResult {

    private List<Row> rows = new ArrayList<>();
    /** 0 based */
    private int page;
    private int size;
    private long total;
    private String sort;

    public List<Row> getRows() { return rows; }
    public void setRows(List<Row> rows) { this.rows = rows; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }

    /** A business partner in the result list (not the full partner: no credentials, no child tables). */
    public static class Row {
        private String id;
        private Integer partyNumber;
        private String partyName;
        private String partyName1;
        private String partyName2;
        private String defaultPartnerRole;
        private String defaultPartnerRoleText;
        private String partnerGroup;
        private String partnerCategory;
        private String pan;
        private String searchTerm1;
        private String searchTerm2;
        private String contactPersonName;
        private String email;
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String workFlowStatusDescription;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public Integer getPartyNumber() { return partyNumber; }
        public void setPartyNumber(Integer partyNumber) { this.partyNumber = partyNumber; }
        public String getPartyName() { return partyName; }
        public void setPartyName(String partyName) { this.partyName = partyName; }
        public String getPartyName1() { return partyName1; }
        public void setPartyName1(String partyName1) { this.partyName1 = partyName1; }
        public String getPartyName2() { return partyName2; }
        public void setPartyName2(String partyName2) { this.partyName2 = partyName2; }
        public String getDefaultPartnerRole() { return defaultPartnerRole; }
        public void setDefaultPartnerRole(String defaultPartnerRole) { this.defaultPartnerRole = defaultPartnerRole; }
        public String getDefaultPartnerRoleText() { return defaultPartnerRoleText; }
        public void setDefaultPartnerRoleText(String defaultPartnerRoleText) { this.defaultPartnerRoleText = defaultPartnerRoleText; }
        public String getPartnerGroup() { return partnerGroup; }
        public void setPartnerGroup(String partnerGroup) { this.partnerGroup = partnerGroup; }
        public String getPartnerCategory() { return partnerCategory; }
        public void setPartnerCategory(String partnerCategory) { this.partnerCategory = partnerCategory; }
        public String getPan() { return pan; }
        public void setPan(String pan) { this.pan = pan; }
        public String getSearchTerm1() { return searchTerm1; }
        public void setSearchTerm1(String searchTerm1) { this.searchTerm1 = searchTerm1; }
        public String getSearchTerm2() { return searchTerm2; }
        public void setSearchTerm2(String searchTerm2) { this.searchTerm2 = searchTerm2; }
        public String getContactPersonName() { return contactPersonName; }
        public void setContactPersonName(String contactPersonName) { this.contactPersonName = contactPersonName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getAddressLine1() { return addressLine1; }
        public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
        public String getAddressLine2() { return addressLine2; }
        public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getWorkFlowStatusDescription() { return workFlowStatusDescription; }
        public void setWorkFlowStatusDescription(String workFlowStatusDescription) { this.workFlowStatusDescription = workFlowStatusDescription; }
    }
}
