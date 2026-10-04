# sample-fake-java

A small, intentionally fictitious classic Java EE (Servlet + JSP + raw JDBC) web application used as a
test fixture for Genie's "Framework upgrade" modernization capability - specifically, upgrading from
plain Servlets/JSP to Spring Boot.

## Domain

A simple library management system: Books, Members, and Loans.

## Layout

- `pom.xml` - Maven WAR project; depends on `javax.servlet-api` (not Spring/Spring Boot), JSTL, and H2
- `src/main/java/com/example/library/model/` - `Book`, `Member`, `Loan` POJOs
- `src/main/java/com/example/library/dao/` - plain JDBC data-access objects (`BookDao`, `MemberDao`,
  `LoanDao`) and a `DatabaseConnectionManager` - a Spring Boot migration would typically replace these
  with Spring Data JPA repositories and a configured `DataSource` bean
- `src/main/java/com/example/library/servlet/` - classic `HttpServlet` controllers (`BookServlet`,
  `MemberServlet`, `LoanServlet`) and an `AppInitializer` `ServletContextListener` that creates the
  schema and seeds demo data on startup - a Spring Boot migration would typically replace the servlets
  with Controller/RestController classes and the listener with `schema.sql`/`data.sql` or a
  `CommandLineRunner`
- `src/main/webapp/WEB-INF/web.xml` - classic deployment descriptor registering every servlet and the
  listener by hand - Spring Boot eliminates the need for this file entirely via auto-configuration
- `src/main/webapp/WEB-INF/views/*.jsp` - JSP views rendered via `RequestDispatcher.forward()` - a
  Spring Boot migration would typically replace these with Thymeleaf templates or a REST API
- `src/test/java/com/example/library/dao/BookDaoTest.java` - JUnit 4 tests over the JDBC DAO layer (4
  tests, all passing)

This repository intentionally has no Spring/Spring Boot dependency anywhere, so a "Framework upgrade"
plan targeting Spring Boot has real, evidenced migration work to do: replacing `web.xml` servlet
registration with Spring MVC controllers, replacing raw JDBC DAOs with Spring Data JPA repositories,
and replacing JSP views with Thymeleaf (or a REST API), while preserving the existing three resources
and their behavior.

Verified locally before pushing: `mvn test` (4/4 JUnit tests pass) and `mvn package` (WAR builds
successfully) both succeed with JDK 17 / Maven 3.9.16.
