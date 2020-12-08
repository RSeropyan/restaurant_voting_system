package app.controller.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;

public class StringToSortDirectionConverter implements Converter<String, Sort.Direction> {
    @Override
    public Sort.Direction convert(String source) {
        return source.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    }
}
