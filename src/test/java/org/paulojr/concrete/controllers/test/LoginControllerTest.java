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
public class LoginControllerTest {

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
    public void shouldLoginSucessful() {
        User user = UserBuilder.newInstance().sampleUser().build();

        given()
                    .header("Accept", "application/json")
                    .contentType("application/json")
                    .body(user)
                .expect()
                    .statusCode(201)
                .when()
                    .post("/user")
                .andReturn()
                    .jsonPath();

        JsonPath path = given()
                    .header("Accept", "application/json")
                    .contentType("application/json")
                    .body(user)
                .expect()
                    .statusCode(200)
                .when()
                    .post("/login")
                .andReturn()
                    .jsonPath();

        User usuarioRetornado = path.getObject("", User.class);

        assertThat(usuarioRetornado.getEmail(), equalTo(user.getEmail()));
        assertThat("Senha é válido!", userService.validatePassword(user.getPassword(), usuarioRetornado));
    }

    @Test
    public void shouldReturn404WhenUserDoentExists() {
        User user = UserBuilder.newInstance().sampleUser().build();

        JsonPath path = given()
                    .header("Accept", "application/json")
                    .contentType("application/json")
                    .body(user)
                .expect()
                    .statusCode(404)
                .when()
                    .post("/login")
                .andReturn()
                    .jsonPath();

        assertThat(path.getString("mensagem"), equalTo("Usuário e/ou senha inválidos"));
    }

    @Test
    public void shouldReturn401WhenThePasswordDoesntMatch() {
        User user = UserBuilder.newInstance().sampleUser().build();
        given()
                    .header("Accept", "application/json")
                    .contentType("application/json")
                    .body(user)
                .expect()
                    .statusCode(201)
                .when()
                    .post("/user")
                .andReturn()
                    .jsonPath();

        user.setPassword("wrong-password");
        JsonPath path = given()
                    .header("Accept", "application/json")
                    .contentType("application/json")
                    .body(user)
                .expect()
                    .statusCode(401)
                .when()
                    .post("/login")
                .andReturn()
                    .jsonPath();

        assertThat(path.getString("mensagem"), equalTo("Usuário e/ou senha inválidos"));
    }


}
