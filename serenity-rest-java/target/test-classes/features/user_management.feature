Feature: user management
    In order to manage user accounts
    As a super admin or admin
    I want to perform task pertaining to user account

  Background:
    Given James is at the user management

  @smoke @account
  Scenario Outline: Create new user account profile
    In order to create new user account
    As a super admin James
    James wants to create a new user account profile
    When James create a new user account with the required fields login = "<login>", first name = "<first_name>", last name = "<last_name>", email = "<email>" and role = "<role>"
    Then the user account with login "<login>" and email "<email>" is created

    Examples:
      | login  | first_name | last_name |  email           | role       |
      | john   | john       | tan       | john@demo.com  | ROLE_ADMIN |
      | mary   | mary       | lee       | mary@demo.com  | ROLE_USER  |

  @smoke @account
  Scenario Outline: Account activation email
  In order to activate new user account
  As a super admin James
  James wants to able to sent email to account owner to activate their account
  When James create a new user account with the required fields login = "<login>", first name = "<first_name>", last name = "<last_name>", email = "<email>" and role = "<role>"
  Then the user account with login "<login>" and email "<email>" is created
  When the user activate the account by entering new password "<new_password>"
  Then the account is activated
  When the account user sign with the newly activated account
  Then the account user is able to sign in

    Examples:
      | login | first_name | last_name | email                        | role      | new_password   |
      | john  |  john      | tan       | john_demo_test@yopmail.com | ROLE_USER | demo12345678 |




