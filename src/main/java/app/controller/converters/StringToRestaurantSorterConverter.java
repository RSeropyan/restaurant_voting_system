package app.controller.converters;

import app.service.helpers.RestaurantSorter;
import org.springframework.core.convert.converter.Converter;

public class StringToRestaurantSorterConverter implements Converter<String, RestaurantSorter> {
    @Override
    public RestaurantSorter convert(String source) {
        switch (source) {
            case "id":
                return RestaurantSorter.ID;
            case "name":
                return RestaurantSorter.NAME;
            case "votes":
            default: return RestaurantSorter.VOTES;
        }
    }
}
