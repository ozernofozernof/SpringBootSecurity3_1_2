package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.exception.EntityNotFoundException;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public UserDaoImpl(EntityManager entityManager) {

        this.entityManager = entityManager;
    }

    @Override
    public User getUserByEmail(String email) {
        return entityManager.createQuery(
                        "SELECT user FROM User user join fetch  user.roles WHERE user.email =:email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public User getUserById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void removeUserById(Long id) {
        entityManager.remove(entityManager.find(User.class, id));
    }

    @Override
    public List<User> listUsers() {
        return entityManager.createQuery("From User", User.class).getResultList();
    }
}