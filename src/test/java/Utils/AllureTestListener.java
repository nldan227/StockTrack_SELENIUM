package Utils;

import SetUp.Setup;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.commons.logging.Log;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AllureTestListener implements ITestListener {
    public String getTestName(ITestResult result) {
        return result.getTestName() != null ? result.getTestName()
                : result.getMethod().getConstructorOrMethod().getName();
    }

    public String getTestDescription(ITestResult result) {
        return result.getMethod().getDescription() != null ? result.getMethod().getDescription() : getTestName(result);
    }
    @Attachment (value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    @Attachment (value = "{0}", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }

    @Attachment (value = "Page screenshot", type = "image/png")
    public static byte[] saveScreenshotPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
//    @Override
//    public void onTestSuccess(ITestResult iTestResult) {
//        // Optional: Add a custom Allure step for success
//        Allure.step("Test passed: " + getTestName(iTestResult));
//    }
    @Override
    public void onTestFailure(ITestResult iTestResult) {
//        saveScreenshotPNG(Setup.driver);
//        //Save a log on Allure report.
//        saveTextLog(getTestName(iTestResult) + " failed and screenshot taken!");
    }

}
