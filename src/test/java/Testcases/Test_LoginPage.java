package Testcases;
import Page.LoginPage;
import SetUp.Setup;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class Test_LoginPage extends Setup {
    LoginPage loginPage;

    @Test(priority=1, description = "Login with valid data")
    public void doLogin(WebDriver driver) throws InterruptedException {
        loginPage = new LoginPage(driver);
        driver.get("http://localhost/NhapKho/model/login.php");
        loginPage.doLogin(driver,"user123", "123");
        Thread.sleep(5000);
    }
}
