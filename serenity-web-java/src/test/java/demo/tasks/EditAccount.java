package demo.tasks;

import demo.ui.HomePage;
import demo.ui.UserAccountForm;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;

public class EditAccount {

  public static Performable forUser(String login, String value) {
    return Task.where(
        "{0} attempts to edit user account #login",
        Click.on(HomePage.ACTION_DROPDOWN(login)), Click.on(HomePage.EDIT_OPTION));
  }

  public static Performable editlastField(String login, String value) {
    return Task.where(
            "{0} attempts to edit lastName field of user login #login with #value",
            Clear.field(UserAccountForm.LASTNAME_FIELD),
            Enter.theValue(value).into(UserAccountForm.LASTNAME_FIELD))
        .with("lastName")
        .of(value);
  }
}
