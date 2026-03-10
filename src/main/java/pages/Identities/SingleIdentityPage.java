package pages.Identities;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import enums.AlertMessages;
import utils.BasePage;

public class SingleIdentityPage extends BasePage {

    private final Locator identityOptionsButton;
    private final Locator deletePIIDataButton;
    private final Locator confirmDeleteButton;


    public SingleIdentityPage(Page page) {
        super(page);
        this.identityOptionsButton = page.locator("div[data-clarity-label='clicked_button_singleIdentity_actionMenu']");
        this.deletePIIDataButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete identity PII data"));
        this.confirmDeleteButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete").setExact(true));
    }

    private void clickOnIdentityOptionMenu(){
        click(identityOptionsButton);
    }

    private void deletePIIDataForIdentity()
    {
        click(deletePIIDataButton);
        click(confirmDeleteButton);
    }

    private void assertSuccessfulDeleteMessageAppeared(){
        assertCorrectAlertMessageAppears(AlertMessages.IDENTITY_REMOVED.getValue());
        logStep("Identity removed successfully with message: " +AlertMessages.IDENTITY_REMOVED.getValue());
    }

    //Business logic
    public SingleIdentityPage deleteIdentity()
    {
        clickOnIdentityOptionMenu();
        deletePIIDataForIdentity();
        assertSuccessfulDeleteMessageAppeared();
        return this;
    }


}
