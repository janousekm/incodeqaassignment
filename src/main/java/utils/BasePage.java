package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class BasePage {

    protected Page page;
    private final double defaultTimeout;
    private final Locator alertMessage;

    public BasePage(Page page) {
        this(page, 30_000);
    }

    public BasePage(Page page, double defaultTimeoutMs) {
        this.page = page;
        this.defaultTimeout = defaultTimeoutMs;
        this.alertMessage = page.locator("div[role='alert']");

        page.setDefaultTimeout(defaultTimeoutMs);
    }

    // ── Click and mouse operations ───────────────────────────────────────────────

    protected void click(Locator locator) {
        waitForAttached(locator);
        locator.click();
    }

    protected void hover(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(5000));
        locator.hover();
    }

    // ── Type (keystroke-by-keystroke) ───────────────────────

    protected void type(Locator locator, String text) {
        waitForAttached(locator);
        locator.pressSequentially(text);
    }

    // ── Fill (instant value set) ────────────────────────────

    protected void fill(Locator locator, String value) {
        locator.fill(value);
    }

    protected void clearAndFill(Locator locator, String value) {
        waitForAttached(locator);
        locator.clear();
        locator.fill(value);
    }

    // ── Fill with retries ───────────────────────────────────

    protected void fillWithRetry(Locator locator, String value, int maxRetries) {
        waitForAttached(locator);
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            locator.fill(value);
            String actual = locator.inputValue();
            if (value.equals(actual)) {
                return;
            }
            if (attempt == maxRetries) {
                throw new RuntimeException(
                        "Fill failed after " + maxRetries + " attempts. Expected: \"" + value + "\", got: \"" + actual + "\"");
            }
            page.waitForTimeout(500);
        }
    }

    protected void fillWithRetry(Locator locator, String value) {
        waitForAttached(locator);
        fillWithRetry(locator, value, 3);
    }

    // ── Wait helpers ────────────────────────────────────────

    protected void waitForAttached(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED)
                .setTimeout(defaultTimeout));
    }

    protected void waitForAttached(Locator locator, double timeoutMs) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED)
                .setTimeout(timeoutMs));
    }

    protected void waitForHidden(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(defaultTimeout));
    }

    protected void waitForDetached(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.DETACHED)
                .setTimeout(defaultTimeout));
    }

    // ── Utility ─────────────────────────────────────────────

    protected String getText(Locator locator) {
        return locator.textContent();
    }


    // ── Logging ─────────────────────────────────────────────

    protected void logStep(String message) {
        com.aventstack.extentreports.ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.info(message);
        }
    }


    // ── Verification helpers ─────────────────────────────────────────────

    protected void assertCorrectAlertMessageAppears(String expectedMessage) {
        alertMessage.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(5000));
        String actualText = getText(alertMessage).toLowerCase();
        Assert.assertEquals(actualText, expectedMessage.toLowerCase(), "Assertion error! Expected: " + expectedMessage + " but got " + actualText);

    }

    protected void waitForAlertMessageToDisappear() {
        waitForDetached(alertMessage);
    }

    protected String createNewDateTimeString() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return myDateObj.format(myFormatObj);
    }
}
