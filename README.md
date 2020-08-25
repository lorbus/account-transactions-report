# Account Transactions Report RESTful API Description

This is a simple project that returns the list of money account transactions created in the current calendar month for 
a given customer, and the total amount in GBP
A debit transaction will only succeed if there are sufficient funds on the account (balance - debit amount >= 0)


## Running

1.  Java version 8
2.  Maven 3 to build the application
3.  From root folder run cmd :
    ```
    mvn clean install
    ```
4.  To start PostgreSQL server image with Docker run cmd :
    ```
    docker-compose up --build
    ```
    To stop the images type the following two buttons from the keyboard :
    ```
    Ctrl + c
    ```
    To remove the images run cmd :
    ```
    docker-compose down
    ``` 
5.  Start application cmd :
    ``` 
    mvn spring-boot:run
    ``` 
    or run the jar file manually cmd :
    ```
    java -jar target/account-transactions-report-0.0.1-SNAPSHOT.jar
    ```
8. To verify that application has started correctly go to :
    ``` 
    http://localhost:4444/api/test
    ``` 
    The following message should appear in the browser :
    ```
    Hello from account transactions report!
    ```

## API Endpoints

HTTP GET :
1.  Return all accounts
    ```
    http://localhost:4444/api/accounts
    ```

2.  Return account by id
    ```
    http://localhost:4444/api/account/{id}
    ```

3.  Return list of accounts by user id
    ```
    http://localhost:4444/api/accounts/user?userId={user}
    ```

4.  Return list of transactions by account id    
    ```
    http://localhost:4444/api/accounts/{id}/transactions
    ```

HTTP POST :
1.  Creates new account
    ```
    http://localhost:4444/api/account
    ```

2.  Creates transaction   
    ```
    http://localhost:4444/api/transaction
    ```


## Used technology

1.  Spring Boot and Spring Data JPA
2   Swagger REST-ful API Documentation Specification 
    ```
    http://localhost:4444/api/swagger-ui.html
    ```
3   SLF4J logging facade for Java
4.  Undertow as web server to run the application
5.  Google Gson (a Java serialization/deserialization library)
6.  PostgreSQL DB
7.  Dockers (for PostgreSQL image)
8.  Flyway tool for db migration


## Not implemented

1.  Security 
2.  Authentication (JWT)
3.  Authorization