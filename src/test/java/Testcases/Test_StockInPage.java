package Testcases;
import Page.NavPage;
import Utils.AllureTestListener;
import Utils.utils;
import Page.LoginPage;
import Page.StockInPage;
import SetUp.Setup;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Feature("Kiểm tra chức năng thêm đơn nhập kho")
public class Test_StockInPage extends Setup {
    StockInPage stockInPage;
    LoginPage loginPage;
    NavPage navPage;

    @BeforeClass
    public void doLogin() throws InterruptedException {
        loginPage = new LoginPage(getDriver());
        navPage = new NavPage(getDriver());
        getDriver().get("http://localhost/NhapKho/model/login.php");
        loginPage.doLogin(getDriver(), "user123", "123");
        utils.waitForElement(getDriver(), navPage.menu_Stock, 3000);
        navPage.navToStockInPage(getDriver());
        Thread.sleep(5);
    }

    @Test(priority = 1, description = "TC007 - Xác nhận chức năng thêm đơn nhập kho với dữ liệu hợp lệ.")
    public void verifyAddStockEntryWithValidData1() throws InterruptedException, IOException, CsvException {
        SoftAssert softAssert = new SoftAssert();
        // Đọc file CSV nhà cung cấp và sản phẩm tương ứng
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        Map<String, List<String>> supplierProductData = utils.readCSV("D:\\Auto Testing\\StockTrackv1\\product_supplier.csv");
        List<String[]> products = new ArrayList<>();

        // Lấy ngẫu nhiên nhà cung cấp
        String supplier = stockInPage.getRandomSupplier(supplierProductData);
        // Lấy ngẫu nhiên sản phẩm tương ứng với nhà cung cấp đó
        String productBySupplier = stockInPage.getRandomProductBySupplier(supplier, supplierProductData, null);  // null để không loại trừ sản phẩm
        long quantity = utils.randomNumber(1);

        // Thêm vào list sản phẩm (tên sản phẩm + số lượng)
        products.add(new String[]{productBySupplier, String.valueOf(quantity)});
        String note = utils.generateText(4, 10);
        String date = utils.getCurrentDate();

        // Tiến hành thêm sản phẩm vào đơn nhập kho
        stockInPage = new StockInPage(getDriver());
        stockInPage.addStock(getDriver(), date, supplier, products, note);
        Thread.sleep(2000);

        // Kiểm tra các thông tin trên table sau khi thêm sản phẩm
        Map<String, List<String>> productDetails = stockInPage.extractProductDetails(products);
        stockInPage.verifyTableStockIn(softAssert, getDriver(), productDetails);

        // Lưu đơn nhập kho
        stockInPage.saveStock();

        // Kiểm tra sự xuất hiện của Alert
        utils.checkAlertMessage(softAssert, getDriver(), "Phiếu đã được lưu thành công!");
        // Gọi assertAll() để kiểm tra tất cả các lỗi
        softAssert.assertAll();
    }

    @Test(priority = 2, description = "TC-008 - Xác nhận chức năng thêm đơn nhập kho với dữ liệu hợp lệ")
    public void verifyAddStockEntryWithValidData2() throws InterruptedException, IOException, CsvException {
        SoftAssert softAssert = new SoftAssert();
        // Đọc file CSV nhà cung cấp và sản phẩm tương ứng
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        Map<String, List<String>> supplierProductData = utils.readCSV("D:\\Auto Testing\\StockTrackv1\\product_supplier.csv");
        List<String[]> products = new ArrayList<>();

        // Lấy ngẫu nhiên nhà cung cấp
        String supplier = stockInPage.getRandomSupplier(supplierProductData);
        String excludeProduct = null;
        for (int i = 1; i <= 2; i++) {
            String product = stockInPage.getRandomProductBySupplier(supplier, supplierProductData, excludeProduct);
            excludeProduct = product; // null để không loại trừ sản phẩm
            long quantity = utils.randomNumber(1);  // Số lượng ngẫu nhiên
            products.add(new String[]{product, String.valueOf(quantity)});
        }
        String date = utils.getCurrentDate();

        // Thêm sản phẩm vào đơn nhập kho
        stockInPage = new StockInPage(getDriver());
        wait.until(ExpectedConditions.elementToBeClickable(stockInPage.btn_reset));
        stockInPage.resetStockIn();
        stockInPage.addStock(getDriver(), date, supplier, products, "");

        // Kiểm tra các thông tin trên table sau khi thêm sản phẩm
        Map<String, List<String>> productDetails = stockInPage.extractProductDetails(products);

        // Kiểm tra các thông tin trên table sau khi thêm sản phẩm
        stockInPage.verifyTableStockIn(softAssert, getDriver(), productDetails);

        // Thêm đơn nhập kho
        stockInPage.saveStock();

        // Kiểm tra alert
        utils.checkAlertMessage(softAssert, getDriver(), "Phiếu đã được lưu thành công!");
        // Gọi assertAll() để kiểm tra tất cả các lỗi
        softAssert.assertAll();
    }

