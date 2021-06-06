package com.example.restfulwebservice.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserDaoService {
    private static List<User> users = new ArrayList<>();

    private static int usersCount = 3;

    static {
        users.add(new User( 1, "name", new Date(),"1111","111111-1111111"));
        users.add(new User( 2, "name2", new Date(),"2222","222222-2222222"));
        users.add(new User( 3, "name3", new Date(),"3333","333333-3333333"));
    }

    public List<User> findAll() {
        return users;
    }

    public User save(User user){
        if(user.getId() == null){
            user.setId(++usersCount);
        }

        users.add(user);
        return user;
    }

    public User findOne(int id){
        for(User user : users){
            if(user.getId() == id)
                return user;
        }
        return null;
    }

    public User deleteById(int id){
        Iterator<User> iterator = users.iterator();

        while(iterator.hasNext()){
            User user = iterator.next();

            if (user.getId() == id){
                iterator.remove();
                return user;
            }
        }

        return null;
    }

    public User modifiedById(User modified_user){
        Iterator<User> iterator = users.iterator();

        while(iterator.hasNext()){
            User user = iterator.next();

            if (user.getId() == modified_user.getId()){
                if(modified_user.getId() != null)
                    user.setId(modified_user.getId());
                if(modified_user.getName() != null)
                    user.setName(modified_user.getName());
                if(modified_user.getJoinDate() != null)
                    user.setJoinDate(modified_user.getJoinDate());

                return user;
            }
        }

        return null;
    }
}
