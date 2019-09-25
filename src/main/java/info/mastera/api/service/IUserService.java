package info.mastera.api.service;

import info.mastera.model.User;

public interface IUserService extends IGenericService<User> {

    User getByUsername(String userName);
}