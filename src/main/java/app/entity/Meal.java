package app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    private MealCategory category;

    private Integer price;

    @JsonBackReference
    @ManyToOne
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
                restaurant.equals(meal.restaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, restaurant);
    }

}
