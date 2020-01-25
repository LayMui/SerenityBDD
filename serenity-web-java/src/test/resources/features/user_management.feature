Feature: user management
  In order to manage user accounts
  As a super admin or admin
  I want to perform task pertaining to user account

  Background:
    Given James is at the user management

  @account
  Scenario Outline: Create new user account profile
  In order to create new user account
  As a super admin James
  James wants to create a new user account profile
    When James create a new user account with the required fields login = "<login>", first name = "<first_name>", last name = "<last_name>", email = "<email>", 2FA = "<2FA>" and role = "<role>"
    Then the user account with login "<login>" and email "<email>" is created

    Examples:
      | login | first_name | last_name | email           | role       | 2FA  |
      | john  | john       | tan       | john@demo.com | ROLE_ADMIN | true |

  @account
  Scenario Outline: Edit user account
  In order to edit the information such as login, firstname, lastname and email and the profle of the existing user account
  As a super admin James
  James can make changes to the editable information
    When James create a new user account with the required fields login = "<login>", first name = "<first_name>", last name = "<last_name>", email = "<email>", 2FA = "<2FA>" and role = "<role>"
    And James with login "<login>" want to edit the last name field with "<value>"
    Then James with login "<login>" is able to edit and update the field

    Examples:
      | login | first_name | last_name | email           | role       | 2FA  | value |
      | john  | john       | tan       | john@demo.com | ROLE_ADMIN | true | lee   |

  @account
  Scenario Outline: User account mandatory fields are not filled in
  In order to prevent super admin from saving incomplete new user account information
  As a super admin James
  James is not allowed to save the user account
    When James didn't fill up the required fields except login "<login>" and last name "<last_name>"
    Then James is unable to save the user account

    Examples:
      | login | last_name |
      | john  | tan       |

