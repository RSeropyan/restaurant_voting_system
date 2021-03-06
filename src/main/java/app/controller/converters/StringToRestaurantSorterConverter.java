package app.controller.converters;

import app.service.helpers.RestaurantSorter;
import org.springframework.core.convert.converter.Converter;

public class StringToRestaurantSorterConverter implements Converter<String, RestaurantSorter> {

    @Override
    public RestaurantSorter convert(String source) {
        if (source.equalsIgnoreCase("id")) {
            return RestaurantSorter.ID;
        }
        else if (source.equalsIgnoreCase("name")) {
            return RestaurantSorter.NAME;
        }
        else if (source.equalsIgnoreCase("votes")) {
            return RestaurantSorter.VOTES;
        }
        else {
            throw new IllegalArgumentException("Incorrect value of 'sort' request parameter. Use 'id', 'name' or 'votes' value.");
        }
    }

}
