package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue3.model.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    /*
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");

    @PersistenceContext(unitName = "UserService")
    EntityManager em = entityManagerFactory.createEntityManager();
    */

    public void createUser(User user) {

        //TODO: write to db
        // ???
        EntityManagerFactory entityManagerFactory = null;
        EntityManager em = null;

        try{
            entityManagerFactory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
            em = entityManagerFactory.createEntityManager();

            try {
                em.getTransaction().begin();
                em.persist(user);
                em.getTransaction().commit();
            }catch (Exception e){
                if(em.getTransaction().isActive()){
                    em.getTransaction().rollback();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(em != null)
                em.close();

            if(entityManagerFactory != null)
                entityManagerFactory.close();
        }
    }

    public User getUserByEmail(String email) throws UserNotFoundException {

        //TODO: read from db
        // ???
        User user = new User();

        EntityManagerFactory entityManagerFactory = null;
        EntityManager em = null;

        try{
            entityManagerFactory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
            em = entityManagerFactory.createEntityManager();

            try {
                em.getTransaction().begin();
                TypedQuery<User> userTypedQuery = em.createQuery("select u from User u where u.email like :email", User.class);
                userTypedQuery.setParameter("email", email);
                em.getTransaction().commit();

                if(userTypedQuery.getResultList().size() > 0)
                    user = userTypedQuery.getResultList().get(1);
                else
                    throw new UserNotFoundException();

            }catch (Exception e){
                if(em.getTransaction().isActive()){
                    em.getTransaction().rollback();
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(em != null)
                em.close();

            if(entityManagerFactory != null)
                entityManagerFactory.close();
        }

        return user;
    }

}
