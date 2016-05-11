package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue3.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    @PersistenceContext(unitName = "UserService")
    EntityManager em;

    public void createUser(User user) {

        //TODO: write to db
        // ????
        em.persist(user);
    }

    public User getUserByEmail(String email) throws UserNotFoundException {

        //TODO: read from db
        TypedQuery<User> userTypedQuery = em.createQuery("select * from user u where u.email like :email", User.class);
        userTypedQuery.setParameter("email", email);
        if(userTypedQuery.getResultList() != null)
            return userTypedQuery.getResultList().get(1);
        return null;
    }


}
