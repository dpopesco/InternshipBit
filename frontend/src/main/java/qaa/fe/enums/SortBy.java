package qaa.fe.enums;

public enum SortBy {
    NAME_ASC("Name (A to Z)"),
    NAME_DESC("Name (Z to A)"),
    PRICE_ASC("Price (low to high)"),
    PRICE_DESC("Price (high to low)");

    private final String type;

    SortBy(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
