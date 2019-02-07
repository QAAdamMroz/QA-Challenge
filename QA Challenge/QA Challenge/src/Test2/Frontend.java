package Test2;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Frontend {
	 
	 public static void main(String[] args) {
		 String report="";
		 String currentPath= System.getProperty("user.dir");
	 String exePath = currentPath.substring(0,currentPath.length() - 12) +"\\chromedriver.exe";
	 System.setProperty("webdriver.chrome.driver", exePath);
	 WebDriver driver = new ChromeDriver();
	 driver.get("https://www.autohero.com/de/search/");
	 try
	 {
		 // w8 for page load
		 ExpectedCondition<Boolean> pageLoadCondition = new
	                ExpectedCondition<Boolean>() {
	                    public Boolean apply(WebDriver driver) {
	                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
	                    }
	                };
	        WebDriverWait wait = new WebDriverWait(driver, 30);
	        wait.until(pageLoadCondition);
	      //Page loaded  
	      try
	      {
	    	  //Registration date
	     driver.findElement(By.xpath("//*[contains(text(), 'Erstzulassung ab')]")).click();
		 TimeUnit.SECONDS.sleep(1);
		 driver.findElement(By.xpath("//select[@name='yearRange.min']")).click();
		 TimeUnit.SECONDS.sleep(1);
		 driver.findElement(By.xpath("//*[contains(text(), '2015')]")).click();
		 TimeUnit.SECONDS.sleep(1);
		 //sort by price
		 driver.findElement(By.xpath("//select[@name='sort']")).click();
		 TimeUnit.SECONDS.sleep(1);
		 driver.findElement(By.xpath("//*[contains(text(), 'Höchster Preis')]")).click();
		 TimeUnit.SECONDS.sleep(5);
		 try
		 {
			 String vResults=driver.findElement(By.xpath("//div[@class='resultsAmount___3OrV7']")).getText();
			 Double preValue=0.0, nxtValue;
			 String nxtValueAsString,regDate,pathPage;
			 int pagesNumber=(Integer.parseInt(vResults.substring(0, vResults.length() - 8))/24)+2;
			  for(int j=1; j<pagesNumber; j++)
			 {
				  if(j!=1)
					 {
					 //change page 
					  		pathPage="//a[contains(@class,'btn btn-link') and text() = '"+j+"']";
							 driver.findElement(By.xpath(pathPage)).click();
							 TimeUnit.SECONDS.sleep(5);
					 }
				  
			 for(int i=1; i<25; i++) //Default paging is set for 24 so i just use tricky way :)
			 {
				 try
				 {
				 //Verify page
			String pathPrice="//div[@class='item___T1IPF']["+i+"]/div[@class='root___Dz4kU']/a[@class='link___2Maxt clearfix']/div[@class='price___1A8DG']/div[@class='totalPrice___3yfNv']";
			 String pathRegDate="//div[@class='item___T1IPF']["+i+"]/div[@class='root___Dz4kU']/a[@class='link___2Maxt clearfix']/ul[@class='specList___2i0rY']/li[@class='specItem___2gMHn'][1]";
			 String pathHref="//div[@class='item___T1IPF']["+i+"]/div[@class='root___Dz4kU']/a[@class='link___2Maxt clearfix']";
			 //Verify price
			 String currentData= driver.findElement(By.xpath(pathHref)).getAttribute("href");
			 nxtValueAsString=driver.findElement(By.xpath(pathPrice)).getText();
			 nxtValue = Double.parseDouble(nxtValueAsString.substring(0, nxtValueAsString.length() - 1));
			 //System.out.println(nxtValue);
			 if(preValue==0.0)
				 preValue=nxtValue; //for case preValue will be empty (first row)
			 else
				 if(nxtValue<=preValue)
					 System.out.println(nxtValue+" <= "+preValue);
				 else
				 {
					 System.out.println("Error:"+nxtValue+" <= "+preValue+" url:"+currentData);
			 report=report+"Error:"+nxtValue+" <= "+preValue+" url:"+currentData+"\n";
				 }
			 preValue=nxtValue;
			 //Verify reg date
			 regDate=driver.findElement(By.xpath(pathRegDate)).getText();
			 if(Double.parseDouble(regDate.substring(5))>=2015)
			 System.out.println(regDate.substring(2));
			 else
			 {
				 System.out.println("There is reg date under 2015: "+regDate.substring(2)+" url:"+currentData);
				 report=report+"There is reg date under 2015: "+regDate.substring(2)+" url:"+currentData+"\n";
			 }
			 }
			 catch(Exception ex)
			 {
				 System.out.println("Out of records"); 
			 }
			 }
						 }
		 }
		 catch(Exception et)
		 {
			 System.out.println(et);
	    	  System.out.println("Error during verify"); 
		 }
	      }
	      catch(Exception es)
	      {
	    	  System.out.println(es);
	    	  System.out.println("Error during sorting");
	      }
	      
	 }
	 catch(Exception e)
	 {
		 System.out.println(e);
		 System.out.println("Error during page loading");
	 }
	 driver.close();
	 Report.main(report);
	 }
	 
}
