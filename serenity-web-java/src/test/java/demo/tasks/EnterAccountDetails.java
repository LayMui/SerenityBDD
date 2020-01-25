package demo.tasks;

import demo.ui.UserAccountForm;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;

public class EnterAccountDetails {

  public static Performable withUserInfo(
      String login, String firstName, String lastName, String email, String role) {
    return Task.where(
            "{0} attempts to enter user info #login and #firstName, #lastName, #email, #role",
            Clear.field(UserAccountForm.LOGIN_FIELD),
            Enter.theValue(login).into(UserAccountForm.LOGIN_FIELD),
            Clear.field(UserAccountForm.FIRSTNAME_FIELD),
            Enter.theValue(firstName).into(UserAccountForm.FIRSTNAME_FIELD),
            Clear.field(UserAccountForm.LASTNAME_FIELD),
            Enter.theValue(lastName).into(UserAccountForm.LASTNAME_FIELD),
            Clear.field(UserAccountForm.EMAIL_FIELD),
            Enter.theValue(email).into(UserAccountForm.EMAIL_FIELD),
            SelectFromOptions.byVisibleText(role).from(UserAccountForm.ROLE_OPTION))
        .with("login")
        .of(login)
        .with("firstName")
        .of(firstName)
        .with("lastName")
        .of(lastName)
        .with("email")
        .of(email);
  }

  public static Performable withUserInfo2FA(
      String login, String firstName, String lastName, String email, String twoFA, String role) {
    return Task.where(
            "{0} attempts to enter user info #login and #firstName, #lastName, #email, #twoFA, #role",
            Clear.field(UserAccountForm.LOGIN_FIELD),
            Enter.theValue(login).into(UserAccountForm.LOGIN_FIELD),
            Clear.field(UserAccountForm.FIRSTNAME_FIELD),
            Enter.theValue(firstName).into(UserAccountForm.FIRSTNAME_FIELD),
            Clear.field(UserAccountForm.LASTNAME_FIELD),
            Enter.theValue(lastName).into(UserAccountForm.LASTNAME_FIELD),
            Clear.field(UserAccountForm.EMAIL_FIELD),
            Enter.theValue(email).into(UserAccountForm.EMAIL_FIELD),
            Click.on(UserAccountForm.TWO_FA_CHECKBOX),
            SelectFromOptions.byVisibleText(role).from(UserAccountForm.ROLE_OPTION))
        .with("login")
        .of(login)
        .with("firstName")
        .of(firstName)
        .with("lastName")
        .of(lastName)
        .with("email")
        .of(email);
  }

  public static Performable save() {
    return Task.where("{0} attempts to save user account", Click.on(UserAccountForm.SAVE_BUTTON));
  }

  public static Performable cancel() {
    return Task.where(
        "{0} attempts to cancel user account", Click.on(UserAccountForm.CANCEL_BUTTON));
  }
}
