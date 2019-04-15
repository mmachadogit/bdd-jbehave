
### Running the service

1- Build app-service
````
git clone https://github.com/fpsoriano/bdd-jbehave.git
cd app-service
mvn clean install
````

2- Import the project in your IDEA and run the application AppServiceApplication.java

3- You can access the project documentation by http://localhost:8080/swagger-ui.html#/





### Running the BDD
1- Build app-integration-test
````
cd app-integration-test
mvn clean install
````

2- Import the project (app-integration-test) and create a new configuration selecting the JUnit
````
name: JBehaveRunner
class: runner.JBehaveRunner
VM options: -ea -DmetaFilters=+app
````

3- Run the project according the configuration above

