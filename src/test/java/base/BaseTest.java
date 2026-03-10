package base;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.LoginPage;
import utils.ConfigReader;
import utils.ExtentReportManager;
import utils.PlaywrightFactory;

import java.lang.reflect.Method;
import java.util.Base64;

public class BaseTest {

    private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();

    protected Page getPage() {
        return tlPage.get();
    }

    @BeforeSuite
    public void beforeSuite() {
        // Initialize ExtentReports once per suite
        ExtentReportManager.getInstance();
    }

    @Parameters({"browser", "headless"})
    @BeforeMethod
    public void setUp(@Optional("chromium") String browser,
                      @Optional("false") String headless,
                      Method method) {
        // Start browser
        Page page = PlaywrightFactory.initBrowser(browser, Boolean.parseBoolean(headless));
        tlPage.set(page);
        page.navigate(ConfigReader.get("baseURL"));

        // Login
        LoginPage login = new LoginPage(page);
        login.login(ConfigReader.get("email"), ConfigReader.get("password"));
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Compose a more descriptive test name
        String testName = String.format("%s [%s | headless=%s]",
                method.getName(),
                browser,
                headless);

        // Create ExtentTest for this test method
        ExtentTest test = ExtentReportManager.getInstance().createTest(testName);
        ExtentReportManager.setTest(test);
        test.info("Browser launched and user logged in");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        Page page = getPage();
        ExtentTest extentTest = ExtentReportManager.getTest();

        if (result.getStatus() == ITestResult.FAILURE) {
            try {
                byte[] screenshotBytes = page.screenshot(
                        new Page.ScreenshotOptions().setFullPage(true)
                );
                String base64Screenshot = Base64.getEncoder().encodeToString(screenshotBytes);

                if (extentTest != null) {
                    extentTest.fail("Test failed: " + result.getThrowable(),
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                }
            } catch (Exception e) {
                if (extentTest != null) {
                    extentTest.fail("Failed to capture screenshot: " + e.getMessage());
                }
            }
        } else if (result.getStatus() == ITestResult.SKIP) {
            if (extentTest != null) {
                extentTest.skip("Test skipped: " + result.getThrowable());
            }
        } else if (extentTest != null) {
            extentTest.pass("Test passed");
        }

        // Remove test from ThreadLocal (important for parallel execution)
        ExtentReportManager.removeTest();

        // Close browser
        tlPage.remove();
        PlaywrightFactory.closeBrowser();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        // Flush all ExtentReports to disk
        ExtentReportManager.flushReports();
    }
}