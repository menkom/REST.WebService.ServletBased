package info.mastera.dao;

import info.mastera.api.dao.IUserDao;
import info.mastera.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class UserDao extends AbstractDao<User> implements IUserDao {

    @Override
    protected Class<User> getTClass() {
        return User.class;
    }

    @Override
    public User getByUsername(String userName) {
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(builder.equal(root.get("name"), userName));
        Query<User> result = session.createQuery(query);
        return result.uniqueResult();
    }
}