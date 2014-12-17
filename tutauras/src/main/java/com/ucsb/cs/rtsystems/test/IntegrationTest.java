package com.ucsb.cs.rtsystems.test;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

public class IntegrationTest {
	
	@Test
	public void lectureInstanceTest(){
		//create a user
		given()
		.contentType(ContentType.JSON)
		.content("{\"ID\":\"12345\", \"firstName\":\"Example\", \"email\":\"example@abc.com\"}")
		.expect()
		.statusCode(204)
		.when()
		.post("rest/user");
		
		//check if it is created
		expect().body("firstName", equalTo("Example")).when().get("rest/user/12345");
		
		//create a subject
		given()
		.contentType(ContentType.JSON)
		.content("{\"code\" :\"cs101\", \"name\":\"Python\", \"description\" : \"Introduction to Computer Science\"}")
		.expect()
		.statusCode(204)
		.when()
		.post("rest/subject");
		
		//check if it got created
		expect().body("code", equalTo("cs101")).when().get("rest/subject/cs101");
		
		//create a lecture
		given()
		.contentType(ContentType.JSON)
		.content("{\"subjectCode\":\"cs101\", \"tutor\":\"12345\", \"dayOfWeek\": 2, \"startTimeHour\":2, \"startTimeMinute\":30, \"endTimeHour\":3, \"endTimeMinute\":30}")
		.expect()
		.statusCode(204)
		.when()
		.post("rest/lecture/cs101");
		
		//check if it got created
		Response response = expect().body("subjectCode", hasItem(("cs101"))).when().get("rest/lecture/cs101/");
		String responseBody = response.getBody().asString();
		JsonPath jsonPath = new JsonPath(responseBody);
		List<HashMap> lectures = jsonPath.getList("");
		Iterator<HashMap> i = lectures.iterator();
		Long lectureId = (Long)((HashMap)i.next()).get("ID");
		long lectureIdval = lectureId.longValue();
		//create a lecture Instance
		given()
		.contentType(ContentType.JSON)
		.content("{\"subjectCode\":\"cs101\", \"lectureId\":\""+lectureIdval+"\", \"studentId\":\"12345\", \"lectureDate\": \"Dec 10, 2014\"}")
		.expect()
		.statusCode(204)
		.when()
		.post("rest/lectureInstance/cs101/"+lectureIdval);
		
		//tear down - also serve to test deletes
		//delete a user
		given()
		.contentType(ContentType.JSON)
		.expect()
		.statusCode(204)
		.when()
		.delete("rest/user/12345");
		
		//check if it is deleted
		expect().statusCode(404).when().get("rest/user/12345");
		
		//delete the subject
		given()
		.contentType(ContentType.JSON)
		.expect()
		.statusCode(204)
		.when()
		.delete("rest/subject/cs101");
		
		//check if it is deleted
		expect().statusCode(404).when().get("rest/subject/cs101");
		
		//delete the lecture - note should get autodeleted by task queue when the subject is deleted
		given()
		.contentType(ContentType.JSON)
		.expect()
		.statusCode(204)
		.when()
		.delete("rest/lecture/cs101/"+lectureIdval);
		
		//check if it is deleted
		expect().statusCode(404).when().get("rest/lecture/cs101/"+lectureIdval);
	}

}
