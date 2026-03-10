package pages.Sessions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import enums.AlertMessages;
import utils.BasePage;

public class SingleSessionDetailsPage extends BasePage {

    private final Locator addFaceToDatabaseButton;

    public SingleSessionDetailsPage(Page page) {
        super(page);
        this.addFaceToDatabaseButton = page.locator("button[data-clarity-label='clicked_button_singleSession_addFaceToDatabase']");
    }


    //Business logic
    public String getFieldValue(String labelText) {
        Locator field = page.locator("div.dinamic-field")
                .filter(new Locator.FilterOptions().setHas(page.locator("h5", new Page.LocatorOptions().setHasText(labelText))))
                .locator("span.content");
        String fieldValue = field.innerText().trim().toLowerCase();
        logStep("Field value for '" + labelText + "': " + fieldValue);
        return fieldValue;
    }

    public String getOcrFullName() {
        return getFieldValue("Full Name (OCR)");
    }

    public SingleSessionDetailsPage clickAddFaceToDatabaseButton() {
        click(addFaceToDatabaseButton);
        return this;
    }

    public SingleSessionDetailsPage assertSuccessfulMessageAppeared() {
        assertCorrectAlertMessageAppears(AlertMessages.FACE_ADDED_TO_DB.getValue());
        logStep("Correct success message is shown: " + AlertMessages.FACE_ADDED_TO_DB.getValue());
        return this;
    }

}
