package info.mastera.service;

import info.mastera.api.dao.IUserDao;
import info.mastera.api.service.IUserService;
import info.mastera.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Override
    public User create(User entity) {
        return userDao.create(entity);
    }

    @Override
    public void delete(User entity) {
        userDao.delete(entity);
    }

    @Override
    public void update(User entity) {
        userDao.update(entity);
    }

    @Override
    public User getById(int id) {
        return userDao.getById(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public Long count() {
        return userDao.count();
    }

    @Override
    public User getByUsername(String userName) {
        return userDao.getByUsername(userName);
    }
}