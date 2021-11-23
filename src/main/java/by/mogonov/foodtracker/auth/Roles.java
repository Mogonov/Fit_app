package by.mogonov.foodtracker.auth;

public enum Roles {
    ROLE_USER("USER"), ROLE_ADMIN("ADMIN");

    private String value;

    Roles(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
