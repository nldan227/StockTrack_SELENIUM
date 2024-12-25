package Page;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import Utils.utils;


public class NavPage {
    @FindBy (xpath="//li[@data-href='profile.php']")
    public WebElement menu_EditProfile;
    @FindBy (xpath="//li[@class='sub-menu dropdown-toggle']")
    public WebElement menu_Stock;
    @FindBy (xpath = "/html[1]/body[1]/div[2]/div[1]/div[3]/ul[1]/li[2]/div[1]/a[1]")
    public WebElement manager_User;
    @FindBy (xpath ="//a[@id='nhap-kho-link']")
    public WebElement func_stockIn;
    @FindBy (xpath = "//a[contains(text(),'Danh sách')]")
    public WebElement func_viewList;
    @FindBy (xpath="//li[@data-href='logout.html']")
    public WebElement  menu_Logout;
    public NavPage(WebDriver driver){
        PageFactory.initElements(driver,this);
    }
    @Step("Chuyển hướng đến trang nhập kho...")
    public void navToStockInPage(WebDriver driver){
        menu_Stock.click();
        utils.waitForElement(driver, func_stockIn, 5000);
        func_stockIn.click();
    }
    @Step("Chuyển hướng đến trang quản lý user...")

    public void navToManagerUserPage(WebDriver driver){
        manager_User.click();
    }
    public void navToViewListPage(){
        menu_Stock.click();
        func_viewList.click();
    }
    public void navToLogOutPage(){
        menu_Logout.click();
    }
    public void navToEditProfilePage(){
        menu_EditProfile.click();
    }
}
