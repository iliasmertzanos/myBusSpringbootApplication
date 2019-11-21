# FREE NOW backend applicant test

## Task Description
You should be able to start the example application by executing com.freenow.FreeNowServerApplicantTestApplication, which starts a webserver on port 8080 (http://localhost:8080) and serves SwaggerUI where can inspect and try existing endpoints.

The project is based on a small web service which uses the following technologies:

* Java 1.8
* Spring Boot
* Database H2 (In-Memory)
* Maven


You should be aware of the following conventions while you are working on this exercise:

 * All new entities should have an ID with type of Long and a date_created with type of ZonedDateTime.
 * The architecture of the web service is built with the following components:
 	* DataTransferObjects: Objects which are used for outside communication via the API
    * Controller: Implements the processing logic of the web service, parsing of parameters and validation of in- and outputs.
    * Service: Implements the business logic and handles the access to the DataAccessObjects.
    * DataAccessObjects: Interface for the database. Inserts, updates, deletes and reads objects from the database.
    * DomainObjects: Functional Objects which might be persisted in the database.
 * TestDrivenDevelopment is a good choice, but it's up to you how you are testing your code.
 * Feel free to use Java as well as Kotlin
 * We do provide code formatter for IntelliJ IDEA and Eclipse in the etc folder

You should commit into a local git repository and include the git repository (.git/) in the upload.

_NOTE: Please DO NOT publish the project, e.g. by uploading it to GitHub or the like!_

---


## Task 1
 * Write a new Controller for maintaining bus (CRUD).
   * Decide on your own how the methods should look like.
   * Entity Bus: Should have at least the following characteristics: license_plate, seat_count, convertible, rating, engine_type (electric, gas, ...)
   * Entity Manufacturer: Decide on your own if you will use a new table or just a string column in the bus table.
 * Extend the DriverController to enable drivers to select a bus they are driving with.
 * Extend the DriverController to enable drivers to deselect a bus.
 * Extend the DriverDo to map the selected bus to the driver.
 * Add example data to resources/data.sql

---


## Task 2
First come first serve: A bus can be selected by exactly one ONLINE Driver. If a second driver tries to select a already used bus you should throw a BusAlreadyInUseException.

---


## Task 3
Imagine a driver management frontend that is used internally by FREE NOW employees to create and edit driver related data. For a new search functionality, we need an endpoint to search for drivers. It should be possible to search for drivers by their attributes (username, online_status) as well as bus characteristics (license plate, rating, etc).

* implement a new endpoint for searching or extend an existing one
* driver/bus attributes as input parameters
* return list of drivers

---


## Task 4 (optional)
This task is _voluntarily_, if you can't get enough of hacking tech challenges, implement security.
Secure the API so that authentication is needed to access it. The details are up to you.

Please include instructions how to authenticate/login, so that we can test the endpoints you implemented!

---

Good luck!
❤️ FREE NOW



_NOTE: Please make sure to not submit any personal data with your tests result. Personal data is for example your name, your birth date, email address etc._