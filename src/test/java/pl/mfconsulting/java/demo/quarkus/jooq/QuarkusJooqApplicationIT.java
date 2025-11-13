package pl.mfconsulting.java.demo.quarkus.jooq;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;
import pl.mfconsulting.java.demo.quarkus.jooq.config.IntegrationTestProfile;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestProfile(IntegrationTestProfile.class)
class QuarkusJooqApplicationIT {

	@Test
	void whenCallGetAllThenReturn() {
        given()
                .when().get("/api/accounts")
                .then()
                    .statusCode(201)
                    .body("size()", is(3));
	}

}
