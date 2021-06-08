package com.example.restfulwebservice.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/jpa")
public class UserJpaController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> retrieveAllusers(){
        return userRepository.findAll();
    }

    @GetMapping("users/{id}")
    public Resource<User> retrieveUser(@PathVariable int id) {
        Optional<User> oUser = userRepository.findById(id);
        if(!oUser.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not Found",id));
        }

        Resource<User> resource = new Resource<>(oUser.get());
        resource.add(linkTo(methodOn(this.getClass()).retrieveAllusers()).withRel("all-users"));

        return resource;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
    }

    @PostMapping("users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users/{id}/post")
    public List<Post> retrieveAllPostsByUser(@PathVariable int id){
        Optional<User> oUser = userRepository.findById(id);
        if(!oUser.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not Found",id));
        }

        return oUser.get().getPosts();
    }
}
