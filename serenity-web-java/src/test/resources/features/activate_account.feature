Feature: activate account
    In order to activate account
    As a bot admin user based on the role given
    I want to activate the account by setting a password

  Background:
    Given the super admin James create a new user account for John
      | login | first_name | last_name | email                        | role       |
      | john  | john       | tan       | john@yopmail.com | ROLE_ADMIN |

  @smoke @activate_account
  Scenario Outline: Activate account with invalid input (<hiptest-uid>)
    In order to validate an activated new user account
    As a super admin James
    James wants to see the account unable to sign in with invalid password
    When the user didn't enter a matching password for "<new_password>"
    Then the account user is unable to sign in

    Examples:
      | new_password | hiptest-uid |
      | Demo12345678 |  |

  