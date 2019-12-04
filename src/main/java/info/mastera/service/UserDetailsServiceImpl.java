package info.mastera.service;

import info.mastera.api.service.IUserService;
import info.mastera.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to receive User credentials
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    IUserService userService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("Username can't be empty");
        }
        User user = userService.getByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(MessageFormat.format("Username {0} not found.", username));
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getUserType().name()));
        return new org.springframework.security.core.userdetails.User(user.getName(),
                passwordEncoder.encode(user.getPassword()),
                grantedAuthorities);
    }
}