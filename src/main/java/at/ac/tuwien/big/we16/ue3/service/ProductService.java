package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.exception.ProductNotFoundException;
import at.ac.tuwien.big.we16.ue3.model.Bid;
import at.ac.tuwien.big.we16.ue3.model.Product;
import at.ac.tuwien.big.we16.ue3.model.User;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collection;

public class ProductService {

    @PersistenceContext(unitName = "ProductService")
    EntityManager em ;


    public Collection<Product> getAllProducts() {
        return em.createNamedQuery("getAllProducts", Product.class).getResultList();
    }

    public Product getProductById(long id) throws ProductNotFoundException {
        TypedQuery<Product> productTypedQuery = em.createQuery("select p from product p where p.id like :id", Product.class);
        productTypedQuery.setParameter("id", id);
        if(productTypedQuery.getResultList() != null)
            return productTypedQuery.getResultList().get(1);
        return null;
    }

    //TODO: write changed users and products to db
    public Collection<Product> checkProductsForExpiration() {
        Collection<Product> newlyExpiredProducts = new ArrayList<>();
        for (Product product : this.getAllProducts()) {
            if (!product.hasExpired() && product.hasAuctionEnded()) {
                product.setExpired();
                newlyExpiredProducts.add(product);
                if (product.hasBids()) {
                    Bid highestBid = product.getHighestBid();
                    for (User user : product.getUsers()) {
                        user.decrementRunningAuctions();
                        if (highestBid.isBy(user)) {
                            user.incrementWonAuctionsCount();
                        }
                        else {
                            user.incrementLostAuctionsCount();
                        }
                    }
                }
            }
        }
        return newlyExpiredProducts;
    }
}
