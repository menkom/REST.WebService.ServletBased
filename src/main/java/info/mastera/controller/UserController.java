package info.mastera.controller;

import info.mastera.api.service.IUserService;
import info.mastera.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * Get User item by id
     */
    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getById(id);
    }

    /**
     * Get all Users
     */
    @GetMapping()
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok().body(userService.getAll());
    }

    /**
     * Delete User by id
     */
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

    /**
     * Get User by name
     */
    @GetMapping("/role/{username}")
    public ResponseEntity<User> readByLogin(@PathVariable("username") String username) throws ServerErrorException {
        return ResponseEntity.ok().body(userService.getByUsername(username));
    }
}