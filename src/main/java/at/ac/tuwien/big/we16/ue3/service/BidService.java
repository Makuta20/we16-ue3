package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.exception.InvalidBidException;
import at.ac.tuwien.big.we16.ue3.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue3.model.Bid;
import at.ac.tuwien.big.we16.ue3.model.Product;
import at.ac.tuwien.big.we16.ue3.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

public class BidService {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");

    @PersistenceContext(unitName = "BidService")
    EntityManager em = entityManagerFactory.createEntityManager();

    public void makeBid(User user, Product product, int centAmount) throws InvalidBidException, UserNotFoundException {
        if (product.hasAuctionEnded() || !product.isValidBidAmount(centAmount) || !user.hasSufficientBalance(centAmount)) {
            throw new InvalidBidException();
        }

        // possible cases:
        // * product has no bids
        //   -> decrease balance by total, increment running
        // * product's highest bid is by the user
        //   -> decrease balance by diff, don't increment running
        // * some other bid on the product is by the user
        //   -> decrease balance by total, don't increment running, reimburse the current highest bidder
        // * product has bids, but none by the user
        //   -> decrease balance by total, increment running, reimburse the current highest bidder


        int decreaseAmount = centAmount;
        User highestBidder = em.find(User.class, product.getHighestBid().getUser().getId());
        User user1 = em.find(User.class, user.getId());
        //Product product1 = em.find(Product.class, product.getId());

        if (product.hasBids()) {
            if (product.getHighestBid().isBy(user)) {
                // The given user already is the highest bidder, so we only substract the difference.
                decreaseAmount = centAmount - product.getHighestBid().getAmount();
            }
            else {
                // TODO reimburse current highest bidder
                highestBidder.increaseBalance(product.getHighestBid().getAmount());

                ServiceFactory.getNotifierService().notifyReimbursement(highestBidder);
            }
        }

        if (!product.hasBidByUser(user)) {
            user1.incrementRunningAuctions();
        }

        user1.decreaseBalance(decreaseAmount);
        Bid bid = new Bid(centAmount, user1);

        //TODO write to db

        em.getTransaction().begin();
        em.persist(bid);
        em.getTransaction().commit();

        closeRessources();

        ServiceFactory.getNotifierService().notifyAllAboutBid(bid);
    }

    public void makeBid(User user, Product product, BigDecimal amount) throws InvalidBidException, UserNotFoundException {
        try {
            int centAmount = amount.movePointRight(2).intValueExact();
            this.makeBid(user, product, centAmount);
        } catch (ArithmeticException e) {
            throw new InvalidBidException();
        }
    }

    private void closeRessources(){
        em.close();
        entityManagerFactory.close();
    }
}
