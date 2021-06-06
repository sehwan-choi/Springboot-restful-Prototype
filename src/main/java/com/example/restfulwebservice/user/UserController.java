package com.example.restfulwebservice.user;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserController {
    private UserDaoService service;

    public UserController(UserDaoService service){
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public Resource<User> retrieveUser(@PathVariable int id){
        User user = service.findOne(id);

        if(user == null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
        
        // HATEOAS

        Resource<User> resource = new Resource<>(user);
        //ntrollerLinkBuilder linkTo = ControllerLinkBuilder.linkTo(methodOn(this.getClass()).retrieveAllUsers());
        
        resource.add(
                linkTo(methodOn(this.getClass()).retrieveAllUsers()).withRel("all-users"),
                linkTo(methodOn(this.getClass()).createUser(user)).withRel("create-user"),
                linkTo(methodOn(this.getClass()).deleteUser(0)).withRel("update-user"),
                linkTo(methodOn(this.getClass()).updateUser(user)).withRel("delete-user"));

        return resource;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User saveUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saveUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id){
        User user = service.deleteById(id);

        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        User updateUser = service.modifiedById(user);

        if(updateUser == null)
            throw new UserNotFoundException(String.format("ID[%s] not found",user.getId()));
        return ResponseEntity.ok(user);
    }
}
