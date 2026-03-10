package pages.Flows;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import enums.AlertMessages;
import utils.BasePage;

public class NewFlowPage extends BasePage {

    private final Locator saveChangesButton;
    private final Locator newFlowNameLabel;
    private final Locator editNameButton;
    private final Locator editNameInput;
    private final Locator saveEditNameButton;
    private final Locator searchModulesInput;
    private final Locator addModuleButton;

    public NewFlowPage(Page page) {
        super(page);
        this.saveChangesButton = page.locator("button[data-clarity-label='clicked_button_flow_saveChanges']");
        this.newFlowNameLabel = page.locator("span:has(+ button[data-clarity-label='clicked_button_flows_editName'])");
        this.searchModulesInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search modules"));
        this.addModuleButton = page.locator("button[data-clarity-label='clicked_item_flowModules_activeModules']");
        this.editNameButton = page.locator("button[data-clarity-label='clicked_button_flows_editName']");
        this.editNameInput = page.locator("#flow-name");
        this.saveEditNameButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save").setExact(true));
    }

    private Locator moduleOption(String moduleName) {
        return page.getByText(moduleName, new Page.GetByTextOptions().setExact(true));
    }

    private void fillSearchModulesInput(String moduleName) {
        clearAndFill(searchModulesInput, moduleName);
    }

    private void clickAddModuleButton() {
        click(addModuleButton);
    }


    private void clickEditFlowNameButton(){
        click(editNameButton);
    }

    private void fillNewNameInput(){
        String newName = createNewDateTimeString();
        clearAndFill(editNameInput, newName);
    }

    private void clickSaveNewName(){
        click(saveEditNameButton);
    }


    //Business action
    public NewFlowPage editFlowName() {
        clickEditFlowNameButton();
        fillNewNameInput();
        clickSaveNewName();
        logStep("Flow name has been changed successfully.");
        return this;
    }


    public String getNewFlowName() {
        String newFlowName = getText(newFlowNameLabel);
        logStep("Flow name that should be created is: " + newFlowName);
        return newFlowName;
    }


    public NewFlowPage clickOnSaveChanges() {
        click(saveChangesButton);
        return this;
    }

    public NewFlowPage addModuleForFlow(String moduleName) {
        fillSearchModulesInput(moduleName);
        hover(moduleOption(moduleName));
        clickAddModuleButton();
        return this;
    }


    public NewFlowPage assertSuccessfulFlowCreationMessageAppeared() {
        assertCorrectAlertMessageAppears(AlertMessages.FLOW_SAVED.getValue());
        waitForAlertMessageToDisappear();
        return this;
    }
}
