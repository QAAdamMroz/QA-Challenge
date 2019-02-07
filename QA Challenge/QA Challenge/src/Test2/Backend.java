package Test2;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;

import java.awt.print.Book;
import java.util.List;

public class Backend {

	@Test
	public void Main()
	{   
		RestAssured.baseURI = "https://www.autohero.com/de/api/v1";
		String report="";
		
		
		Response response = given().
				contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body("{\"meta\":{\"fields\":[\"bodyType\",\"builtYear\",\"co2Value\",\"composites\",\"firstRegistrationYear\",\"fuelConsumption\",\"fuelType\",\"gearType\",\"horsePower\",\"id\",\"kw\",\"manufacturer\",\"mileage\",\"model\",\"offerPrice\",\"publishedAt\",\"registration\",\"status\",\"subType\",\"subTypeExtra\",\"subtitle\",\"title\",\"tuvNumber\"],\"from\":0,\"locale\":\"de\",\"location\":{\"countries\":[\"DE\"],\"locations\":[]},\"published\":true,\"registrationDate\":{\"from\":2015},\"sellerType\":[3],\"size\":999,\"sort\":[{\"field\":\"sellerType\",\"direction\":\"desc\"},{\"field\":\"offerPrice.amountMinorUnits\",\"direction\":\"desc\"}],\"status\":[\"retail-ready\",\"reserved\"]}}")
				.when()
				.post(RestAssured.baseURI + "/search-template/classified/findAds/v2")				;
				
		//I am lazy - i did "pagin" for selenium it gonna work the same so i just changee display every data :) parameter: "size":999
		System.out.println("POST Response\n" + response.asString());
		
		//Reg year verify
		JsonPath jsonPathEvaluator = response.jsonPath();
		
		List<Integer> allCarsReg = jsonPathEvaluator.getList("response.hits.hits._source.firstRegistrationYear");
		for(int cars : allCarsReg)
		 {
			
			if(2014<cars)
		 System.out.println("Reg year: " + cars);
			else
			{
			System.out.println("Incorrect reg year: " + cars);
			report=report+"Incorrect reg year: " + cars+"\n";
			}
		 }
		//Price verify
		int prev=0;
		List<Integer> allCarsPrice = jsonPathEvaluator.getList("response.hits.hits._source.offerPrice.amountMinorUnits");
		for(int cars : allCarsPrice)
		 {
			if(prev==0)
				prev=cars;//first one
			if(prev>=cars)
			{
		 System.out.println("Price: " + prev+" >= "+cars);
		 prev=cars;//correct value
		 }
			else
			{
				System.out.println("Incorrect Price: " + prev+" >= "+cars);
				report=report+"Incorrect Price: " + prev+" >= "+cars+"\n";
				 prev=cars;
			}
		 }
		Report.main(report);
	}
	

}
