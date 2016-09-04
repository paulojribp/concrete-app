package org.paulojr.concrete.controllers.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.paulojr.concrete.daos.FacadeTestDaos;
import org.paulojr.concrete.models.User;
import org.paulojr.concrete.services.UserService;
import org.paulojr.concrete.test.builders.UserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProfileControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;
    @Autowired
    private FacadeTestDaos daos;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = this.port;
        daos.clearUsers();
    }

    @Test
    public void shouldReturn404ForUnexistingToken() {
        User user = UserBuilder.newInstance()
                .password("abc123")
                .email("test@gmail.com")
                .build();

        JsonPath userPath = given()
                .header("Accept", "application/json")
                .contentType("application/json")
                .body(user)
                .expect()
                .statusCode(201)
                .when()
                .post("/user")
                .andReturn()
                .jsonPath();

        user = userPath.getObject("", User.class);

        JsonPath path = given()
                    .header("Accept", "application/json")
                    .header("token", "123")
                    .contentType("application/json")
                    .body(user)
                .expect()
                    .statusCode(404)
                .when()
                    .get("/profile/"+user.getId())
                .andReturn()
                    .jsonPath();

        assertThat(path.getString("mensagem"), equalTo("Não autorizado"));
    }

    @Test
    public void shouldReturn401ForWrongToken() {
        User user = UserBuilder.newInstance()
                .password("abc123")
                .email("richard@gmail.com")
                .build();

        JsonPath userPath = given()
                    .header("Accept", "application/json")
                    .contentType("application/json")
                    .body(user)
                .expect()
                    .statusCode(201)
                .when()
                    .post("/user")
                .andReturn()
                    .jsonPath();

        user = userPath.getObject("", User.class);

        JsonPath path = given()
                    .header("Accept", "application/json")
                    .header("token", user.getToken())
                    .contentType("application/json")
                    .body(user)
                .expect()
                    .statusCode(401)
                .when()
                    .get("/profile/123")
                .andReturn()
                    .jsonPath();

        assertThat(path.getString("mensagem"), equalTo("Não autorizado"));
    }

    @Test
    public void shouldReturn200WithUser() {
        User user = UserBuilder.newInstance()
                .password("abc123")
                .email("peter@gmail.com")
                .build();

        JsonPath userPath = given()
                    .header("Accept", "application/json")
                    .contentType("application/json")
                    .body(user)
                .expect()
                    .statusCode(201)
                .when()
                    .post("/user")
                .andReturn()
                    .jsonPath();

        user = userPath.getObject("", User.class);

        JsonPath path = given()
                    .header("Accept", "application/json")
                    .header("token", user.getToken())
                    .contentType("application/json")
                    .body(user)
                .expect()
                    .statusCode(200)
                .when()
                    .get("/profile/" + user.getId())
                .andReturn()
                    .jsonPath();

        User receivedUser = path.getObject("", User.class);
        assertThat(user.getId(), equalTo(receivedUser.getId()));
        assertThat(user.getToken(), equalTo(receivedUser.getToken()));
    }

}
