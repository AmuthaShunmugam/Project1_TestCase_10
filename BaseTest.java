package com.ibm.test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ibm.pages.AdminPage;
import com.ibm.pages.AdminPage1;
import com.ibm.pages.UserPage;
import com.ibm.utilities.ExcelUtil;
import com.ibm.utilities.PropertiesFileHandler;

public class BaseTest {
	WebDriver driver;
	WebDriverWait wait;
	PropertiesFileHandler propFIleHandler;
	HashMap<String, String> data;

	@BeforeSuite
	public void propertiesfile() throws IOException {
		String file = "./TestData/data.properties";
		PropertiesFileHandler propFileHandler = new PropertiesFileHandler();
		data = propFileHandler.getPropertiesAsMap(file);
	}

	@BeforeMethod
	public void BrowserInitialization() {
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 60);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void DeleteCategoryCheckInDataBase() throws InterruptedException, SQLException {
		
		String url = data.get("url");
		String userName = data.get("username");
		String password = data.get("password");
		String CouponName=data.get("Coupon");
		String CouponCode=data.get("Code");
		String CouponDiscount=data.get("Discount");
		driver.get(url);
		System.out.println("Delete a coupon and check in the database");
		AdminPage1 home = new AdminPage1(driver, wait);
		home.EnetrEmailAddress(userName);
		home.EnetrPassword(password);
		home.ClickonLoginButton();
		home.ClickOnMarketing();
        home.ClickOnCoupon();
		home.ClickonAddButton();
		home.EnterCouponName(CouponName);
		home.EnterCouponCode(CouponCode);
		home.EnterCouponDiscount(CouponDiscount);
		home.ClickonTheSaveButton();Thread.sleep(2000);
		//To get the count of as_coupons
		Connection c = DriverManager.getConnection("jdbc:mysql://foodsonfinger.com:3306/foodsonfinger_atozgroceries","foodsonfinger_atoz", "welcome@123");
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("SELECT counT(*) from as_coupons  ");
		rs.next();
	    int rowCount = rs.getInt(1);
	    System.out.println("The count before deleting the record:" +rowCount);
		home.ClickonCouponAction();Thread.sleep(2000);
		home.ClickonCouponDelete();Thread.sleep(2000);
		home.ClickonDeleteRecord();Thread.sleep(2000);
		Connection c1 = DriverManager.getConnection("jdbc:mysql://foodsonfinger.com:3306/foodsonfinger_atozgroceries","foodsonfinger_atoz", "welcome@123");
		Statement s1 = c.createStatement();
		ResultSet rs1 = s.executeQuery("SELECT counT(*) from as_coupons  ");
		rs1.next();
	    int rowCount1 = rs1.getInt(1);
	    System.out.println("The count after deleting the record:" +rowCount1);
	    
		}

		
	}

