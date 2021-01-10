package app.service.utils;

import org.springframework.data.domain.Sort;

public class PaginationSettings {
    public static final Integer DEFAULT_CURRENT_PAGE = 0;
    public static final Integer DEFAULT_PAGE_SIZE = 100;
    public static final RestaurantSorter DEFAULT_SORTED_BY = RestaurantSorter.VOTES;
    public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.DESC;
}
