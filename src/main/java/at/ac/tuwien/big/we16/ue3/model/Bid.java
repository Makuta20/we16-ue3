package at.ac.tuwien.big.we16.ue3.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Bid extends BaseEntity{

    private int amount;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    public Bid(int centAmount, User user) {
        amount = centAmount;
        this.user = user;
    }

    public int getAmount() {
        return amount;
    }

    public float getConvertedAmount() {
        float convertedAmount = (float)this.amount;
        return convertedAmount / 100;
    }

    public User getUser() {
        return user;
    }

    public boolean isBy(User user) {
        return this.user.equals(user);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
