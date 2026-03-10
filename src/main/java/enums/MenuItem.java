package enums;

public enum MenuItem {

    SESSIONS("sessions"),
    IDENTITIES("identities"),
    AUTHENTICATIONS("authentications"),
    ANALYTICS("analytics"),
    FLOWS("flows"),
    WORKFLOWS("workflows"),
    COMPLIANCE("compliance"),
    WATCHLIST("customWatchlist"),
    USERS("users"),
    STATUS("servicesHealth"),
    CONFIGURATION("configuration");

    private final String value;

    MenuItem(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
