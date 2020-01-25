package demo;

import demo.browserstack.BrowserStackSerenityTest;
import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    plugin = {"pretty"},
    features = "src/test/resources/features",
    snippets = SnippetType.CAMELCASE)
public class CucumberTestSuite extends BrowserStackSerenityTest {}
