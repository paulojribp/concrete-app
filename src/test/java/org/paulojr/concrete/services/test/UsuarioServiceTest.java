package org.paulojr.concrete.services.test;

import com.jayway.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Test;
import org.paulojr.concrete.models.Phone;
import org.paulojr.concrete.models.User;

import java.util.Arrays;

import static com.jayway.restassured.RestAssured.given;

public class UsuarioServiceTest {

    @Test
    public void shouldAddANewUser() {
        User user = new User();
        user.setName("Davi");
        user.setEmail("davi@gmail.com");
        user.setPassword("davi@2016");
        Phone phone = new Phone();
        phone.setDdd("11");
        phone.setNumber("948372573");
        user.setPhones(Arrays.asList(phone));

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

        User usuarioRetornado = path.getObject("user", User.class);

        Assert.assertEquals(usuarioRetornado.getName(), user.getName());
        Assert.assertEquals(usuarioRetornado.getEmail(), user.getEmail());
    }

}
