export class PartnerModel {
    partyNumber: string;
    addressLine1: string;
    addressLine2: string;
    city: string;
    contactNumber: string;
    contactPersonName: string;
    country: string;
    email: string;
    groupCompany: string;
    id: string;
    pan: string;
    partyName1: string;
    partyName2: string;
    partyRole: string;
    password: string;
    postalCode: string;
    state: string;
    street: string;
    userName: string;
    industrySector: string;

    _links: Object;
    mobileNumber: null;
    faxNumber: null;
    searchTerm1: null;
    searchTerm2: null;
    title: null;

    /**
     * constructor()
     * Initialize the object.
     * @param _partner
     */
    constructor(_partner: any) {
        this.partyNumber = _partner.partyNumber || '';
        this.addressLine1 = _partner && _partner.addressLine1 || '';
        this.addressLine2 = _partner && _partner.addressLine2 || '';
        this.city = _partner && _partner.city || '';
        this.contactNumber = _partner && _partner.contactNumber || '';
        this.contactPersonName = _partner && _partner.contactPersonName || '';
        this.country = _partner && _partner.country || '';
        this.email = _partner && _partner.email || '';
        this.groupCompany = _partner && _partner.groupCompany || '';
        this.id = _partner && _partner.id || '';
        this.pan = _partner && _partner.pan || '';
        this.partyName1 = _partner && _partner.partyName1 || '';
        this.partyName2 = _partner && _partner.partyName2 || '';
        this.partyRole = _partner && _partner.partyRole || '';
        this.password = _partner && _partner.password || '';
        this.postalCode = _partner && _partner.postalCode || '';
        this.state = _partner && _partner.state || '';
        this.street = _partner && _partner.street || '';
        this.userName = _partner && _partner.userName || '';
        this.industrySector = _partner && _partner.industrySector || '';
        this.mobileNumber = _partner && _partner.mobileNumber || null;
        this.faxNumber = _partner && _partner.faxNumber || null;

        this.searchTerm1 = _partner && _partner.searchTerm1 || null;
        this.searchTerm2 = _partner && _partner.searchTerm2 || null;
        this.title = _partner && _partner.title || null;
        this._links = _partner && _partner._links || '';
    }


  /*
   Industry Sector Codes
   * Returns the string value of the Industry Sector code.

   */
  get industrySectorDescription(): string{
    switch (this.industrySector) {
      case '0' : return ' ';
      case '1' : return 'Power';
      case '2' : return 'Railways';
      case '3' : return 'Urban Infra';
      case '4' : return 'Roads';
      case '5' : return 'Ports';
      case '6' : return 'Oil & Gas';
      case '7' : return 'Corporates';
      case '8' : return 'Infrastructure';
      case '9' : return 'Others';
      case '10' : return 'Energy Supply / Distribution';
      case '11' : return 'Div. Holding comp';
      case '12' : return 'Raw Materials';
      case '13' : return 'Precious Metals';
      case '14' : return 'Financial Services';
      case '15' : return 'Real Estate';
      case '21' : return 'Chemical Industry';
      case '22' : return 'Health';
      case '23' : return 'Glass';
      case '24' : return 'Construction Industry';
      case '25' : return 'Building Supplier';
      case '26' : return 'Paper and Pulp';
      case '27' : return 'Timber and Infrastructure';
      case '31' : return 'Spinning Mill, Weaving Mill and Textile Refinement';
      case '32' : return 'Apparel';
      case '41' : return 'Iron and Steel';
      case '42' : return 'Vehicles';
      case '43' : return 'Vehicle Supplier';
      case '44' : return 'Mechanical Engineering';
      case '45' : return 'Specialized Mechanical Engineering';
      case '46' : return 'Machine Tool Engineering';
      case '47' : return 'Aircraft Construction';
      case '51' : return 'Breweries, Beverages, Tobacco';
      case '52' : return 'Nutrition';
      case '61' : return 'Electricals / Electrical Engineering';
      case '62' : return 'Computers and Data Processing';
      case '63' : return 'Software';
      case '64' : return 'Telecommunications';
      case '71' : return 'Consumer Products';
      case '72' : return 'Traffic and Transport';
      case '73' : return 'Leisure and Hotel';
      case '81' : return 'Commercial Banks';
      case '82' : return 'Mortgage Banks';
      case '83' : return 'Life Insurances';
      case '84' : return 'Non-Life Insurances';
      case '85' : return 'Reinsurances';
      case '86' : return 'Insurance Holdings';
      case '91' : return 'Trading';
      case '92' : return 'Pharmaceutical Trade';
      case '93' : return 'Publishing and Media';
      case '94' : return 'Environment ';

    }
  }
}
