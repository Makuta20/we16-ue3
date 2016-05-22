package at.ac.tuwien.big.we16.ue3.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "RelatedProduct")
public class RelatedProduct extends BaseEntity{

    private String name;

    @ManyToOne
    private Product product;

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
