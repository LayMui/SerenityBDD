package demo.stepdefinitions;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.thucydides.core.util.EnvironmentVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SigninStepDefinitions {

  private EnvironmentVariables environmentVariables;

  private final Logger log = LoggerFactory.getLogger(SigninStepDefinitions.class);
  Actor james;

  @Given("James is at the demo admin url page")
  public void jamesIsAtThedemoAdminUrlPage() {

    String theRestApiBaseUrl =
        environmentVariables
            .optionalProperty("restapi.baseurl")
            .orElse("https://idemo-dev.demo.io/idemo-admin");

    james = Actor.named("James the super admin").whoCan(CallAnApi.at(theRestApiBaseUrl));
  }

  @When("James sign in with username = \"(.*)\" and password = \"(.*)\"")
  public void jamesSignInWithUsernameAndPassword(String username, String password) {
    String credentials =
        String.format("{\"username\":  \"%s\", \"password\": \"%s\"}", username, password);
    log.info("CREDENTIALS:" + credentials);
    james.attemptsTo(
        Post.to("/api/authenticate")
            .with(request -> request.header("Content-Type", "application/json").body(credentials)));
  }

  @Then("James is able to login")
  public void jamesIsAbleToLogin() {
    String idToken = SerenityRest.then().extract().body().jsonPath().get("id_token");
    log.info("ID_TOKEN:" + idToken);
    james.should(
        seeThatResponse(
            "the expected id token should be returned", response -> response.statusCode(200)));

    Ensure.that("Token is returned", response -> idToken.matches("(.*)"));
  }
}
