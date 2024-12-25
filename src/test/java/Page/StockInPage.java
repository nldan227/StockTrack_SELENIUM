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
import Utils.utils;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.security.Key;
import java.time.Duration;
import java.util.*;

public class StockInPage {
    @FindBy(xpath = "//input[@id='import-date']")
    public WebElement field_importDate;
    @FindBy(xpath = "//input[@id='type-supplier']")
    public WebElement field_Supplier;
    @FindBy(xpath = "//datalist[@id='suppliers']/option")
    public List<WebElement> list_Suppliers;
    @FindBy(xpath = "//input[@id='type-product-code']")
    public WebElement field_ProductCode;
    @FindBy(xpath = "//datalist[@id='product-code']/option")
    public List<WebElement> list_productCode;
    @FindBy(id = "quantity")
    public WebElement field_quantity;
    @FindBy(id = "price")
    public WebElement field_price;
    @FindBy(xpath = "//input[@id='note']")
    public WebElement field_note;
    @FindBy(id = "add-product")
    public WebElement btn_addProduct;
    @FindBy(xpath = "//input[@id='total-amount']")
    public WebElement field_totalPrice;
    @FindBy(id = "saveStock")
    public WebElement btn_saveStock;
    @FindBy(xpath = "//button[@id='reset']")
    public WebElement btn_reset;
    @FindBy(id = "close-ifame")
    public WebElement btn_closeIframe;
    public StockInPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @Step("Thêm đơn nhập kho với các thông tin Ngày nhập: {1}, Nhà cung cấp: {2}, Ghi chú: {4} ")
    public void addStock(WebDriver driver, String importDate, String supplier, List<String[]> products, String note) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        if (isInIframe(driver)) {
            wait.until(ExpectedConditions.elementToBeClickable(field_importDate));
            // Chọn ngày nhập
            field_importDate.sendKeys(importDate);
            //Chọn nhà cung cấp
            field_Supplier.click();  // Kích hoạt datalist
            field_Supplier.sendKeys(supplier);
            // Lặp qua từng sản phẩm trong danh sách
            for (String[] productInfo : products) {
                String product = productInfo[0];   // Sản phẩm
                String quantity = productInfo[1];  // Số lượng (chuyển từ String sang long)
                Allure.step(String.format("Chi tiết sản phẩm: Tên: %s, Số lượng: %s", product, quantity));
                // Chọn sản phẩm
                field_ProductCode.click();  // Kích hoạt datalist
                Thread.sleep(500);
                field_ProductCode.clear();
                Thread.sleep(1000); // Chờ sản phẩm load
                field_ProductCode.sendKeys(product);
                Thread.sleep(2000); // Chờ sản phẩm load
                // Nhập số lượng
                field_quantity.click();
                field_quantity.sendKeys(quantity);
                Thread.sleep(2000);
                // Nhập ghi chú
                field_note.sendKeys(note);
                // Thêm sản phẩm
                btn_addProduct.click();
            }
        }
    }
    public void saveStock(){
        // Nhấn lưu
        btn_saveStock.click();
    }
    public boolean isInIframe(WebDriver driver) {
        try {
            driver.switchTo().defaultContent(); // Quay lại trang chính
            driver.switchTo().frame("iframe-container"); // Cố gắng chuyển vào iframe
            return true;  // Nếu không có lỗi, tức là đang ở trong iframe
        } catch (Exception e) {
            return false; // Nếu có lỗi, tức là không ở trong iframe
        }
    }

    public  Map<String, List<String>> extractProductDetails(List<String[]> products) {
        List<String> productCode = new ArrayList<>();
        List<String> productName = new ArrayList<>();
        List<String> listQuantity = new ArrayList<>();
        for (String[] product : products) {
            String[] productDetails = product[0].split(" - ");
            productCode.add(productDetails[0]);
            productName.add(productDetails[1]);
            listQuantity.add(product[1]);
        }
        Map<String, List<String>> result = new HashMap<>();
        result.put("productCode", productCode);
        result.put("productName", productName);
        result.put("listQuantity", listQuantity);
        return result;
    }
    public void verifyTableStockIn(SoftAssert softAssert, WebDriver driver, Map<String, List<String>> productDetails) throws InterruptedException {
        List<String> productCode = productDetails.get("productCode");
        List<String> productName = productDetails.get("productName");
        List<String> listQuantity = productDetails.get("listQuantity");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("product-list")));
        List<WebElement> rows = table.findElements(By.xpath("//tbody//tr"));
        double expectedTotal = 0.0;

        // Kiểm tra dữ liệu từng hàng trong bảng
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            WebElement row = rows.get(rowIndex);
            List<WebElement> allCells = row.findElements(By.tagName("td"));

            // Kiểm tra Mã sản phẩm
            logAndVerify(driver, softAssert, "Kiểm tra Mã sản phẩm", allCells.get(0).getText(), productCode.get(rowIndex),
                    "Mã sản phẩm trong bảng (" + allCells.get(0).getText() + ") không khớp với mã sản phẩm mong đợi (" + productCode.get(rowIndex) + ")");

            // Kiểm tra Tên sản phẩm
            logAndVerify(driver, softAssert, "Kiểm tra Tên sản phẩm", allCells.get(1).getText(), productName.get(rowIndex),
                    "Tên sản phẩm trong bảng (" + allCells.get(1).getText() + ") không khớp với tên sản phẩm mong đợi (" + productName.get(rowIndex) + ")");

            // Kiểm tra Số lượng
            logAndVerify(driver, softAssert, "Kiểm tra Số lượng", allCells.get(2).getText(), listQuantity.get(rowIndex),
                    "Số lượng sản phẩm trong bảng (" + allCells.get(2).getText() + ") không khớp với số lượng sản phẩm mong đợi (" + listQuantity.get(rowIndex) + ")");

            // Kiểm tra Tổng tiền cho từng sản phẩm
            String quantityText = allCells.get(2).getText();
            String priceText = allCells.get(3).getText();
            String totalAmountText = allCells.get(4).getText();

            double quantity = Double.parseDouble(quantityText);
            double price = Double.parseDouble(priceText.replaceAll("[^\\d]", ""));
            double expectedTotalAmount = quantity * price;

            String formattedAmount = String.format(Locale.GERMANY, "%,.0f VNĐ", expectedTotalAmount);
            logAndVerify(driver, softAssert, "Kiểm tra Tổng tiền sản phẩm", totalAmountText, formattedAmount,
                    "Tổng tiền sản phẩm trong bảng (" + totalAmountText + ") không khớp với tổng tiền sản phẩm mong đợi (" + formattedAmount + ")");

            expectedTotal += expectedTotalAmount;
        }

        // Kiểm tra Tổng tiền nhập kho
        utils.scrollDown(driver);
        Thread.sleep(1000);
        utils.waitForElement(driver, field_totalPrice, 5);
        String totalPrice = field_totalPrice.getAttribute("value");
        String formattedTotal = String.format(Locale.GERMANY, "%,.0f VNĐ", expectedTotal);

        logAndVerify(driver, softAssert, "Kiểm tra Tổng tiền nhập kho", totalPrice, formattedTotal,
                "Tổng tiền nhập kho trong bảng (" + totalPrice + ") không khớp với tổng tiền nhập kho mong đợi (" + formattedTotal + ")");
    }

    // Phương thức log và kiểm tra giá trị
    private void logAndVerify(WebDriver driver, SoftAssert softAssert, String stepDescription, String actualValue, String expectedValue, String errorMessage) {
        Allure.step(stepDescription, () -> {
            if (!expectedValue.equals(actualValue)) {
                AllureTestListener.saveScreenshotPNG(driver); // Chụp ảnh màn hình khi thất bại
                Allure.addAttachment("Chi tiết lỗi", " Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
            } else {
                Allure.step("Thành công: " + stepDescription + " Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
            }
            softAssert.assertTrue(expectedValue.equals(actualValue), errorMessage);
        });
    }
    public static String getRandomSupplier(Map<String, List<String>> data){
        Random random = new Random();
        List<String> suppliers = new ArrayList<>(data.keySet());
        String randomSupplier = suppliers.get(random.nextInt(suppliers.size()));
        return randomSupplier;
    }
    // Lấy sản phẩm ngẫu nhiên của một nhà cung cấp khác sản phẩm đã chọn
    public static String getRandomProductBySupplier(String supplier, Map<String, List<String>> supplierProductData, String excludeProduct) {
        Random rand = new Random();
        // Lấy danh sách các sản phẩm của nhà cung cấp từ Map
        List<String> products = supplierProductData.get(supplier);
        // Kiểm tra xem nhà cung cấp có tồn tại trong map không
        if (products == null) {
            return null; // Nếu không có sản phẩm cho nhà cung cấp, trả về null hoặc xử lý tùy theo yêu cầu
        }
        // Loại bỏ sản phẩm đã chọn (excludeProduct)
        products.remove(excludeProduct);
        // Chọn ngẫu nhiên một sản phẩm còn lại
        if (products.isEmpty()) {
            return null; // Nếu không còn sản phẩm nào, trả về null
        }
        int productIndex = rand.nextInt(products.size());
        return products.get(productIndex);
    }
    public void resetStockIn(){
        btn_reset.click();
    }

}




