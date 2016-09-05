package org.paulojr.concrete.controllers.test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.paulojr.concrete.daos.FacadeTestDaos;
import org.paulojr.concrete.models.User;
import org.paulojr.concrete.test.builders.UserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacadeTestDaos daos;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        daos.clearUsers();
    }

    @Test
    public void shouldAddANewUser() {
        User user = UserBuilder.newInstance()
                .sampleUser()
                .build();

        JsonPath path = given()
                    .header("Accept", "application/json")
                    .contentType("application/json")
                    .body(user)
                .expect()
                    .statusCode(201)
                .when()
                    .post("/user")
                .andReturn()
                    .jsonPath();

        User usuarioRetornado = path.getObject("", User.class);

        assertThat(usuarioRetornado.getId(), is(notNullValue()));
        assertThat(usuarioRetornado.getCreated(), is(notNullValue()));
        assertThat(usuarioRetornado.getLastLogin(), is(notNullValue()));
        assertThat(usuarioRetornado.getToken(), is(notNullValue()));

        assertThat(usuarioRetornado.getPassword(), not(equalTo(user.getPassword())));
    }

    @Test
    public void shouldValidateIfEmailIsAlreadyUsed() {
        User user = UserBuilder.newInstance().sampleUser().build();
        User userJose = UserBuilder.newInstance()
                .name("José Silva")
                .password("123")
                .email(user.getEmail())
                .phone("88", "910289944")
                .build();

        JsonPath path = given()
                    .header("Accept", "application/json")
                    .contentType("application/json")
                    .body(user)
                .expect()
                    .statusCode(201)
                .when()
                    .post("/user")
                .andReturn()
                    .jsonPath();
        JsonPath pathJose = given()
                    .header("Accept", "application/json")
                    .contentType("application/json")
                    .body(userJose)
                .expect()
                    .statusCode(400)
                .when()
                    .post("/user")
                .andReturn()
                    .jsonPath();

        assertThat(pathJose.getString("mensagem"), equalTo("E-mail já existente"));
    }
}
