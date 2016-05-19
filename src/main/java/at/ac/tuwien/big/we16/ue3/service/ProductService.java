package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.exception.ProductNotFoundException;
import at.ac.tuwien.big.we16.ue3.model.Bid;
import at.ac.tuwien.big.we16.ue3.model.Product;
import at.ac.tuwien.big.we16.ue3.model.User;
import at.ac.tuwien.big.we16.ue3.productdata.JSONDataLoader;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProductService {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");

    @PersistenceContext(unitName = "ProductService")
    EntityManager em = entityManagerFactory.createEntityManager();


    public Collection<Product> getAllProducts() {

        List<Product> products = null;

        try{
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
            EntityManager em = entityManagerFactory.createEntityManager();

            try {
                em.getTransaction().begin();
                products = em.createNamedQuery("getAllProducts", Product.class).getResultList();
                em.getTransaction().commit();

            }catch (Exception e){
                if(em.getTransaction().isActive()){
                    em.getTransaction().rollback();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeRessources();
        }


        return products;
    }

    public Product getProductById(String id) throws ProductNotFoundException {

        Product product = null;

        try{
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
            EntityManager em = entityManagerFactory.createEntityManager();

            try {
                em.getTransaction().begin();
                product = em.find(Product.class, id);
                em.getTransaction().commit();

            }catch (Exception e){
                if(em.getTransaction().isActive()){
                    em.getTransaction().rollback();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeRessources();
        }

        return product;
    }

    //TODO: write changed users and products to db
    // ????
    public Collection<Product> checkProductsForExpiration() {
        Collection<Product> newlyExpiredProducts = new ArrayList<>();
        for (Product product : this.getAllProducts()) {
            try {
                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
                EntityManager em = entityManagerFactory.createEntityManager();

                try {
                    em.getTransaction().begin();
                    Product product1 = em.find(Product.class, product.getId());

                    if (!product.hasExpired() && product.hasAuctionEnded()) {
                        product.setExpired();
                        newlyExpiredProducts.add(product);
                        if (product.hasBids()) {
                            Bid highestBid = product.getHighestBid();
                            for (User user : product.getUsers()) {

                                //em.getTransaction().begin();
                                User user1 = em.find(User.class, user.getId());

                                user1.decrementRunningAuctions();
                                if (highestBid.isBy(user1)) {
                                    user1.incrementWonAuctionsCount();
                                } else {
                                    user1.incrementLostAuctionsCount();
                                }
                                //em.getTransaction().commit();
                            }
                        }
                    }
                    //update product
                    em.getTransaction().commit();


                } catch (Exception e) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeRessources();
            }
        }


        return newlyExpiredProducts;
    }

    public void createProduct(Product p){

        try{
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
            EntityManager em = entityManagerFactory.createEntityManager();
            try {
                em.getTransaction().begin();
                em.persist(p);
                em.getTransaction().commit();
            }catch (Exception e){
                if(em.getTransaction().isActive()){
                    em.getTransaction().rollback();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeRessources();
        }



        closeRessources();
    }

    private void closeRessources(){
        em.close();
        entityManagerFactory.close();
    }
}
