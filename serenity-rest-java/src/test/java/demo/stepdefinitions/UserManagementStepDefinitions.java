package demo.stepdefinitions;

import static io.restassured.RestAssured.given;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.equalTo;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.thucydides.core.util.EnvironmentVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserManagementStepDefinitions {

  Actor james;
  private String idToken;
  private String userId;
  private String resetKey;
  private String newAccountLogin;
  private String newAccountPassword;
  private String theRestApiBaseUrl;
  private String superAdminUsername;
  private String superAdminPassword;
  private final Logger log = LoggerFactory.getLogger(UserManagementStepDefinitions.class);

  EnvironmentVariables environmentVariables;

  @Before({"@account"})
  public void setUp() {
    superAdminUsername =
        environmentVariables.optionalProperty("superadmin.username").orElse("admin");
    superAdminPassword =
        environmentVariables.optionalProperty("superadmin.password").orElse("admin");
    theRestApiBaseUrl =
        environmentVariables
            .optionalProperty("restapi.baseurl")
            .orElse("https://idemo-dev.demo.io/idemo-admin");

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
    log.info("TOKEN ID: " + idToken);
  }

  @Given("James is at the user management")
  public void jamesIsAtTheUserManagement() {
    System.out.println("James has logged in and is at user management ready to create new user");
    log.info("James has logged in and is at user management ready to create new user");
  }

  @When(
      "James create a new user account with the required fields login = \"(.*)\", first name = \"(.*)\", last name = \"(.*)\", email = \"(.*)\" and role = \"(.*)\"")
  public void jamesCreateANewUserAccountWithTheRequiredFieldsLoginFirstNameLastNameEmailAndRole(
      String login, String firstName, String lastName, String email, String role) {
    String userAccount =
        String.format(
            "{\"id\": null, \"login\": \"%s\", \"firstName\": \"%s\", \"lastName\": \"%s\", \"email\": \"%s\", \"activated\": true, \"langKey\": \"en\", \"createdBy\": null, \"createdDate\": null, \"lastModifiedBy\": null, \"lastModifiedDate\": null, \"resetDate\": null, \"authorities\": [\"%s\"]}",
            login, firstName, lastName, email, role);

    log.info("USER ACCOUNT:" + userAccount);
    log.info("TOKEN:" + idToken);

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

  @Then("the user account with login \"(.*)\" and email \"(.*)\" is created")
  public void theUserAccountWithLoginAndEmailIsCreated(String login, String email) {

    james.should(
        seeThatResponse(
            "the expected user with login" + login + "should be returned",
            response ->
                response
                    .statusCode(201)
                    .body("activated", equalTo(true))
                    .body("using2FA", equalTo(false))
                    .body("login", equalTo(login))
                    .body("email", equalTo(email))));
    Ensure.that("ID is returned", response -> userId.matches("(.*)"));
    newAccountLogin = login;
  }

  @When("the user activate the account by entering new password \"(.*)\"")
  public void theUserWithLoginActivateTheAccountByEnteringNewPassword(String newPassword) {
    String resetPassword =
        String.format("{\"key\": \"%s\", \"newPassword\": \"%s\"}", resetKey, newPassword);
    log.info("Entering resetpassword" + resetPassword);

    james.attemptsTo(
        Post.to("/api/account/reset_password/finish")
            .with(
                request -> request.header("Content-Type", "application/json").body(resetPassword)));
    newAccountPassword = newPassword;
  }

  @Then("the account is activated")
  public void theAccountIsActivated() {
    james.should(
        seeThatResponse(
            "the expected status should be returned", response -> response.statusCode(200)));
    log.info("Account is activated");
  }

  @When("the account user sign with the newly activated account")
  public void theAccountUserSignWithTheNewlyActivatedAccount() {
    String newAccountCredentials =
        String.format(
            "{\"username\":  \"%s\", \"password\": \"%s\"}", newAccountLogin, newAccountPassword);
    System.out.println("NEW ACCOUNT CREDENTIALS:" + newAccountCredentials);
    james.attemptsTo(
        Post.to("/api/authenticate")
            .with(
                request ->
                    request
                        .header("Content-Type", "application/json")
                        .body(newAccountCredentials)));

    log.info(
        "NEW ACCOUNT TOKEN ID: " + SerenityRest.lastResponse().jsonPath().getString("id_token"));
  }

  @Then("the account user is able to sign in")
  public void theAccountUserIsAbleToSignIn() {
    String newAccountIdToken = SerenityRest.then().extract().body().jsonPath().get("id_token");
    log.info("NEW ACCOUNT ID_TOKEN:" + newAccountIdToken);
    james.should(
        seeThatResponse(
            "the expected id token should be returned", response -> response.statusCode(200)));

    Ensure.that("Token is returned", response -> newAccountIdToken.matches("(.*)"));
  }

  @After("@account")
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
