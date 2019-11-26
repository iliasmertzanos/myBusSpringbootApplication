# Practice application to exercise on spring boot 

## Few tech words

The project is based on :

* Java 1.8
* Spring Boot
* Database H2 (In-Memory)
* Maven
* Swagger

---

## Features

* Main idea of the project is that this service package provides API to manipulate bus and driver entities
* On the controller layer you can find many features to maintain the driver and bus entities, like create, delete, update and read.
* There are two special features among the usual oner (create, read, update and delete): 
* First the API provides the opportunity to allocate a bus to a driver and vice versa (deallocate).
* Second it provides the ability to filter drivers according to their attributes as well as the attributes of the cars they have selected. In this way the API gives back a list of drivers that matches the criteria that were being given.
* At this point two different approaches were developed in order to achieve a totally flexible filtering of the drivers from the data base.
* First approach was that the controller gets all the drivers from the data base and then filters the list using predicates. For every attribute there is small POJO class that is automatically called and it provides an appropriate predicate. See method in the `DriverController public List<DriverWithBusDTO> filterDrivers(@RequestBody  GroupedCriteria myGroupCriteria)`.
* Previews solution was nice to implement but what about scalability??? Therefore I came up with a better one using Specifications and reducing the amount of code that I really needed to 1/3 from what I was using before. See method in the `DriverController public List<DriverWithBusDTO> filterDriversUsingCriteria(@RequestBody  CriteriaGroup myGroupCriteria)`.
* Both methods accept the attributes as they are defined in ENUMs in the package `com.myBusApp.domainvalue`
* The API is tested on all layers using JUnit and the test classes are to be found in the package `src\test\java\com\myBus`
* You should be able to start the example application by executing `com.myBusApp.MyBusApplication`, which starts a webserver on port 8080 (http://localhost:8080) and serves SwaggerUI where can inspect and try existing endpoints (over http://localhost:8080/).

