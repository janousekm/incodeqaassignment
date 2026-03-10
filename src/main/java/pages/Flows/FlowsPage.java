package pages.Flows;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import enums.AlertMessages;
import utils.BasePage;
import utils.DataGridHelper;

import java.util.List;
import java.util.stream.Collectors;

public class FlowsPage extends BasePage {
    private final DataGridHelper dataGrid;

    //Locators
    private final Locator addNewFlowButton;


    private final Locator findAFlowInput;

    private final Locator flowsModulesLabel;
    private final Locator flowMoreActionsButton;
    private final Locator deleteFlowButton;
    private final Locator confirmDeleteFlowButton;



    public FlowsPage(Page page) {
        super(page);
        this.addNewFlowButton = page.locator("button[data-clarity-label='clicked_button_flows_new']");

        this.findAFlowInput = page.locator("input[name='searchName']");
        this.flowsModulesLabel = page.locator("button[data-clarity-label='clicked_action_flows_modules'] + span p");
        this.flowMoreActionsButton = page.locator("button[data-clarity-label='clicked_action_flows_moreActions']");
        this.deleteFlowButton = page.locator("div[role='menuitem']", new Page.LocatorOptions().setHasText("Delete Flow"));
        this.confirmDeleteFlowButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm"));
        this.dataGrid = new DataGridHelper(page);
    }



    private void fillFindAFlowInput(String flowName) {
        fill(findAFlowInput, flowName);
        dataGrid.clickApplyFilterButton();
    }

    private void clickMoreActionsButton(){
        click(flowMoreActionsButton);
    }

    private void clickDeleteFlowButton(){
        click(deleteFlowButton);
    }

    private void clickConfirmDeleteButton(){
        click(confirmDeleteFlowButton);
    }



    //Business logic
    public FlowsPage clickOnAddNewFlowButton() {
        click(addNewFlowButton);
        return this;
    }

    public FlowsPage assertSuccessfulFlowDeletionMessageAppeared() {
        assertCorrectAlertMessageAppears(AlertMessages.FLOW_REMOVED.getValue());
        return this;
    }

    public FlowsPage filterFlowGrid(String flowName) {
        fillFindAFlowInput(flowName);
        return this;
    }

    public List<String> getAllFlowModules() {
        waitForAttached(flowsModulesLabel.first());
        List<String> flowModules = flowsModulesLabel.allTextContents()
                .stream()
                .map(text -> text.replaceFirst("^\\d+\\.\\s*", "").trim())
                .collect(Collectors.toList());
        logStep("Modules from Flows page: " + flowModules);
        return flowModules;
    }

    public FlowsPage deleteFlowAndConfirmDeletion() {
        clickMoreActionsButton();
        clickDeleteFlowButton();
        clickConfirmDeleteButton();
        assertSuccessfulFlowDeletionMessageAppeared();
        return this;
    }
}
