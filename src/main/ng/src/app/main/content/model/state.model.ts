export class StateModel {

    private static states = [
        {code: "01" , value:"Andhra Pradesh"},
        {code: "02" , value:"Arunachal Pradesh"},
        {code: "03" , value:"Assam"},
        {code: "04" , value:"Bihar"},
        {code: "05" , value:"Goa"},
        {code: "06" , value:"Gujarat"},
        {code: "07" , value:"Haryana"},
        {code: "08" , value:"Himachal Pradesh"},
        {code: "09" , value:"Jammu und Kashmir"},
        {code: "10" , value:"Karnataka"},
        {code: "11" , value:"Kerala"},
        {code: "12" , value:"Madhya Pradesh"},
        {code: "13" , value:"Maharashtra"},
        {code: "14" , value:"Manipur"},
        {code: "15" , value:"Megalaya"},
        {code: "16" , value:"Mizoram"},
        {code: "17" , value:"Nagaland"},
        {code: "18" , value:"Orissa"},
        {code: "19" , value:"Punjab"},
        {code: "20" , value:"Rajasthan"},
        {code: "21" , value:"Sikkim"},
        {code: "22" , value:"Tamil Nadu"},
        {code: "23" , value:"Tripura"},
        {code: "24" , value:"Uttar Pradesh"},
        {code: "25" , value:"West Bengal"},
        {code: "26" , value:"Andaman und Nico.In."},
        {code: "27" , value:"Chandigarh"},
        {code: "28" , value:"Dadra und Nagar Hav."},
        {code: "29" , value:"Daman und Diu"},
        {code: "30" , value:"Delhi"},
        {code: "31" , value:"Lakshadweep"},
        {code: "32" , value:"Pondicherry"},
        {code: "33" , value:"Chhaattisgarh"},
        {code: "34" , value:"Jharkhand"},
        {code: "35" , value:"Uttaranchal"},
        {code: "36" , value:"Telangana"}
    ];

    /**
     * getStates()
     */
    public static getStates(): Array<Object> {
        return this.states;
    }

    /**
     * getState()
     */
    public static getState(stateCode: string): string {
        const state = this.states.find(state => state.code === stateCode);
        return state ? state.value : '';
    }
}