    @Test(priority = 3, description = "TC-009 - Xác nhận chức năng thêm đơn nhập kho với dữ liệu trường ngày nhập được bỏ trống.")
    public void verifyAddStockEntryWithInValidData1() throws IOException, CsvException, InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        Map<String, List<String>> supplierProductData = utils.readCSV("D:\\Auto Testing\\StockTrackv1\\product_supplier.csv");
        List<String[]> products = new ArrayList<>();
        String supplier = stockInPage.getRandomSupplier(supplierProductData);
        String productBySupplier = stockInPage.getRandomProductBySupplier(supplier, supplierProductData, null);  // null để không loại trừ sản phẩm
        long quantity = utils.randomNumber(1);
        products.add(new String[]{productBySupplier, String.valueOf(quantity)});
        String note = utils.generateText(4, 10);
        String date = "";
        // Tiến hành thêm sản phẩm vào đơn nhập kho
        stockInPage = new StockInPage(getDriver());
        wait.until(ExpectedConditions.elementToBeClickable(stockInPage.btn_reset));
        stockInPage.resetStockIn();
        stockInPage.addStock(getDriver(), date, supplier, products, note);
        Thread.sleep(2000);
        // Kiểm tra các thông tin trên table sau khi thêm sản phẩm
        Map<String, List<String>> productDetails = stockInPage.extractProductDetails(products);
        stockInPage.verifyTableStockIn(softAssert, getDriver(), productDetails);
        // Lưu đơn nhập kho
        stockInPage.saveStock();
        // Kiểm tra sự xuất hiện của Alert
        utils.checkAlertMessage(softAssert, getDriver(), "Các trường bắt buộc không được để trống!");
        // Gọi assertAll() để kiểm tra tất cả các lỗi
        softAssert.assertAll();
    }

    @Test(priority = 4, description = "TC-010 - Xác nhận chức năng thêm đơn nhập kho với trường số lượng được bỏ trống.")
    public void verifyAddStockEntryWithInValidData2() throws IOException, CsvException, InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        Map<String, List<String>> supplierProductData = utils.readCSV("D:\\Auto Testing\\StockTrackv1\\product_supplier.csv");
        List<String[]> products = new ArrayList<>();

        String supplier = stockInPage.getRandomSupplier(supplierProductData);
        String productBySupplier = stockInPage.getRandomProductBySupplier(supplier, supplierProductData, null);

        products.add(new String[]{productBySupplier, ""});
        String date = utils.getCurrentDate();

        stockInPage = new StockInPage(getDriver());
        wait.until(ExpectedConditions.elementToBeClickable(stockInPage.btn_reset));
        stockInPage.resetStockIn();
        stockInPage.addStock(getDriver(), date, supplier, products, "");
        utils.checkAlertMessage(softAssert, getDriver(), "Vui lòng nhập đầy đủ thông tin và đảm bảo số lượng và giá đều lớn hơn 0.");
        Thread.sleep(2000);
        // Gọi assertAll() để kiểm tra tất cả các lỗi
        softAssert.assertAll();
    }

    @Test(priority = 5, description = "TC-011 - Xác nhận chức năng thêm đơn nhập kho với dữ liệu số lượng âm.")
    public void verifyAddStockEntryWithInValidData3() throws IOException, CsvException, InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        Map<String, List<String>> supplierProductData = utils.readCSV("D:\\Auto Testing\\StockTrackv1\\product_supplier.csv");
        List<String[]> products = new ArrayList<>();

        String supplier = stockInPage.getRandomSupplier(supplierProductData);
        String productBySupplier = stockInPage.getRandomProductBySupplier(supplier, supplierProductData, null);

        long quantity = -2;
        products.add(new String[]{productBySupplier, String.valueOf(quantity)});
        String date = utils.getCurrentDate();

        stockInPage = new StockInPage(getDriver());
        wait.until(ExpectedConditions.elementToBeClickable(stockInPage.btn_reset));
        stockInPage.resetStockIn();
        stockInPage.addStock(getDriver(), date, supplier, products, "");
        utils.checkAlertMessage(softAssert, getDriver(), "Vui lòng nhập đầy đủ thông tin và đảm bảo số lượng và giá đều lớn hơn 0.");
        Thread.sleep(2000);
        // Gọi assertAll() để kiểm tra tất cả các lỗi
        softAssert.assertAll();
    }

    @Test(priority = 6, description = "TC-012 - Xác nhận chức năng thêm đơn nhập kho với điều kiện người dùng chọn sản phẩm trước mà không điền thông tin nhà cung cấp.")
    public void verifyAddStockEntryWithInValidData4() throws IOException, CsvException, InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        stockInPage = new StockInPage(getDriver());
        wait.until(ExpectedConditions.elementToBeClickable(stockInPage.btn_reset));
        stockInPage.resetStockIn();

        if (stockInPage.isInIframe(getDriver())) {
            Allure.step("Kiểm tra trạng thái datalist của sản phẩm khi không chọn nhà cung cấp", () -> {
                WebElement field_productCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='type-product-code']")));
                field_productCode.click();
                Thread.sleep(4000);
                // Lấy danh sách sản phẩm từ dropdown
                List<String> actualProductList = new ArrayList<>();
                for (WebElement option : stockInPage.list_productCode) {
                    actualProductList.add(option.getAttribute("value"));
                }
                if (actualProductList.isEmpty()) {
                    Allure.step("Datalist rỗng khi không chọn nhà cung cấp. Thành công.");
                } else {
                    Allure.step("Thất bại: Datalist không trống, có các option hiển thị khi không chọn nhà cung cấp.", () -> {
                                AllureTestListener.saveScreenshotPNG(getDriver());
                                Allure.addAttachment("Danh sách sản phẩm thực tế", String.join(", ", actualProductList));
                                Allure.addAttachment("Danh sách sản phẩm mong đợi", "Không hiển thị");
                    });
                }
                softAssert.assertTrue(actualProductList.isEmpty(),
                        "Datalist không trống, có các option hiển thị khi không chọn nhà cung cấp.");
            });

            utils.checkAlertMessage(softAssert, getDriver(), "Vui lòng chọn thông tin nhà cung cấp.");

            Thread.sleep(2000);

            // Gọi assertAll() để xác nhận tất cả các kiểm tra
            softAssert.assertAll();
        }
    }
    @Test(priority = 7, description = "TC-013 - Xác nhận dữ liệu trong dropdown list của trường Sản phẩm sẽ tương ứng theo từng Nhà Cung Cấp.")
    public void verifyDropdownProductCorrespondsToSupplier() throws IOException, CsvException, InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        stockInPage = new StockInPage(getDriver());
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(stockInPage.btn_reset));
        stockInPage.resetStockIn();

        // Đọc dữ liệu nhà cung cấp và sản phẩm từ file CSV
        Map<String, List<String>> supplierProductData = utils.readCSV("D:\\Auto Testing\\StockTrackv1\\product_supplier.csv");

        if (stockInPage.isInIframe(getDriver())) {
            // Lấy nhà cung cấp ngẫu nhiên
            utils.waitForElement(getDriver(), stockInPage.field_Supplier, 5);
            String randomSupplier = stockInPage.getRandomSupplier(supplierProductData);
            List<String> expectedProductList = supplierProductData.get(randomSupplier);

            // Nhập nhà cung cấp vào trường Supplier
            stockInPage.field_Supplier.sendKeys(randomSupplier);
            wait.until(ExpectedConditions.attributeToBeNotEmpty(stockInPage.field_Supplier, "value"));

            // Mở dropdown sản phẩm
            stockInPage.field_ProductCode.click();
            Thread.sleep(2000);

            // Lấy danh sách sản phẩm từ dropdown
            List<String> actualProductList = new ArrayList<>();
            for (WebElement option : stockInPage.list_productCode) {
                actualProductList.add(option.getAttribute("value"));
            }

            // Log kết quả kiểm tra
            if (actualProductList.equals(expectedProductList)) {
                Allure.step("Thành công: Danh sách sản phẩm của " + randomSupplier + " khớp với danh sách mong đợi.", () -> {

                    Allure.addAttachment("Danh sách sản phẩm thực tế", String.join(", ", actualProductList));
                    Allure.addAttachment("Danh sách sản phẩm mong đợi", String.join(", ", expectedProductList));
                });
            } else {
                Allure.step("Thất bại: Danh sách sản phẩm  của" + randomSupplier + " không khớp với danh sách mong đợi.", () -> {
                    AllureTestListener.saveScreenshotPNG(getDriver());
                    Allure.addAttachment("Danh sách sản phẩm thực tế", String.join(", ", actualProductList));
                    Allure.addAttachment("Danh sách sản phẩm mong đợi", String.join(", ", expectedProductList));
                });
            }

            // Kiểm tra với SoftAssert
            softAssert.assertEquals(actualProductList, expectedProductList,
                    "Danh sách sản phẩm trong dropdown không khớp với danh sách mong đợi của nhà cung cấp " + randomSupplier);

            // Xác nhận tất cả các kiểm tra
            softAssert.assertAll();
        }
    }

}
