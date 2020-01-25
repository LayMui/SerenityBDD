package demo;

import static cucumber.api.SnippetType.CAMELCASE;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    plugin = {"pretty"},
    strict = true,
    snippets = CAMELCASE,
    features = "classpath:features")
public class CucumberTestSuite {

}
