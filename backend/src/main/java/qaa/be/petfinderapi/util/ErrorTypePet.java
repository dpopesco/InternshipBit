package qaa.be.petfinderapi.util;

public class ErrorTypePet {

    public static final String FIRST_NAME_LENGTH = "'First Name' must be between 1 and 256 characters. You entered 257 characters.";
    public static final String LAST_NAME_LENGTH = "'Last Name' must be between 1 and 256 characters. You entered 257 characters.";
    public static final String PASSWORD_LENGTH = "'Password' must be between 1 and 30 characters.";
    public static final String PHONE_LENGTH = "'Phone Number' must be between 1 and 15 characters. You entered 16 characters.";
    public static final String FIRST_NAME_TOO_SHORT = "First name is required. 'First Name' must be between 1 and 256 characters. You entered 0 characters.";
    public static final String LAST_NAME_TOO_SHORT = "Last name is required. 'Last Name' must be between 1 and 256 characters. You entered 0 characters.";
    public static final String PHONE_NUMBER_TOO_SHORT = "Phone number is required. 'Phone Number' must be between 1 and 15 characters. You entered 0 characters.";
    public static final String PASSWORD_REQUIRED = "Password is required. Password should contain at least one UpperCase letter, one number, and one special character.";
    public static final String EMAIL_TOO_SHORT = "Email address is required. A valid email address is required. 'Email' must be between 1 and 100 characters. You entered 0 characters.";
    public static final String EMAIL_LENGTH = "'Email' must be between 1 and 100 characters. You entered 101 characters.";
    public static final String FIRST_NAME_NOT_VALID = "A valid first name is required.";
    public static final String LAST_NAME_NOT_VALID = "A valid last name is required.";
    public static final String EMAIL_NOT_VALID = "A valid email address is required.";
    public static final String PHONE_NOT_VALID = "A valid phone number is required.";
    public static final String EMAIL_DUPLICATE = "Violation of UNIQUE KEY constraint 'UQ__Users__A9D105344D4C309B'. Cannot insert duplicate key in object 'dbo.Users'.";
    public static final String FIRST_NAME_LETTERS = "'First Name' must contain letters.";
    public static final String LAST_NAME_LETTERS = "'Last Name' must contain letters.";
    public static final String FIRST_NAME_REQUIRED_ERROR = "[The FirstName field is required.]";
    public static final String LAST_NAME_REQUIRED_ERROR = "[The LastName field is required.]";
    public static final String EMAIL_REQUIRED_ERROR = "[The Email field is required.]";
    public static final String PASSWORD_REQUIRED_ERROR = "[The Password field is required.]";
    public static final String PHONE_REQUIRED_ERROR = "[The PhoneNumber field is required.]";
    public static final String FIRST_NAME_REQUIRED_DETAIL = "First name is required.";
    public static final String LAST_NAME_REQUIRED_DETAIL = "Last name is required.";
    public static final String EMAIL_REQUIRED_DETAIL = "Email address is required. A valid email address is required.";
    public static final String PHONE_REQUIRED_DETAIL = "Phone number is required.";
    public static final String PASSWORD_NOT_VALID = "Password should contain at least one UpperCase letter, one number, and one special character.";
    public static final String PASSWORD_REQUESTED_DETAIL = "Password is required";

}
