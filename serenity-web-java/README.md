# Getting started instructions
### The project directory structure
The project has build scripts for Maven and follows the standard directory structure used in most Serenity projects:
```Gherkin
src
  + main
  + test
    + java                        Test runners and supporting code
    + resources
      + features                  Feature files
             signin.feature 
            user_management.feature
       + webdriver                 Bundled webdriver binaries
         + linux
         + mac
         + windows 
           chromedriver.exe       OS-specific Webdriver binaries 
           geckodriver.exe
```
The Screenplay pattern describes tests in terms of actors and the tasks they perform. Tasks are represented as objects performed by an actor, rather than methods. This makes them more flexible and composable, at the cost of being a bit more wordy
```java
    @Before
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
    }

    @Before(value="@signin")
       public void setTheStage() {
           OnStage.setTheStage(new OnlineCast());
       }
   
       @Given("^(.*) is at the demo admin url page")
       public void jamesIsAtThedemoAdminUrlPage(String actor) {
           theActorCalled(actor).attemptsTo(Open.browserOn().the(loginPage));
       }
   
       @When("^(.*) sign in with username = \"(.*)\" and password = \"(.*)\"")
       public void jamesSignInWithUsernameAndPassword(String actor, String username, String password) {
           theActorInTheSpotlight().attemptsTo(EnterLoginDetails.forBotAdminUser(username, password));
       }
   
       @Then("^(.*) is able to login")
       public void jamesIsAbleToLogin(String actor) {
           theActorInTheSpotlight().should(
                   seeThat(TheWebPage.title(),
                           equalTo("Intents")));
       }
```

In both approaches, the Page Objects very close or identical. The differences are mainly in the action classes. Screenplay classes emphasise reusable components and a very readable declarative style, whereas Lean Page Objects and Action Classes opt for a more imperative style.

The `EnterLoginDetails` class performs the same role as it’s equivalent in the Lean Page Object/Action Class version, and looks quite similar:
```java
public class EnterLoginDetails {
    public static Performable forBotAdminUser(String username, String password) {
        return Task.where("{0} attempts to enter #username and #password",
                Clear.field(LoginPage.USERNAME_FIELD),
                Enter.theValue(username).into(LoginPage.USERNAME_FIELD),
                Clear.field(LoginPage.PASSWORD_FIELD),
                Enter.theValue(password).into(LoginPage.PASSWORD_FIELD),
                Click.on(LoginPage.SIGNIN_BUTTON)
        ).with("username").of(username).with("password").of(password);
    }
}
```

## Executing the tests
Please ensure you have download the webdriver such as chromedriver, firefox into the path
See the serenity.conf 
```
drivers {
  windows {
    webdriver.chrome.driver = "src/test/resources/webdriver/windows/chromedriver.exe"
    webdriver.gecko.driver = "src/test/resources/webdriver/windows/geckodriver.exe"
    webdriver.ie.driver = "src/test/resources/webdriver/windows/IEDriverServer.exe"
  }
  mac {
    webdriver.chrome.driver = "/usr/local/bin/chromedriver"
    webdriver.gecko.driver = "/usr/local/bin/geckodriver"
  }
  linux {
    webdriver.chrome.driver = "src/test/resources/webdriver/linux/chromedriver"
    webdriver.gecko.driver = "src/test/resources/webdriver/linux/geckodriver"
  }
}
```
By default, the tests will run remotely on browserstack. You can run them it in local browser chrome by overriding the `driver` system property, e.g.
```json
$ mvn clean verify -Dwebdriver.driver=chrome 
```

### Webdriver configuration
The WebDriver configuration is managed entirely from this file, as illustrated below:
```java
webdriver {
    driver = chrome
}
headless.mode = true

chrome.switches="""--start-maximized;--test-type;--no-sandbox;--ignore-certificate-errors;
                   --disable-popup-blocking;--disable-default-apps;--disable-extensions-file-access-check;
                   --incognito;--disable-infobars,--disable-gpu"""

```

The project also bundles some of the WebDriver binaries that you need to run Selenium tests in the `src/test/resources/webdriver` directories. These binaries are configured in the `drivers` section of the `serenity.conf` config file:
```json
drivers {
  windows {
    webdriver.chrome.driver = "src/test/resources/webdriver/windows/chromedriver.exe"
    webdriver.gecko.driver = "src/test/resources/webdriver/windows/geckodriver.exe"
  }
  mac {
    webdriver.chrome.driver = "src/test/resources/webdriver/mac/chromedriver"
    webdriver.gecko.driver = "src/test/resources/webdriver/mac/geckodriver"
  }
  linux {
    webdriver.chrome.driver = "src/test/resources/webdriver/linux/chromedriver"
    webdriver.gecko.driver = "src/test/resources/webdriver/linux/geckodriver"
  }
}
```
This configuration means that development machines and build servers do not need to have a particular version of the WebDriver drivers installed for the tests to run correctly.

### Environment-specific configurations
We can also configure environment-specific properties and options, so that the tests can be run in different environments. Here, we configure three environments, __dev__, _staging_ and _prod_, with different starting URLs for each:
```json
environments {
  default {
    webdriver.base.url = "https://idemo-dev.demo.io/idemo-admin/#/"
  }
  dev {
    webdriver.base.url = "https://localhost:8080/idemo-admin/#/"
  }
  staging {
    webdriver.base.url = "https://idemo-qa.demo.io/idemo-admin/#/"
  }
  prod {
    webdriver.base.url = "https://idemo-dev.demo.io/idemo-admin/#/"
  }
}

```
  
You use the `environment` system property to determine which environment to run against. 
For example to run the tests in the staging environment, you could run:
```json
$ mvn clean verify -Denvironment=staging
```

For example to run the tests with specific tags, you could run:
```json
$ mvn clean verify -Dcucumber.options="--tags @smoke"
```
For example to run the tests in the staging environment and with specific tags, you could run:
```json
$ mvn clean verify -Denvironment=staging -Dcucumber.options="--tags @smoke"
```

For example to run the tests locally using specific browser say chrome
```json
$ mvn verify -Dwebdriver.driver=chrome -Dcucumber.options="--tags @smoke"
```
For example to run the tests on browserstack the configuration and capabilities
Change the serenity.conf

For example to run the tests on browserstack with different build say 2.5
```json
$ mvn clean verify -Dcucumber.options="--tags @smoke" -Dbstack_build=2.5
```

To force updating of snapshots and re-download all the dependencies
$ mvn clean install -U -Dmaven.test.failure.ignore=true 

The test results will be recorded in the `target/site/serenity` directory.
