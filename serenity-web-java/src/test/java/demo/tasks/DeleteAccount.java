package demo.tasks;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import demo.ui.HomePage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Click;
import net.thucydides.core.annotations.Step;

public class DeleteAccount implements Performable {

  private String login;

  public DeleteAccount(String login) {
    this.login = login;
  }

  public static Performable forUser(String login) {
    return instrumented(DeleteAccount.class, login);
  }

  @Override
  @Step("{0} delete the user with login #login")
  public <T extends Actor> void performAs(T actor) {
    actor.attemptsTo(
        Click.on(HomePage.ACTION_DROPDOWN(login)),
        Click.on(HomePage.DELETE_OPTION(login)),
        Click.on(HomePage.CONFIRM_DELETE_BUTTON));
  }
}
