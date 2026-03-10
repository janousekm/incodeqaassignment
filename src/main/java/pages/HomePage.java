package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import enums.MenuItem;
import utils.BasePage;

public class HomePage extends BasePage {
    public HomePage(Page page) {
        super(page);
    }


    private Locator menuItem(MenuItem item) {
        return page.locator("[data-clarity-label='clicked_sideMenu_" + item.getValue() + "']");
    }

    public HomePage navigateTo(MenuItem item) {
        click(menuItem(item));
        page.waitForLoadState(LoadState.NETWORKIDLE);
        logStep("Navigating to: "+item);
        return this;
    }
}
