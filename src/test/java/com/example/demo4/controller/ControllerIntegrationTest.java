    package com.example.demo4.controller;

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
    import com.example.demo4.model.User;
    import com.example.demo4.repository.UserRepository;
    import com.example.demo4.service.UserService;
    import org.junit.Test;
    import org.junit.jupiter.api.BeforeAll;
    import org.junit.runner.RunWith;
    import org.mockito.Mockito;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
    import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.boot.test.mock.mockito.MockBean;
    import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
    import org.springframework.context.annotation.ComponentScan;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.MediaType;
    import org.springframework.test.context.ContextConfiguration;
    import org.springframework.test.context.DynamicPropertyRegistry;
    import org.springframework.test.context.DynamicPropertySource;
    //import org.springframework.test.context.junit4.SpringRunner;
    //import org.springframework.test.context.junit4.SpringRunner;
    import org.springframework.test.context.junit4.SpringRunner;
    import org.springframework.test.web.reactive.server.WebTestClient;
    import org.testcontainers.containers.MySQLContainer;
    import org.testcontainers.junit.jupiter.Container;
    import org.testcontainers.junit.jupiter.Testcontainers;
    import org.testcontainers.utility.DockerImageName;
    import reactor.core.publisher.Flux;
    import reactor.core.publisher.Mono;
    import reactor.test.StepVerifier;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;


    @RunWith(SpringRunner.class)
    //@WebFluxTest(UserController.class)  // Specify the controller class to test

    //@Configuration
    //@ContextConfiguration
    @SpringBootTest(classes = UserController.class)
    @ComponentScan(basePackages = "com.example.demo4")
    @AutoConfigureWebTestClient
    @Testcontainers
    //@ContextConfiguration(classes = ControllerIntegrationTest.class)
        //@WebFluxTest(UserController.class)
    public class ControllerIntegrationTest {


        @Autowired
        private WebTestClient webTestClient;

        @MockBean
        private UserService userService;

        @Autowired
        private UserRepository userRepository;
        @Container

        private static final Logger logger = LoggerFactory.getLogger(ControllerIntegrationTest.class);

        //    @ServiceConnection
        public static MySQLContainer mySQLContainer  = (MySQLContainer) new MySQLContainer("mysql:8.2.0")
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test")
                .withCommand("--init-file", "/docker-entrypoint-initrd.d/init.sql");
//        public static mysql = new MySQLContainer<>(DockerImageName.parse("mysql:5.7.34"));
    //mysql.start();

        @DynamicPropertySource
        static void dynamicProperties(DynamicPropertyRegistry registry) {

            if (!mySQLContainer.isRunning()) {
                mySQLContainer.start();
                System.out.println("Testcontainers MySQL container started.");
            } else {
                System.out.println("Testcontainers MySQL container is already running.");
            }


            registry.add("spring.r2dbc.url", () -> "r2dbc:mysql://" + mySQLContainer.getHost() +
                    ":" + mySQLContainer.getMappedPort(3306) + "/" + mySQLContainer.getDatabaseName());
            registry.add("spring.r2dbc.username", mySQLContainer::getUsername);
            registry.add("spring.r2dbc.password", mySQLContainer::getPassword);
        }




//        @BeforeAll
//        static void beforeAll() {
//            if (!mySQLContainer.isRunning()) {
//                mySQLContainer.start();
//                System.out.println("Testcontainers MySQL container started.");
//            } else {
//                System.out.println("Testcontainers MySQL container is already running.");
//            }
//        }

    //    @Test
    //    public void getAllUsers() {
    //
    //
    //
    //        User expectedUser = new User("1", "Alice", "alice@example.com");
    //
    //        // Arrange: Set up the mock behavior for UserService
    //        Mockito.when(userService.getAllUsers())
    //                .thenReturn(Flux.just(expectedUser));
    //
    //        // Act and Assert
    //        webTestClient.get().uri("/api/users")
    //                .exchange()
    //                .expectStatus().isOk()
    //                .expectHeader().contentType(MediaType.APPLICATION_JSON)
    //                .expectBodyList(User.class)
    //                .value(userList -> {
    //                    // Use StepVerifier to perform assertions on the Flux
    //                    StepVerifier.create(Flux.fromIterable(userList))
    //                            .expectNext(expectedUser)
    //                            .verifyComplete();
    //                });
    //    }

        @Test
        public void getAllUsers() {

            logger.info("Starting getAllUsers test...");
//            logger.info("Testcontainers MySQL container started. JDBC URL: {}", mySQLContainer.getMappedPort(3306));


            // Set up the database with some test data
    //        userRepository.saveAll(Flux.just(
    //                new User("1", "Alice", "alice@example.com"),
    //                new User("2", "Bob", "bob@example.com"),
    //                new User("3", "Charlie", "charlie@example.com")
    //        )).blockLast();

            // Act and Assert
            webTestClient.get().uri("/api/users")
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType("application/json")
                    .expectBodyList(User.class)
                    .value(userList -> {
                        StepVerifier.create(Flux.fromIterable(userList))
                                .expectNextCount(3)
                                .verifyComplete();
                    });
        }

//        @Test
//        public void saveUser() {
//            // Arrange
//            User newUser = new User("4", "David", "david@example.com");
//
//            // Act
//            webTestClient.post().uri("/api/users")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(Mono.just(newUser), User.class)
//                    .exchange()
//                    .expectStatus().isCreated()
//                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                    .expectBody(User.class)
//                    .value(savedUser -> {
//                        // Assert
//                        assertEquals(newUser.getId(), savedUser.getId());
//                        assertEquals(newUser.getUsername(), savedUser.getUsername());
//                        assertEquals(newUser.getEmail(), savedUser.getEmail());
//                    });
//        }

        @Test
        public void saveUser() {
            // Arrange
            User newUser = new User("4", "David", "david@example.com");

            // Act
            Flux<User> userFlux = webTestClient.post().uri("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(newUser), User.class)
                    .exchange()
                    .expectStatus().isCreated()
                    .returnResult(User.class)
                    .getResponseBody();


        }



    }

