Feature: signin
    In order to perform authentication to access the demo system
    As a user based on the role given
    I want to sign in

  Background:
    Given James is at the demo admin url page

    @smoke
  Scenario Outline: Sign in with valid credentials
     In order to login to the demo system
     As a user based on the role given
     James wants to login with valid credentials
    When James sign in with username = "<username>" and password = "<password>"
    Then James is able to login

    Examples:
      | username | password |
      | admin    | admin    |

    

  