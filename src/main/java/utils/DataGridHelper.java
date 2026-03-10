package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.options.WaitUntilState.NETWORKIDLE;


/**
 * Utility class for DataGrid actions.
    * Provides methods to interact with data grids, such as clicking rows and retrieving cell values.
 */

public class DataGridHelper extends BasePage{

    private static final String defaultTableLocator = "table";

    private final String tableSelector;
    private final Locator addFilterButton;
    private final Locator filterBySessionInput;
    private final Locator filterByNameInput;
    private final Locator applyFilterButton;

    public DataGridHelper(Page page) {
        this(page, defaultTableLocator);
    }

    public DataGridHelper(Page page, String tableSelector) {
        super(page);
        this.addFilterButton = page.locator("#filter-menu-trigger");
        this.filterBySessionInput = page.locator("#sessionId");
        this.filterByNameInput = page.locator("input[id='name']");
        this.applyFilterButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Apply"));
        this.tableSelector = tableSelector;
    }

    // Locator accessors
    private Locator table() {
        return page.locator(tableSelector);
    }

    private Locator headers() {
        return table().locator("thead th");
    }

    private Locator rows() {
        return table().locator("tbody tr");
    }

    private Locator cells(Locator row) {
        return row.locator("td");
    }

    private Locator filterOptionByGroupTitle(String groupTitle, String buttonName) {
        return page.locator("div.menu-group")
                .filter(new Locator.FilterOptions().setHasText(groupTitle))
                .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(buttonName));
    }

    private Locator filterByOption(String buttonName) {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(buttonName).setExact(true));
    }

    //DataGrid actions

    //Get column index by header name
    private int getColumnIndex(String headerName) {
        int count = headers().count();
        for (int i = 0; i < count; i++) {
            if (headers().nth(i).innerText().trim().equalsIgnoreCase(headerName)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Column not found: " + headerName);
    }

    public void clickRow(int rowIndex) {
        rowIndex = rowIndex - 1;
        rows().nth(rowIndex).click();
    }

    // Wait for row to appear (with page refresh) then click
    public void waitForRow(int rowIndex, int maxRetries) {
        int index = rowIndex - 1;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            if (rows().count() > index && rows().nth(index).isVisible()) {
                return;
            }
            page.reload(new Page.ReloadOptions().setWaitUntil(NETWORKIDLE));
        }
        throw new IllegalStateException("Row " + rowIndex + " not visible after " + maxRetries + " retries");
    }

    // Get value by row + column header
    public String getCellValue(int rowIndex, String headerName) {
        rowIndex = rowIndex - 1;
        int colIndex = getColumnIndex(headerName);
        return cells(rows().nth(rowIndex)).nth(colIndex).innerText().trim();
    }

    // ── DataGrid filter helpers ─────────────────────────────────────────────

    public void clickAddFilterButton() {
        click(addFilterButton);
    }

    public void clickFilterOption(String groupTitle, String buttonName) {
        click(filterOptionByGroupTitle(groupTitle, buttonName));
    }

    public void clickFilterOption(String buttonName) {
        click(filterByOption(buttonName));
    }

    public void fillFilterBySessionIDInput(String sessionId) {
        clearAndFill(filterBySessionInput, sessionId);
    }

    public void fillFilterByNameInput(String name) {
        clearAndFill(filterByNameInput, name);
    }

    public void clickApplyFilterButton() {
        click(applyFilterButton);
        waitForDataGridFiltering();
    }


    // Wait for grid to be filtered
    public void waitForDataGridFiltering()
    {
        page.waitForCondition(
                () -> rows().count() >= 1,
                new Page.WaitForConditionOptions().setTimeout(10000)
        );
    }

}