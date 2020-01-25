Feature: signin
  In order to perform authentication to access the demo system
  As a user based on the role given
  I want to sign in

  Background:
    Given James is at the demo admin url page

 @smoke @signin
  Scenario Outline: Sign in with valid credentials (<hiptest-uid>)
  In order to login to the demo system
  As a user based on the role given
  James wants to login with valid credentials
    When James sign in with username = "<username>" and password = "<password>"
    Then James is able to login

    Examples:
      | username | password | hiptest-uid                              |
      | admin    | admin    | uid:2d713c9f-4fca-47ff-825d-6f5f58ff7dc4 |

  @smoke @signin
  Scenario Outline: Cannot sign in with invalid credentials (<hiptest-uid>)
  In order to validate login to the demo system
  As a bot admin user James
  James is not allowed to login with invalid credentials
    When James sign in with username = "<username>" and password = "<password>"
    Then James is unable to login

    Examples:
      | username | password | hiptest-uid                              |
      | admin    | admin123 | uid:2d713c9f-4fca-47ff-825d-6f5f58ff7dc4 |

