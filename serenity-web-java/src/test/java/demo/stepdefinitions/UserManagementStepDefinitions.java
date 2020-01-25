package demo.stepdefinitions;

import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.CoreMatchers.equalTo;

import demo.tasks.Access;
import demo.tasks.CreateAccount;
import demo.tasks.DeleteAccount;
import demo.tasks.EditAccount;
import demo.tasks.EnterAccountDetails;
import demo.tasks.EnterLoginDetails;
import demo.ui.HomePage;
import demo.ui.LoginPage;
import demo.ui.UserAccountForm;
import demo.ui.UserManagementPage;
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

public class UserManagementStepDefinitions {

  LoginPage loginPage;
  HomePage homePage;
  UserManagementPage userManagementPage;

  @Before(value = "@account", order = 1)
  public void setTheStage() {
    OnStage.setTheStage(new OnlineCast());
  }

  @Before(value = "@account", order = 2)
  public void OpenApplication() {
    theActorCalled("james").attemptsTo(Open.browserOn().the(loginPage));
    theActorInTheSpotlight().attemptsTo(EnterLoginDetails.forBotAdminUser("admin", "admin"));
  }

  @Given("^(.*) is at the user management")
  public void jamesIsAtTheUserManagement(String actor) {
    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(HomePage.ACCOUNT_MENU_DROPDOWN),
                WebElementStateMatchers.isVisible()));

    theActorInTheSpotlight().attemptsTo(Access.accountMenu());
    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(HomePage.USER_MANAGEMENT_OPTION),
                WebElementStateMatchers.isVisible()));
    theActorInTheSpotlight().attemptsTo(Access.userManagement());
  }

  @When(
      "^(.*) create a new user account with the required fields login = \"(.*)\", first name = \"(.*)\", last name = \"(.*)\", email = \"(.*)\", 2FA = \"(.*)\" and role = \"(.*)\"")
  public void jamesCreateANewUserAccountWithTheRequiredFieldsLoginFirstNameLastNameEmailAndRole(
      String actor,
      String login,
      String firstName,
      String lastName,
      String email,
      String twoFA,
      String role) {
    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(UserManagementPage.CREATE_NEW_USER),
                WebElementStateMatchers.isVisible()));
    theActorInTheSpotlight().attemptsTo(CreateAccount.forNewUser());
    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(UserAccountForm.LOGIN_FIELD),
                WebElementStateMatchers.isVisible()));
    if (Boolean.parseBoolean(twoFA) == true) {
      theActorInTheSpotlight()
          .attemptsTo(
              EnterAccountDetails.withUserInfo2FA(login, firstName, lastName, email, twoFA, role));
    } else {
      theActorInTheSpotlight()
          .attemptsTo(EnterAccountDetails.withUserInfo(login, firstName, lastName, email, role));
    }
    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(UserAccountForm.SAVE_BUTTON),
                WebElementStateMatchers.isEnabled()));
    theActorInTheSpotlight().attemptsTo(EnterAccountDetails.save());
  }

  @Then("the user account with login \"(.*)\" and email \"(.*)\" is created")
  public void theUserAccountWithLoginAndEmailIsCreated(String login, String email) {
    theActorInTheSpotlight().should(seeThat(TheWebPage.title(), equalTo("Users")));

    // Cleanup
    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(HomePage.ACTION_DROPDOWN(login)),
                WebElementStateMatchers.isVisible()));
    theActorInTheSpotlight().attemptsTo(DeleteAccount.forUser(login));
  }

  @When("^(.*) didn't fill up the required fields except login \"(.*)\" and last name \"(.*)\"")
  public void jamesDidnTFillUpTheRequiredFieldsExceptLoginAndLastName(
      String actor, String login, String lastName) {
    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(UserManagementPage.CREATE_NEW_USER),
                WebElementStateMatchers.isVisible()));
    theActorInTheSpotlight().attemptsTo(CreateAccount.forNewUser());
    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(UserAccountForm.LOGIN_FIELD),
                WebElementStateMatchers.isVisible()));
    theActorInTheSpotlight()
        .attemptsTo(EnterAccountDetails.withUserInfo(login, "", lastName, "", "ROLE_USER"));
  }

  @Then("^(.*) is unable to save the user account$")
  public void jamesIsUnableToSaveTheUserAccount(String actor) {
    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(UserAccountForm.SAVE_BUTTON),
                WebElementStateMatchers.isNotEnabled()));
  }

  @When("^(.*) with login \"(.*)\" want to edit the last name field with \"(.*)\"")
  public void jamesWithLoginWantToEditTheLastNameFieldWith(
      String actor, String login, String value) {

    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(HomePage.ACTION_DROPDOWN(login)),
                WebElementStateMatchers.isVisible()));

    theActorInTheSpotlight().attemptsTo(EditAccount.forUser(login, value));

    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(UserAccountForm.LASTNAME_FIELD),
                WebElementStateMatchers.isVisible()));
    theActorInTheSpotlight().attemptsTo(EditAccount.editlastField(login, value));
    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(UserAccountForm.SAVE_BUTTON),
                WebElementStateMatchers.isEnabled()));
    theActorInTheSpotlight().attemptsTo(EnterAccountDetails.save());
  }

  @Then("^(.*) with login \"(.*)\" is able to edit and update the field$")
  public void jamesWithLoginIsAbleToEditAndUpdateTheField(String actor, String login) {
    theActorInTheSpotlight().should(eventually(seeThat(TheWebPage.title(), equalTo("Users"))));

    // Cleanup
    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(HomePage.ACTION_DROPDOWN(login)),
                WebElementStateMatchers.isVisible()));
    theActorInTheSpotlight().attemptsTo(DeleteAccount.forUser(login));
  }
}
