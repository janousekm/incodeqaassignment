package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import utils.BasePage;

public class LoginPage extends BasePage {

    // Locators
    private final Locator emailInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator dashboardIndicator;

    public LoginPage(Page page) {
        super(page);

        this.emailInput = page.locator("#email");
        this.passwordInput = page.locator("#password");
        this.loginButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Log In")
        );
        this.dashboardIndicator = page.locator("div[data-testid='home-title']");
    }

    // Page actions
    private void fillUsername(String email) {
        fill(emailInput, email);
    }

    private void fillPassword(String password) {
        fillWithRetry(passwordInput, password);
    }

    private void clickLogin() {
        click(loginButton);
    }

    // Business Action
    public LoginPage login(String username, String password) {
        fillUsername(username);
        fillPassword(password);
        clickLogin();
        waitForAttached(dashboardIndicator);
        return this;
    }

}
