package demo.tasks;

import demo.ui.UserManagementPage;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;

public class CreateAccount {

  public static Performable forNewUser() {
    return Task.where(
        "{0} attempts to create new user account", Click.on(UserManagementPage.CREATE_NEW_USER));
  }
}
