package com.example.demo4.service;

import com.example.demo4.model.User;
import com.example.demo4.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<String, User> userStorage = new HashMap<>();
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;

        // Initialize the userStorage map in the constructor
        User user1 = new User("1", "Alice", "alice@example.com");
        User user2 = new User("2", "Bob", "bob@example.com");
        User user3 = new User("3", "Charlie", "charlie@example.com");

        userStorage.put(user1.getId(), user1);
        userStorage.put(user2.getId(), user2);
        userStorage.put(user3.getId(), user3);
    }

    public Flux<User> getAllUsers() {
         return userRepository.findAll();
//        Flux<User> flux = Flux.fromIterable(userStorage.values());

//        System.out.println("Type of the returned Flux: " + flux.getClass());
//
//        return flux;

    }

    public Mono<User> getUserById(String id) {
        // return userRepository.findById(id);
        return Mono.justOrEmpty(userStorage.get(id));
    }

    public Mono<User> saveUser(User user) {
         return userRepository.save(user);
//        userStorage.put(user.getId(), user);
//        return Mono.just(user);
    }

    public Mono<Void> deleteUser(String id) {
        // return userRepository.deleteById(id);
        userStorage.remove(id);
        return Mono.empty();
    }
}
