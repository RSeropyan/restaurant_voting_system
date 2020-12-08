package app.service.utils;

public enum RestaurantSorter {
    byID("id"),           // default
    byNAME("name"),
    byVOTES("votes");

    private final String fieldName;

    RestaurantSorter(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
