package app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

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

    public Meal(Integer id, String name, MealCategory category, Integer price, Restaurant restaurant) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.restaurant = restaurant;
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
}
