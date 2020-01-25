package demo.ui;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class UserAccountForm extends PageObject {

  public static final Target LOGIN_FIELD =
      Target.the("login").locatedBy("css:input[data-qa='login-input']");
  public static final Target FIRSTNAME_FIELD =
      Target.the("firstName").locatedBy("css:input[data-qa='first-name-input']");
  public static final Target LASTNAME_FIELD =
      Target.the("lastName").locatedBy("css:input[data-qa='last-name-input']");
  public static final Target EMAIL_FIELD =
      Target.the("email").locatedBy("css:input[data-qa='email-input']");
  public static final Target TWO_FA_CHECKBOX = Target.the("2FA").located(By.id("two-fa"));
  public static final Target ROLE_OPTION = Target.the("role").located(By.name("authority"));
  public static final Target SAVE_BUTTON =
      Target.the("save").locatedBy("css:button[data-qa='save-user-button']");
  public static final Target CANCEL_BUTTON =
      Target.the("cancel").locatedBy("css:button[data-qa='cancel-user-button']");
}
