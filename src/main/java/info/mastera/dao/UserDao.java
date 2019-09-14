package info.mastera.dao;

import info.mastera.api.dao.IUserDao;
import info.mastera.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends AbstractDao<User> implements IUserDao {

    @Override
    protected Class<User> getTClass() {
        return User.class;
    }
}