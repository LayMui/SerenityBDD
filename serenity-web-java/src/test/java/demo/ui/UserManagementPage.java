package demo.ui;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class UserManagementPage extends PageObject {

  public static final Target CREATE_NEW_USER =
      Target.the("createNewUser").locatedBy("css:button[data-qa='create-new-user']");
}
