package qaa.fe.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import qaa.fe.saucedemo.checkout.CheckoutProductComponent;
import qaa.fe.saucedemo.inventory.ProductComponent;

@Getter
@Setter
@EqualsAndHashCode
public class Product {

    private String name;
    private String description;
    private Double price;
    String imageUrl;
    boolean isAddToCart;

    public Product(ProductComponent productComponent) {
        name = productComponent.getName();
        description = productComponent.getDescription();
        price = productComponent.getPrice();
    }

    public Product(CheckoutProductComponent checkoutProductComponent) {
        name = checkoutProductComponent.getName();
        description = checkoutProductComponent.getDescription();
        price = checkoutProductComponent.getPrice();
    }

    public Product(String name, String description, String imageUrl, Double price, boolean isAddToCart) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.isAddToCart = isAddToCart;
    }
}
