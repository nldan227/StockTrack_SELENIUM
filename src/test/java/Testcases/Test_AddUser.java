package Testcases;

import Page.AddUserPage;
import Page.LoginPage;
import Page.NavPage;
import Page.StockInPage;
import SetUp.Setup;
import Utils.utils;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Random;

@Feature("Kiểm tra chức năng thêm người dùng")
public class Test_AddUser extends Setup {
    LoginPage loginPage;
    NavPage navPage;
    AddUserPage addUserPage;

    @BeforeClass
    public void doLogin() throws InterruptedException {
        loginPage = new LoginPage(getDriver());
        navPage = new NavPage(getDriver());
        getDriver().get("http://localhost/NhapKho/model/login.php");
        loginPage.doLogin(getDriver(),"manager", "123");
        utils.waitForElement(getDriver(), navPage.manager_User, 3000);
        navPage.navToManagerUserPage(getDriver());
        Thread.sleep(5000);

    }

    @Test(priority = 1, description = "TC-001 - Xác minh chức năng thêm người dùng mới với thông tin hợp lệ.")
    public void addUserWithValidData1() throws InterruptedException {
        String roleName = "";
        String username = utils.generateUsername();
        String password = "123456aA@";
        String name = "Ng Linh Dan";
        String email = utils.generateValidEmail();
        int role = new Random().nextInt(2) + 1; // Sinh số 1 hoặc 2
        String phone = "076859119";
        addUserPage = new AddUserPage(getDriver());
        addUserPage.addUser(getDriver(), username, password, name, email, role, phone, "");
        Thread.sleep(2000);
        addUserPage.clickAdd();
        SoftAssert softAssert = new SoftAssert();
        utils.checkAlertMessage(softAssert, getDriver(), "Người dùng đã được thêm thành công!");
        System.out.println(role);
        if(role==1){
            roleName = "manager";
        }else{
            roleName = "user";
        }
        Thread.sleep(2000);
        addUserPage.verifyLastAddedUserInTable(getDriver(), softAssert, username, name, phone, roleName);
        // Gọi assertAll() để kiểm tra tất cả các lỗi
        softAssert.assertAll();
    }

    @Test(priority = 2, description = "TC-002 - Xác minh chức năng thêm người dùng mới với thông tin hợp lệ.")
    public void addUserWithValidData2() throws InterruptedException {
        String roleName = "";
        String username = utils.generateUsername();
        String password = "123456aA@";
        String name = "Ng Linh Dan";
        String email = utils.generateValidEmail();
        int role = new Random().nextInt(2) + 1; // Sinh số 1 hoặc 2
        addUserPage = new AddUserPage(getDriver());
        addUserPage.addUser(getDriver(), username, password, name, email, role, "", "");
        Thread.sleep(2000);
        addUserPage.clickAdd();
        SoftAssert softAssert = new SoftAssert();
        utils.checkAlertMessage(softAssert, getDriver(), "Người dùng đã được thêm thành công!");
        System.out.println(role);
        if(role==1){
            roleName = "manager";
        }else{
            roleName = "user";
        }
        Thread.sleep(2000);
        addUserPage.verifyLastAddedUserInTable(getDriver(), softAssert, username, name, "", roleName);
        // Gọi assertAll() để kiểm tra tất cả các lỗi
        softAssert.assertAll();
    }
    @Test(priority = 3, description = "TC-003 - Xác minh chức năng thêm người dùng mới với thông tin không hợp lệ trong các trường tên người dùng, mật khẩu và email.")
    public void addUserWithInValidData1() throws InterruptedException {
        String username = "user123";
        String password = "12346a@";
        String name = "Ng Linh Dan";
        String email = utils.generateText(6,10);
        int role = new Random().nextInt(2) + 1; // Sinh số 1 hoặc 2
        addUserPage = new AddUserPage(getDriver());
        addUserPage.addUser(getDriver(), username, password, name, email, role, "", "");
        Thread.sleep(2000);
        SoftAssert softAssert = new SoftAssert();
        addUserPage.verifyErrorMessagesInSpans(getDriver(), softAssert,"Tên tài khoản đã được sử dụng.", "Mật khẩu chưa đủ mạnh (ít nhất 8 ký tự, bao gồm chữ, số và ký tự đặc biệt).", "","Email sai định dạng.");
        addUserPage.clickAdd();
        utils.checkAlertMessage(softAssert, getDriver(), "Thông tin không hợp lệ. Vui lòng nhập lại.");
        // Gọi assertAll() để kiểm tra tất cả các lỗi
        softAssert.assertAll();
    }

