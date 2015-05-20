Feature: Redis operations

  #######
    # REDIS
    #######
  Scenario: Redis Steps for Key/Value
    Given I have the redis key "key1" with value "value1"
    Given I have the redis key "key2" with value in file "responses/value2.text"
    Given I have the redis key "key3" with value:
  """
  value3

  """
    Given I have the redis key "key4" with value "value4" with ttl 5 seconds
    Then the redis key "key4" should be "value4"
    Then the redis key "key3" should exists
    Then the redis keys "key1,key2,key3" should exists
    Then the redis keys should exists:
      | key1 |
      | key2 |
      | key3 |
    Then the redis key "key4" should not exists after 6 seconds
    Then the redis key "key4" should not exists
    Then the redis keys "key100,key200,key300" should not exists
    Then the redis key "key1" should be "value1"
    Then the redis key "key2" should be:
  """
  value2

  """
    Then the redis key "key3" should be file "responses/value3.text"

  Scenario: Redis Steps for Key/Value With DB
    Given I have the redis key "key1" in the db 1 with value "value1"
    Given I have the redis key "key2" in the db 1 with value in file "responses/value2.text"
    Given I have the redis key "key3" in the db 3 with value:
  """
  value3

  """
    Given I have the redis key "key4" in the db 4 with value "value4" with ttl 5 seconds
    Then the redis key "key4" in the db 4 should be "value4"
    Then the redis key "key3" in the db 3 should exists
    Then the redis keys "key1,key2" in the db 1 should exists
    Then the redis keys in the db 1 should exists:
      | key1 |
      | key2 |
    Then the redis key "key4" in the db 4 should not exists after 6 seconds
    Then the redis key "key4" in the db 4 should not exists
    Then the redis keys "key100,key200,key300" in the db 1 should not exists
    Then the redis key "key1" in the db 1 should be "value1"
    Then the redis key "key2" in the db 1 should be:
  """
  value2

  """
    Then the redis key "key3" in the db 3 should be file "responses/value3.text"

  Scenario: Redis Steps for Lists
    Given I have the redis list "list1" with values "value1"
    Given I have the redis list "list2" with values in file "responses/list2.text"
    Given I have the redis list "list3" with values:
      | value3   |
      | value33  |
      | value333 |
    Given I have the redis list "list4" with values "value4" with ttl 5 seconds
    Given I have the redis list "list5" with values "value5,value55,value555" with ttl 5 seconds
    Then the redis list "list4" should be "value4"
    Then the redis list "list5" should be "value5,value55,value555"
    Given I have the redis list "list6" with values "value6,value66,value666"
    Then the redis list "list6" should be:
      | value6   |
      | value66  |
      | value666 |
    Then the redis list "list3" should exists
    Then the redis lists "list1,list2,list3" should exists
    Then the redis lists should exists:
      | list1 |
      | list2 |
      | list3 |
    Then the redis list "list5" should not exists after 6 seconds
    Then the redis list "list4" should not exists
    Then the redis lists "list100,list200,list300" should not exists
    Given I have the redis list "list7" with values "value7,value77,value777"
    Then the redis list "list7" should be file "responses/list7.text"

  Scenario: Redis Steps for Lists
    Given I have the redis list "list1" in the db 1 with values "value1"
    Given I have the redis list "list1.2" in the db 1 with values "value1.2"
    Given I have the redis list "list2" in the db 2 with values in file "responses/list2.text"
    Given I have the redis list "list3" in the db 3 with values:
      | value3   |
      | value33  |
      | value333 |
    Given I have the redis list "list4" in the db 4 with values "value4" with ttl 5 seconds
    Given I have the redis list "list5" in the db 5 with values "value5,value55,value555" with ttl 5 seconds
    Then the redis list "list4" in the db 4 should be "value4"
    Then the redis list "list5" in the db 5 should be "value5,value55,value555"
    Given I have the redis list "list6" in the db 6 with values "value6,value66,value666"
    Then the redis list "list6" in the db 6 should be:
      | value6   |
      | value66  |
      | value666 |
    Then the redis list "list3" in the db 3 should exists
    Then the redis lists "list1,list1.2" in the db 1 should exists
    Then the redis lists in the db 1 should exists:
      | list1   |
      | list1.2 |
    Then the redis list "list5" in the db 5 should not exists after 6 seconds
    Then the redis list "list4" in the db 4 should not exists
    Then the redis lists "list100,list200,list300" in the db 1 should not exists
    Given I have the redis list "list7" in the db 7 with values "value7,value77,value777"
    Then the redis list "list7" in the db 7 should be file "responses/list7.text"

  Scenario: Redis Steps for Cleaning One Database
    Given I have the redis key "key" in the db 8 with value "value"
    Then I have the redis key "key" in the db 8 with value "value"
    Given I have cleaned redis db 8
    Then the redis key "key" in the db 8 should not exists

  Scenario: Redis Steps for Cleaning All Databases
    Given I have the redis key "key" in the db 0 with value "value"
    And I have the redis key "key" in the db 3 with value "value"
    And I have the redis key "key" in the db 7 with value "value"
    Then I have the redis key "key" in the db 0 with value "value"
    And I have the redis key "key" in the db 3 with value "value"
    And I have the redis key "key" in the db 7 with value "value"
    Given I have cleaned redis
    Then the redis key "key" in the db 0 should not exists
    And the redis key "key" in the db 3 should not exists
    And the redis key "key" in the db 7 should not exists
