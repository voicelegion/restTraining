package lv.romanpipchenko;

import io.restassured.http.ContentType;
import lv.romanpipchenko.restexample.beans.Cat;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CatsRestTesting {
    private String catsName = RandomStringUtils.randomAlphabetic(7);

    @Test
    public void testGetCats() {
        testGetCats("Timka");
    }

    private void testGetCats(String nameForVerification) {
        List<Cat> cats=getAllCats();
        assertTrue(cats.size() >0);
        assertTrue(catExists(nameForVerification));
    }

    @Test
    public void testPostCats() {
        postCat(catsName);
    }

    @Test
    public void testPutCats() {
        postCat(catsName);
        updateCat(getAllCats().size() - 1);
        String lastCatsName = getAllCats().get(getAllCats().size() - 1).getName();
        assertTrue(!lastCatsName.equals(catsName));
    }

    @Test
    public void testDeleteCats() {
        postCat(catsName);
        List<Cat> listCats = getAllCats();
        deleteCat(listCats.size() - 1);
        assertFalse(catExists(catsName));
    }
    /**
     * After main CRUD tests are done, we continue with additional tests for REST API
     */
    @Test
    public void testDeleteNonExistingCat(){
       assertTrue(given()
               .delete("/rest/cat/"+ getAllCats().size())
               .then().statusCode(404)
               .log().body()
               .extract().body().asString()
               .equals(("Cat with id="+ getAllCats().size()+" is not found")));
    }
    @Test
    public void testPostCatBadJson(){
        assertTrue(given()
                .body("{\"someUglyText\"}")
                .contentType(ContentType.JSON)
                .post("/rest/cat")
                .then().statusCode(400)
                .log().all()
                .extract().body().asString()
                .contains("Bad Request"));
    }

    @Test
    public void testCreateExistingCat(){
        createExistingCat();
    }

    @Test
    public void testGetOneCat(){
        assertTrue(getOneCat(2).contains("name"));
    }


    private void postCat(String nameForCat) {
        int catsAge = RandomUtils.nextInt(0, 15);
        Cat newCat = new Cat();
        newCat.setAge(catsAge);
        newCat.setColor("Nigga!");
        newCat.setName(nameForCat);
        given()
                .body(newCat)
                .contentType(ContentType.JSON).post("/rest/cat/")
                .then()
                .statusCode(201);
        testGetCats(nameForCat);
    }

    private List<Cat> getAllCats() {
        return Arrays.asList(given().get("/rest/cat").then().statusCode(200).extract().response().getBody().as(Cat[].class));
    }

    private void deleteCat(int index) {
        given().delete("/rest/cat/" + index).then().statusCode(200).log().body();
    }

    private boolean catExists(String nameOfCat) {
        boolean result = false;
        for (Cat cat : getAllCats()) {
            if (nameOfCat.equals(cat.getName())) {
                result = true;
                break;
            }
        }
        return result;
    }

    private void updateCat(int catsIndex) {
        given()
                .body("{\"name\":\"Valeroid\",\"age\":100,\"color\":\"SILVER\"}")
                .contentType(ContentType.JSON)
                .put("rest/cat/" + catsIndex)
                .then()
                .statusCode(200);
        assertTrue(given().get("/rest/cat").then().extract().response().asString().contains("Valeroid"));
    }

    private String getOneCat(int catsIndex){
       return given().get("rest/cat/"+ catsIndex).then().statusCode(200).extract().body().asString();
    }

    private void createExistingCat(){
        int firstCounter = 0;
        int secondCounter = 0;
        Cat someNewCat = getAllCats().get(ThreadLocalRandom.current().nextInt(0, getAllCats().size()));
        for (Cat cat : getAllCats()){
            if (cat.equals(someNewCat)){
                firstCounter++;
            }
        }
        given().body(someNewCat).contentType(ContentType.JSON).post("/rest/cat").then().statusCode(201);

        for (int i = 0; i < getAllCats().size(); i++) {
            if (someNewCat.equals(getAllCats().get(i))){
                secondCounter++;
            }
        }
        assertTrue(secondCounter == firstCounter+1);
        System.out.println("Second couter is: "+secondCounter);
        System.out.println("First couter is: "+firstCounter);
    }





}
