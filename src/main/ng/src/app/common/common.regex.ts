// Tax percentage regex
export const TAX_PERCENTAGE_REGEX = /^\d{1,2}(\.\d{1,2})?$/; // 1-2 digits followed by 1-2 digits after decimal point
// Share holding percentage regex
export const SHARE_HOLDING_PERCENTAGE_REGEX = /^\d{1,3}(\.\d{1,2})?$/; // 1-3 digits followed by 1-2 digits after decimal point

// Mobile number regex
export const MOBILE_NUMBER_REGEX = /^[6-9]\d{9}$/; // 10 digit mobile number starting with 6-9

// Phone number regex
export const PHONE_NUMBER_REGEX = /^\d{10,11}$/; // 10-11 digit phone number

// Numeric only regex
export const NUMERIC_ONLY_REGEX = /^[0-9]+$/; // Only numbers

// Alpha numeric only regex
export const ALPHA_NUMERIC_ONLY_REGEX = /^[a-zA-Z0-9]+$/; // Only letters and numbers

// Alpha only regex
export const ALPHA_ONLY_REGEX = /^[a-zA-Z]+$/; // Only letters

// Email regex
export const EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

// Decimal number regex
export const FIVE_COMMA_TWO = /^\d{1,5}(\.\d{0,2})?$/; // 1-5 digits before decimal point, 0-2 digits after decimal point (max 18 total including decimal)
export const SEVEN_COMMA_TWO = /^\d{1,7}(\.\d{0,2})?$/; // 1-7 digits before decimal point, 0-2 digits after decimal point (max 18 total including decimal)
export const FIFTEEN_COMMA_TWO = /^\d{1,15}(\.\d{0,2})?$/; // 1-15 digits before decimal point, 0-2 digits after decimal point (max 18 total including decimal)