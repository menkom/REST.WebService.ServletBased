package info.mastera.service;

import info.mastera.dao.UserDao;
import info.mastera.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user;
        try {
            user = userDao.getByUsername(login);
            if (user == null) {
                throw new UsernameNotFoundException("User not found or invalid username/password.");
            } else {
                Set<GrantedAuthority> roles = new HashSet<>();
                roles.add(new SimpleGrantedAuthority(user.getUserType().name()));
                return new org.springframework.security.core.userdetails
                        .User(user.getName(), user.getPassword(), roles);
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found or invalid username/password.", e);
        }
    }
}