package qaa.be.test.dummyapi.user;

public abstract class UserApiConstants {

    protected static final String ERROR_DATA_MESSAGE_EMPTY_REQUIRED_FIELDS = "Path `%s` is required.";

    protected static final String ERROR_LESS_THAN_MIN_FIELD =
            "Path `%s` (`%s`) is shorter than the minimum allowed length (%s).";
    protected static final String ERROR_DATA_MESSAGE_WRONG_GENDER = "`%s` is not a valid enum value for path `gender`.";

    protected static final String ERROR_MORE_THAN_MAX_FIELD =
            "Path `%s` (`%s`) is longer than the maximum allowed length (%s).";
    protected static final String ERROR_DATA_MESSAGE_WRONG_FORMAT_EMAIL = "Path `email` is invalid (%s).";
    protected static final String ERROR_DATA_MESSAGE_WRONG_TITLE = "`%s` is not a valid enum value for path `title`.";
    protected static final String ERROR_DATA_MESSAGE_TOO_LOW_DATE_OF_BIRTH =
            "Path `dateOfBirth` (Sun Dec 31 1899 00:00:00 GMT+0000 (Coordinated Universal Time)) is " +
                    "before minimum allowed value (Mon Jan 01 1900 00:00:00 GMT+0000 (Coordinated Universal Time)).";
    protected static final String ERROR_DATA_MESSAGE_AFTER_MAX_DATE_OF_BIRTH =
            "Path `dateOfBirth` (%s GMT+0000 (Coordinated Universal Time))" +
                    " is after maximum allowed value";

    protected static final String ERROR_DATA_MESSAGE_WRONG_DATE_OF_BIRTH =
            "Cast to date failed for value \"%s\" (type string) at path \"dateOfBirth\"";
    protected static final String MIN_FIRST_OR_LAST_NAME = "2";
    protected static final String MAX_FIRST_OR_LAST_NAME = "30";
    protected static final String MIN_PHONE = "5";
    protected static final String MAX_PHONE = "20";
}
