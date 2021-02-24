package app.entity;

import app.controller.views.RestaurantView;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;

@Entity
@JsonView(RestaurantView.Detailed.class)
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Meal name must not be blank.")
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Meal category must be selected.")
    private MealCategory category;

    @NotNull(message = "Meal price must be specified.")
    @PositiveOrZero(message = "Meal price must be greater or equal to zero.")
    private Integer price;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "restaurant")
    private Restaurant restaurant;

    public Meal() {
    }

    public Meal(String name, MealCategory category, Integer price) {
        this.name = name;
        this.category = category;
        this.price = price;
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

    public MealCategory getCategory() {
        return category;
    }

    public void setCategory(MealCategory category) {
        this.category = category;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return name.equals(meal.name) &&
                category == meal.category &&
                restaurant.getName().equals(meal.restaurant.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, restaurant.getName());
    }

}
