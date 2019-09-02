package info.mastera.controller;

import info.mastera.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public User getCustomer() {
        return new User()
                .setName("customerName")
                .setTelephone("tel");
    }

    @GetMapping("/all")
    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        list.add(new User()
                .setName("customer1")
                .setTelephone("telNum"));
        return list;
    }
}