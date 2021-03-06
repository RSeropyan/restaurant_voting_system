package app.controller.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;

public class StringToSortDirectionConverter implements Converter<String, Sort.Direction> {

    @Override
    public Sort.Direction convert(String source) {
        if (source.equalsIgnoreCase("asc")) {
            return Sort.Direction.ASC;
        }
        else if (source.equalsIgnoreCase("desc")) {
            return Sort.Direction.DESC;
        }
        else {
            throw new IllegalArgumentException("Incorrect value of 'sdir' request parameter. Use 'asc' or 'desc' value.");
        }
    }

}
