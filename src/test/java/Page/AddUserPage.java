package Page;

import SetUp.Setup;
import Utils.AllureTestListener;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class AddUserPage {
    @FindBy(xpath = "//input[@id='username']")
    WebElement field_username;
    @FindBy(xpath = "//input[@id='password']")
    WebElement field_password;
    @FindBy(xpath = "//input[@id='full_name']")
    WebElement field_name;
    @FindBy(xpath = "//input[@id='email']")
    WebElement field_email;
    @FindBy(xpath = "//input[@id='phone']")
    WebElement field_phone;
    @FindBy(xpath = "//input[@id='address']")
    WebElement field_address;
    @FindBy(xpath = "//select[@id='role_id']")
    WebElement field_role;
    @FindBy(xpath = "//span[@id='username-error']")
    WebElement span_username;
    @FindBy(xpath = "//span[@id='password-error']")
    WebElement span_password;
    @FindBy(xpath = "//span[@id='email-error']")
    WebElement span_email;
    @FindBy(xpath = "//span[@id='fullname-error']")
    WebElement span_fullname;
    @FindBy(xpath = "//div[@class='listUsers']//table")
    WebElement table_ListUser;
    @FindBy(xpath = "//input[@value='Hủy bỏ']")
    WebElement btn_cancel;
    @FindBy(xpath = "//input[@name='submit']")
    WebElement btn_addUser;
    public AddUserPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }


    @Step("Thêm người dùng: {0}, password: {1}, name: {2}, email: {3}, role: {4}, phone: {5}")
    public void addUser(WebDriver driver, String username, String password, String name, String email, int role, String phone, String address) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(field_username)).sendKeys(username);
        btn_cancel.click();
        Thread.sleep(2000);
        field_username.sendKeys(username);
        field_password.sendKeys(password);
        field_name.sendKeys(name);
        field_email.sendKeys(email);
        field_phone.sendKeys(phone);
        field_address.sendKeys(address);
        // Random ngẫu nhiên số 1 hoặc 2
        Actions actions = new Actions(driver);
        if(role==1){
            actions.click(field_role);
            actions.sendKeys(Keys.ARROW_UP).perform();
            Thread.sleep(500);
        }else{
            actions.click(field_role);
            actions.sendKeys(Keys.ARROW_DOWN).perform();
            Thread.sleep(500);
        }
        actions.sendKeys(Keys.ENTER).perform();
    }
    public void clickAdd(){
        btn_addUser.click();
    }

    public void verifyLastAddedUserInTable(WebDriver driver, SoftAssert softAssert, String expectedUsername, String expectedFullName, String expectedPhone, String expectedRole) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(table_ListUser));

        // Lấy tất cả các hàng trong bảng
        List<WebElement> rows = table_ListUser.findElements(By.tagName("tr"));
        WebElement lastRow = rows.get(rows.size() - 1);

        // Lấy tất cả các cột trong hàng cuối cùng
        List<WebElement> cells = lastRow.findElements(By.tagName("td"));

        // Kiểm tra từng giá trị trong các cột
        checkCellValue(driver,"Username", cells.get(1).getText(), expectedUsername, softAssert);
        checkCellValue(driver, "Full Name", cells.get(2).getText(), expectedFullName, softAssert);
        checkCellValue(driver,"Phone Number", cells.get(3).getText(), expectedPhone, softAssert);
        checkCellValue(driver,"Role", cells.get(4).getText(), expectedRole, softAssert);
    }

    // Hàm kiểm tra giá trị của từng cột và log kết quả vào Allure
    private void checkCellValue(WebDriver driver, String fieldName, String actualValue, String expectedValue, SoftAssert softAssert) {
        Allure.step("Kiểm tra " + fieldName, () -> {
            if (!expectedValue.equals(actualValue)) {
                AllureTestListener.saveScreenshotPNG(driver); // Chụp ảnh khi kiểm tra thất bại
                Allure.addAttachment("Thông báo lỗi " + fieldName, "Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
            } else {
                Allure.step(fieldName + " khớp. Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
            }
            softAssert.assertTrue(expectedValue.equals(actualValue),
                    fieldName + " không khớp. Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
        });
    }

    public void verifyErrorMessagesInSpans(WebDriver driver, SoftAssert softAssert, String expectedMsgUsername, String expectedMsgPass, String expectedMsgFullName, String expectedMsgEmail) throws InterruptedException {
        // Kiểm tra và log từng thông báo
        verifyAndLog(driver,"Username", span_username.getText().trim(), expectedMsgUsername, softAssert);
        verifyAndLog(driver,"Password", span_password.getText().trim(), expectedMsgPass, softAssert);
        verifyAndLog(driver,"Full Name", span_fullname.getText().trim(), expectedMsgFullName, softAssert);
        Thread.sleep(3000);
        verifyAndLog(driver,"Email", span_email.getText().trim(), expectedMsgEmail, softAssert);
    }

    // Phương thức kiểm tra giá trị và log vào Allure
    private void verifyAndLog(WebDriver driver, String fieldName, String actualValue, String expectedValue, SoftAssert softAssert) {
        Allure.step("Kiểm tra thông báo " + fieldName, () -> {
            if (!expectedValue.equals(actualValue)) {
                // Khi giá trị không khớp
                AllureTestListener.saveScreenshotPNG(driver);
                Allure.addAttachment("Thông báo lỗi " + fieldName, "Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
            } else {
                // Khi giá trị khớp
                Allure.step("Thông báo " + fieldName + " khớp. Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
            }
            // Thêm vào kiểm tra với SoftAssert
            softAssert.assertTrue(expectedValue.equals(actualValue),
                    "Thông báo lỗi " + fieldName + " không khớp. Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
        });
    }

}
