package app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Integer votes;

    @JsonManagedReference
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals;

    public Restaurant() {
    }

    public Restaurant(String name, Integer votes, List<Meal> meals) {
        this.name = name;
        this.votes = votes;
        this.meals = meals;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", votes=" + votes +
                '}';
    }

    public void addMeal(Meal meal) {
        meals.add(meal);
        meal.setRestaurant(this);
    }

    public void removeMeal(Meal meal) {
        meals.remove(meal);
        meal.setRestaurant(null);
    }
    public void removeAllMeals() {
        meals.clear();
    }

}
