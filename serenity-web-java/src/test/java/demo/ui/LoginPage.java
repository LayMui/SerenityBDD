package demo.ui;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;

@DefaultUrl("https://idemo-dev.demo.io/idemo-admin/#/")
public class LoginPage extends PageObject {

  public static final Target USERNAME_FIELD = Target.the("username").located(By.id("username"));
  public static final Target PASSWORD_FIELD = Target.the("password").located(By.id("password"));
  public static final Target SIGNIN_BUTTON =
      Target.the("signin").locatedBy("//button[@type='submit']");
  public static final Target ERROR =
      Target.the("error").locatedBy("//div[text()='Invalid username or password']");
}
