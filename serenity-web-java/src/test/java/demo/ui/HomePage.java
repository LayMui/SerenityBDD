package demo.ui;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class HomePage extends PageObject {

  public static final Target ACCOUNT_MENU_DROPDOWN =
      Target.the("accountMenu").located(By.id("account-menu"));
  public static final Target USER_MANAGEMENT_OPTION =
      Target.the("userManagementOption").locatedBy("css:a[href='#/admin']");
  public static final Target TEACHING_OPTION =
      Target.the("teachingOption").locatedBy("css:a[href='#/enquiry']");
  public static final Target SETTINGS_OPTION =
      Target.the("settingsOption").locatedBy("css:a[href='#/settings']");
  public static final Target PASSWORD_OPTION =
      Target.the("passwordOption").locatedBy("css:a[href='#/password']");
  public static final Target SESSION_OPTION =
      Target.the("sessionOption").locatedBy("css:a[href='#/sessions']");
  public static final Target TWO_FA_OPTION =
      Target.the("twoFactorOption").located(By.id("twoFactorSetup"));
  public static final Target LOGOUT_OPTION = Target.the("logoutOption").located(By.id("logout"));

  public static final Target INTENT_TAB =
      Target.the("intentTab").locatedBy("css:a[href='#/intents']");
  public static final Target REPORTING_TAB =
      Target.the("reportingTab").locatedBy("css:a[href='#/ask-admin-report']");
  public static final Target ONTOLOGY_TAB =
      Target.the("ontologyTab").locatedBy("css:a[href='#/ontology-editor']");
  public static final Target LIBRARIES_TAB =
      Target.the("librariesTab").locatedBy("css:a[href='#/libraries']");

  public static final Target ACTION_DROPDOWN(String login) {
    String actionDropdownLocator = String.format("css:button[data-qa='%s-action-dropdown']", login);
    return Target.the("action dropdown").locatedBy(actionDropdownLocator);
  }

  public static final Target EDIT_OPTION =
      Target.the("edit option").locatedBy("//button[text()='Edit']");

  public static final Target DELETE_OPTION(String login) {
    String deleteOptionLocator = String.format("css:button[data-qa='%s-delete-button']", login);
    return Target.the("delete option").locatedBy(deleteOptionLocator);
  }

  public static final Target CONFIRM_DELETE_BUTTON =
      Target.the("confirm delete user").locatedBy("css:button[data-qa='confirm-delete-user']");
}
