package petstore;


import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;


public class Pet {

    String token = "";
    String url_base = "https://petstore.swagger.io/v2/pet";

    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    @Test(priority = 1)
    public void incluirPet() throws IOException {

        String jsonBody = lerJson("Dados/pet1.json");

        Response response =
        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
                .when()
                .post(url_base)
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("zulu labrador"))
                .body("status", is("available"))
                .body("category.name", containsString("dog"))
                .extract()
                .response()
                ;


        token = response.jsonPath().getString("tags[0].id");
        System.out.println(token);

    }
    @Test(priority = 2)
    public void consultarPet(){
        String petId = "3112198065";
        String url_base_get = url_base + "/" + petId;

        given()
                .contentType("application/json")
                .log().all()
                .when()
                .get(url_base_get)
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("zulu labrador"))
                .body("category.name", containsString("dog"))

        ;

    }

    @Test(priority = 3)
    public void alterarPet() throws IOException {
        String jsonBody = lerJson("Dados/pet2.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
                .when()
                .put(url_base)
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("zulu labrador"))
                .body("status", is("sold"))


        ;

    }

    @Test(priority = 4)
    public void deletePet() throws IOException {

        given()
                .contentType("application/json")
                .log().all()
                .when()
                .delete(url_base + "/" + token)
                .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", is(token))


        ;

    }

}
