package app.dao;

import app.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @QueryHints(value = { @QueryHint(name = "hibernate.query.passDistinctThrough", value = "false")})
    @Query("SELECT DISTINCT r from Restaurant r left join fetch r.meals where r.id = ?1")
    Restaurant getRestaurantById(Integer id);

}
