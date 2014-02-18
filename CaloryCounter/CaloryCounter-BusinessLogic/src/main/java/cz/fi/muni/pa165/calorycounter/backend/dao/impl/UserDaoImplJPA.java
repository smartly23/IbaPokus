package cz.fi.muni.pa165.calorycounter.backend.dao.impl;

import cz.fi.muni.pa165.calorycounter.backend.dao.UserDao;
import cz.fi.muni.pa165.calorycounter.backend.model.AuthUser;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * JPA/Hibernate DAO implementation - for operations on the persistence layer on
 * User entities.
 *
 * @author Martin Pasko (smartly23)
 */
@Repository
public class UserDaoImplJPA implements UserDao {

    final static Logger log = LoggerFactory.getLogger(UserDaoImplJPA.class);
    // injected from Spring
    @PersistenceContext
    private EntityManager em;

    public UserDaoImplJPA() {
    }

    // this is only for legacy compatibility with some old tests
    public UserDaoImplJPA(EntityManager em) {
        this.em = em;
    }

    @Override
    public AuthUser getByUsername(String username) throws NoResultException {
        if (validate(username)) {
            throw new IllegalArgumentException("Invalid username: null");
        }
        TypedQuery<AuthUser> query;
        AuthUser returnedUser;
        query = em.createQuery("SELECT tbl FROM AuthUser tbl "
                + " WHERE tbl.username = :uname", AuthUser.class);
        query.setParameter("uname", username);
        returnedUser = query.getSingleResult();     // getSingleResult hadze NoResultException
        return returnedUser;
    }

    @Override
    public Long create(AuthUser user) {
        if (validate(user) || user.getUsername() == null) {
            throw new IllegalArgumentException("Invalid user: null or null username of user");
        }
        AuthUser createdUser = em.merge(user);
        return createdUser.getId();

    }

    @Override
    public AuthUser get(Long id) throws NoResultException {
        if (validate(id)) {
            throw new IllegalArgumentException("Invalid id: null");
        }
        if (em.createQuery("SELECT tbl.id FROM AuthUser tbl WHERE tbl.id = "
                + ":givenId", Long.class).setParameter("givenId", id).getResultList().size() < 1) {
            throw new IllegalArgumentException("Invalid id: nonexistent");
        }
        return em.createQuery("SELECT tbl FROM AuthUser tbl "
                + "WHERE tbl.id = :givenId", AuthUser.class).setParameter("givenId", id).getSingleResult();
        // nechceme vracat manazovanu entitu (return em.find(AuthUser.class, id)), treba vyuzivat CRUD metody
    }

    @Override
    public void update(AuthUser user) {
        if (validate(user) || user.getId() == null) {
            throw new IllegalArgumentException("Invalid user: null or with no id.");
        }
        String currentPass;
        try {
            currentPass = em.createQuery("SELECT tbl.password FROM AuthUser tbl WHERE tbl.id = "
                    + ":givenId", String.class).setParameter("givenId", user.getId()).getSingleResult();
        } catch (NoResultException e) {
            throw new IllegalArgumentException("Invalid user: nonexistent");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(currentPass);
        }
        em.merge(user);
    }

    @Override
    public void remove(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid user: null or with no id.");
        }
        AuthUser authUser = em.find(AuthUser.class, id);
        if (authUser == null) {
            log.error("Given user with id " + id + " is not in DB.");
        }
        em.remove(authUser);
    }

    private boolean validate(Object obj) {
        return obj == null;
    }

    @Override
    public boolean existsUsername(String username) throws NoResultException {
        TypedQuery<Long> query;
        query = em.createQuery("SELECT COUNT(tbl.username) FROM AuthUser tbl "
                + " WHERE tbl.username = :uname", Long.class);
        query.setParameter("uname", username);
        Long result = query.getSingleResult();
        if (result == 0) {
            return false;
        } else if (result == 1) {
            return true;
        }
        throw new RuntimeException("There is more than one identical username in the database.");
    }

    @Override
    public AuthUser login(String username, String password) {
        if (validate(username) || validate(password)) {
            throw new IllegalArgumentException("Invalid username or password: null");
        }
        TypedQuery<AuthUser> query;
        AuthUser returnedUser;
        query = em.createQuery("SELECT tbl FROM AuthUser tbl "
                + " WHERE tbl.username = :uname and tbl.password = :pword", AuthUser.class);
        query.setParameter("uname", username);
        query.setParameter("pword", password);
        try {
            returnedUser = query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
        return returnedUser;
    }

    @Override
    public List<AuthUser> getAll() {
        return em.createQuery("SELECT tbl FROM AuthUser tbl", AuthUser.class).getResultList();
    }
}
