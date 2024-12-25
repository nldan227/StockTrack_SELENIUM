package Page;

import SetUp.Setup;
import Utils.utils;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    @FindBy (id="username")
    WebElement field_username;
    @FindBy (id="password")
    WebElement field_password;

    @FindBy (xpath = "//input[@name='submit']")
    WebElement btn_submit;
    public LoginPage (WebDriver driver)
    {
        PageFactory.initElements(driver,this);
    }

    @Step("Đăng nhập với username: {0}, passowrd: {1}")
    public void doLogin(WebDriver driver, String username, String password){
        utils.waitForElement(driver, field_username, 5000);
        field_username.sendKeys(username);
        field_password.sendKeys(password);
        btn_submit.click();
    }

}
