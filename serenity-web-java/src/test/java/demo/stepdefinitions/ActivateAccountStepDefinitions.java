package demo.stepdefinitions;

import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

import converse.tasks.EnterConfirmedPassword;
import converse.ui.ActivateAccountPage;
import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.questions.WebElementQuestion;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.thucydides.core.util.EnvironmentVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivateAccountStepDefinitions {

  Actor james;
  private String idToken;
  private String userId;
  private String login;
  private String resetKey;
  private String theRestApiBaseUrl;
  private String superAdminUsername;
  private String superAdminPassword;
  private final Logger log = LoggerFactory.getLogger(ActivateAccountStepDefinitions.class);

  EnvironmentVariables environmentVariables;
  ActivateAccountPage activateAccountPage;

  @Before(value = "@activate_account", order = 1)
  public void setUp() {
    superAdminUsername =
        environmentVariables.optionalProperty("superadmin.username").orElse("admin");
    superAdminPassword =
        environmentVariables.optionalProperty("superadmin.password").orElse("admin");
    theRestApiBaseUrl =
        environmentVariables
            .optionalProperty("restapi.baseurl")
            .orElse("https://demo.io/demo-admin");

    log.info("theRestApiBaseUrl: " + theRestApiBaseUrl);

    String credentials =
        String.format(
            "{\"username\":  \"%s\", \"password\": \"%s\"}",
            superAdminUsername, superAdminPassword);
    log.info("CREDENTIALS:" + credentials);
    james = Actor.named("James the super admin").whoCan(CallAnApi.at(theRestApiBaseUrl));
    james.attemptsTo(
        Post.to("/api/authenticate")
            .with(request -> request.header("Content-Type", "application/json").body(credentials)));

    idToken = SerenityRest.lastResponse().jsonPath().getString("id_token");
    log.info("ID TOKEN: " + idToken);
  }

  @Before(value = "@activate_account", order = 2)
  public void setTheStage() {
    OnStage.setTheStage(new OnlineCast());
  }

  @Given("^the super admin James create a new user account for John$")
  public void theSuperAdminJamesCreateANewUserAccountForJohn(DataTable dt) {
    List<List<String>> list = dt.asLists(String.class);

    login = list.get(1).get(0);
    String firstName = list.get(1).get(1);
    String lastName = list.get(1).get(2);
    String email = list.get(1).get(3);
    String role = list.get(1).get(4);
    String userAccount =
        String.format(
            "{\"id\": null, \"login\": \"%s\", \"firstName\": \"%s\", \"lastName\": \"%s\", \"email\": \"%s\", \"activated\": true, \"langKey\": \"en\", \"createdBy\": null, \"createdDate\": null, \"lastModifiedBy\": null, \"lastModifiedDate\": null, \"resetDate\": null, \"authorities\": [\"%s\"]}",
            login, firstName, lastName, email, role);

    log.info("TOKEN:" + idToken);
    log.info("USER ACCOUNT:" + userAccount);

    james.attemptsTo(
        Post.to("/api/users")
            .with(
                request ->
                    request
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + idToken)
                        .body(userAccount)));
    userId = SerenityRest.lastResponse().jsonPath().getString("id");
    resetKey = SerenityRest.lastResponse().jsonPath().getString("resetKey");

    log.info("ID:" + userId);
    log.info("RESET KEY:" + resetKey);
  }

  @When("^the user didn't enter a matching password for \"([^\"]*)\"$")
  public void theUserDidnTEnterAMatchingPasswordFor(String newPassword) {
    String activateAccountUrl =
        theRestApiBaseUrl + "/#/reset/finish" + "?key=" + resetKey + "&new=true&twoFactor=false";
    theActorCalled("james").attemptsTo(Open.url(activateAccountUrl));
    theActorInTheSpotlight().attemptsTo(EnterConfirmedPassword.doesNotMatch(newPassword, "123"));
  }

  @Then("^the account user is unable to sign in$")
  public void theAccountUserIsUnableToSignIn() {
    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(activateAccountPage.MISMATCH_ERROR),
                WebElementStateMatchers.isVisible()));
  }

  @When("^the user enter a matching password for \"([^\"]*)\"$")
  public void theUserEnterAMatchingPasswordFor(String newPassword) {
    String activateAccountUrl =
        theRestApiBaseUrl + "/#/reset/finish" + "?key=" + resetKey + "&new=true&twoFactor=false";
    theActorCalled("james").attemptsTo(Open.url(activateAccountUrl));
    theActorInTheSpotlight()
        .attemptsTo(EnterConfirmedPassword.doesNotMatch(newPassword, newPassword));
  }

  @Then("^the account user is able to sign in$")
  public void theAccountUserIsAbleToSignIn() {
    theActorInTheSpotlight()
        .should(
            seeThat(
                WebElementQuestion.the(activateAccountPage.MISMATCH_ERROR),
                WebElementStateMatchers.isNotVisible()));
  }

  @After("@activate_account")
  public void tearDown() {
    Response response =
        given()
            .log()
            .all()
            .and()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + idToken)
            .baseUri(theRestApiBaseUrl)
            .basePath("/api/users/")
            .when()
            .delete(userId);
  }
}
