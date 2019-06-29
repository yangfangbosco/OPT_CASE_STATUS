import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Driver {
	
	//public static int start = 1990188800;
	public static int start = 1990189200;
	public static int end = 1990189300;
	
	//public static int start = 1990226664;
	//public static int end = 1990227164;
	
	public static Map<String, Integer> status_map = new HashMap<String, Integer>();
	
	public static int check_EAD_status(int start, int end) {
		
		System.setProperty("webdriver.chrome.driver", "/Users/_/Downloads/chromedriver");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		ChromeDriver driver = new ChromeDriver(options);
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("result.txt", true));
			for(int i = start; i < end; i++) {
				start++;
				driver.get("https://egov.uscis.gov/casestatus/landing.do");
				driver.findElement(By.id("receipt_number")).click();
				driver.findElement(By.id("receipt_number")).sendKeys("YSC"+i);
				driver.findElement(By.name("initCaseSearch")).click();
				WebElement status = driver.findElement(By.cssSelector("h1"));
				String status_str = status.getText();
				//System.out.println("YSC"+i + ": " + status_str);
				writer.write("YSC"+i + ": " + status_str + "\n");
				if(status_map.containsKey(status_str)) {
					status_map.put(status_str,status_map.get(status_str)+1);
				}else {
					status_map.put(status_str,1);
				}
				int remain = end - start;
				System.out.println("remain: " + remain);
			}
			writer.close();
		}catch(Exception e) {
			driver.quit();	
			start++;
			
			System.out.println(e.getMessage());
			return start;
		}
		driver.quit();
		return start;
		
	}
	

	public static void main(String[] args) {
		while(start < end) {
			start = check_EAD_status(start, end);
		}
		System.out.println(new Date());
		for(String key: status_map.keySet()) {
			System.out.println(key + ": " + status_map.get(key));
		}
	}
}
