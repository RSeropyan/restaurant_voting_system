package app.service.helpers;

public enum RestaurantSorter {
    ID("id"),           // default
    NAME("name"),
    VOTES("votes");

    private final String fieldName;

    RestaurantSorter(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