    @Test(priority = 4, description = "TC-004 - Xác minh chức năng thêm người dùng mới khi trường tên người dùng bị bỏ trống và thông tin mật khẩu cùng email không hợp lệ.")
    public void addUserWithInValidData2() throws InterruptedException {
        String username = "";
        String password = "abcdfe12";
        String name = "Ng Linh Dan";
        String email = "nguyenlinhdan227@gmail.com";
        int role = new Random().nextInt(2) + 1;
        addUserPage = new AddUserPage(getDriver());
        addUserPage.addUser(getDriver(), username, password, name, email, role, "", "");
        Thread.sleep(2000);
        SoftAssert softAssert = new SoftAssert();
        addUserPage.verifyErrorMessagesInSpans(getDriver(), softAssert,"Tên tài khoản không được để trống.", "Mật khẩu chưa đủ mạnh (ít nhất 8 ký tự, bao gồm chữ, số và ký tự đặc biệt).", "","Email đã được sử dụng.");
        addUserPage.clickAdd();
        utils.checkAlertMessage(softAssert, getDriver(), "Thông tin không hợp lệ. Vui lòng nhập lại.");
        // Gọi assertAll() để kiểm tra tất cả các lỗi
        softAssert.assertAll();
    }
    @Test(priority = 5, description = "TC-005 - Xác minh chức năng thêm người dùng mới khi trường tên người dùng bị để trống và thông tin mật khẩu cùng email không hợp lệ.")
    public void addUserWithInValidData3() throws InterruptedException {
        String username = utils.generateUsername();
        String password = "abcdfeg!@#";
        String name = "";
        String email = utils.generateInvalidEmail();
        int role = new Random().nextInt(2) + 1;
        addUserPage = new AddUserPage(getDriver());
        addUserPage.addUser(getDriver(), username, password, name, email, role, "", "");
        Thread.sleep(2000);
        SoftAssert softAssert = new SoftAssert();
        addUserPage.verifyErrorMessagesInSpans(getDriver(), softAssert,"", "Mật khẩu chưa đủ mạnh (ít nhất 8 ký tự, bao gồm chữ, số và ký tự đặc biệt).", "Tên không được để trống.","Email không tồn tại.");
        addUserPage.clickAdd();
        utils.checkAlertMessage(softAssert, getDriver(), "Thông tin không hợp lệ. Vui lòng nhập lại.");
        // Gọi assertAll() để kiểm tra tất cả các lỗi
        softAssert.assertAll();
    }
    @Test(priority = 6, description = "TC-006 - Xác minh chức năng thêm người dùng mới khi trường tên người dùng bị để trống và thông tin mật khẩu cùng email không hợp lệ.")
    public void addUserWithInValidData4() throws InterruptedException {
        String username = utils.generateUsername();
        String password = "";
        String name = "Ng Linh Dan";
        String email = utils.generateValidEmail();
        int role = new Random().nextInt(2) + 1;
        addUserPage = new AddUserPage(getDriver());
        addUserPage.addUser(getDriver(), username, password, name, email, role, "", "");
        Thread.sleep(2000);
        SoftAssert softAssert = new SoftAssert();
        addUserPage.verifyErrorMessagesInSpans(getDriver(), softAssert, "", "Password không được để trống.", "","");
        addUserPage.clickAdd();
        utils.checkAlertMessage(softAssert, getDriver(), "Thông tin không hợp lệ. Vui lòng nhập lại.");
        // Gọi assertAll() để kiểm tra tất cả các lỗi
        softAssert.assertAll();
    }

}
