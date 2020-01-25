package demo.tasks;

import demo.ui.HomePage;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;

public class Access {

  public static Performable userManagement() {
    return Task.where(
        "{0} attempts to access user management", Click.on(HomePage.USER_MANAGEMENT_OPTION));
  }

  public static Performable accountMenu() {
    return Task.where(
        "{0} attempts to access user management", Click.on(HomePage.ACCOUNT_MENU_DROPDOWN));
  }

  public static Performable logout() {
    return Task.where("{0} attempts to access logout", Click.on(HomePage.LOGOUT_OPTION));
  }
}
