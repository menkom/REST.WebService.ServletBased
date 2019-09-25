package info.mastera.controller;

import info.mastera.api.service.IUserService;
import info.mastera.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    UserDetailsService userDetailsService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getById(id);
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody User user) throws ServerErrorException {
        if (userService.getByUsername(user.getName()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("There is User with " + user.getName() + "name in system.");
        }
        userService.create(user);
        return ResponseEntity.ok().body("Item created.");
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok().body(userService.getAll());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        User user = userService.getById(id);
        if (user != null) {
            userService.delete(user);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    @PostMapping("/login")
    public boolean login(@RequestBody User user) {
        boolean isValidUser = false;
        try {
            UserDetails userFromBase = userDetailsService.loadUserByUsername(user.getName());
            if (userFromBase != null && user.getPassword().equals(userFromBase.getPassword())) {
                isValidUser = true;
            }
        } catch (UsernameNotFoundException e) {
        }
        return isValidUser;
    }

    @GetMapping("/role/{username}")
    public ResponseEntity<User> readByLogin(@PathVariable("username") String username) throws ServerErrorException {
        return ResponseEntity.ok().body(userService.getByUsername(username));
    }
}