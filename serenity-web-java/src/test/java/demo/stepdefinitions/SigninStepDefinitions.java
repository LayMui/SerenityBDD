package demo.stepdefinitions;

import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.CoreMatchers.equalTo;

import demo.tasks.EnterLoginDetails;
import demo.ui.LoginPage;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.questions.WebElementQuestion;
import net.serenitybdd.screenplay.questions.page.TheWebPage;

public class SigninStepDefinitions {

  LoginPage loginPage;

  @Before(value = "@signin")
  public void setTheStage() {
    OnStage.setTheStage(new OnlineCast());
  }

  @Given("^(.*) is at the demo admin url page")
  public void jamesIsAtThedemoAdminUrlPage(String actor) {
    theActorCalled(actor).attemptsTo(Open.browserOn().the(loginPage));
  }

  @When("^(.*) sign in with username = \"(.*)\" and password = \"(.*)\"")
  public void jamesSignInWithUsernameAndPassword(String actor, String username, String password) {
    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(loginPage.USERNAME_FIELD),
                WebElementStateMatchers.isVisible()));
    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(loginPage.PASSWORD_FIELD),
                WebElementStateMatchers.isVisible()));
    theActorInTheSpotlight().attemptsTo(EnterLoginDetails.forBotAdminUser(username, password));
  }

  @Then("^(.*) is able to login")
  public void jamesIsAbleToLogin(String actor) {
    theActorInTheSpotlight().should(eventually(seeThat(TheWebPage.title(), equalTo("Intents"))));
  }

  @Then("^(.*) is unable to login")
  public void jamesIsUnableToLogin(String actor) {
    theActorInTheSpotlight().should(eventually(seeThat(TheWebPage.title(), equalTo("Admin"))));

    theActorInTheSpotlight()
        .should(
            seeThat(WebElementQuestion.the(loginPage.ERROR), WebElementStateMatchers.isVisible()));
  }
}
