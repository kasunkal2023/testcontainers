//package com.example.demo4.controller;
//
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.reactive.server.WebTestClient;
//
//@RunWith(SpringRunner.class)
//@WebFluxTest(UserController.class)  // Specify the controller class to test
//public class UserControllerTest {
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @Test
//    public void getAllUsers() {
//        webTestClient.get().uri("/api/users").exchange()
//                .expectStatus().isOk();
////                .expectBody(Flux.class)
////                .hasSize(3)
////                .as(StepAssertions::body)
////                .contains(new User("1", "Alice", "alice@example.com"));
//    }
//}


package com.example.demo4.controller;
import com.example.demo4.model.User;
import com.example.demo4.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
//@WebFluxTest(UserController.class)  // Specify the controller class to test

//@Configuration
//@ContextConfiguration
@SpringBootTest(classes = UserController.class)
@ComponentScan(basePackages = "com.example.demo4")
@AutoConfigureWebTestClient


//@WebFluxTest(UserController.class)
public class UserControllerTest {


    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

//        @Test
//        public void getAllUsers() {
//
//            Mockito.when(userService.getAllUsers())
//                    .thenReturn(Flux.just(new User("1", "Alice", "alice@example.com")));
//
//            webTestClient.get().uri("/api/users").exchange()
//                    .expectStatus().isOk()
//                    .expectBody(Flux.class).isEqualTo(Flux.just(new User("1", "Alice", "alice@example.com")));
//        }

    @Test
    public void getAllUsers() {
        User expectedUser = new User("1", "Alice", "alice@example.com");

        //get all users from the user repository

//        userService.getAllUsers().subscribe(System.out::println);

        // Arrange: Set up the mock behavior for UserService
        Mockito.when(userService.getAllUsers())
                .thenReturn(Flux.just(expectedUser));

        // Act and Assert
        webTestClient.get().uri("/api/users")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(User.class)
                .value(userList -> {
                    // Use StepVerifier to perform assertions on the Flux
                    StepVerifier.create(Flux.fromIterable(userList))
                            .expectNext(expectedUser)
                            .verifyComplete();
                });
    }


    @Test
    public void getUserById() {

        Mockito.when(userService.getUserById("1"))
                .thenReturn(Mono.just(new User("1", "Alice", " alico@gmail.com")));

        webTestClient.get().uri("/api/users/1").exchange()
                .expectStatus().isOk();
//                .expectBody(Flux.class)

    }

    @Test
    public void saveUser() {
        User userToSave = new User("2", "Alice1", "alice23@gmail.com");
        User savedUser = new User("2", "Alice1", "alice23@gmail.com");

        // Arrange: Set up the mock behavior for UserService
        Mockito.when(userService.saveUser(userToSave))
                .thenReturn(Mono.just(savedUser));

        // Act and Assert
        webTestClient.post().uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userToSave)
                .exchange()
                .expectStatus().isCreated();
    }


    @Test
    public void deleteUser() {
        // Arrange: Set up the mock behavior for UserService
        Mockito.when(userService.deleteUser("1"))
                .thenReturn(Mono.empty());

        // Act and Assert
        webTestClient.delete().uri("/api/users/1")
                .exchange()
                .expectStatus().isNoContent();
    }


}
