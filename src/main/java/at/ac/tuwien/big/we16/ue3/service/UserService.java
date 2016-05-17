package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue3.model.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");

    @PersistenceContext(unitName = "UserService")
    EntityManager em = entityManagerFactory.createEntityManager();

    public void createUser(User user) {

        //TODO: write to db
        // ???
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();

        closeRessources();
    }

    public User getUserByEmail(String email) throws UserNotFoundException {

        //TODO: read from db
        // ???
        em.getTransaction().begin();
        TypedQuery<User> userTypedQuery = em.createQuery("select u from user u where u.email like :email", User.class);
        userTypedQuery.setParameter("email", email);
        em.getTransaction().commit();

        User user = null;

        if(userTypedQuery.getResultList().size() > 0)
            user = userTypedQuery.getResultList().get(1);

        closeRessources();

        return user;
    }

    private void closeRessources(){
        em.close();
        entityManagerFactory.close();
    }

}
