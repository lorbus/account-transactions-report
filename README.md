# Account Transactions Report RESTful API Description

This is a simple project that returns the list of money account transactions created in the current calendar month for 
a given customer, and the total amount in GBP; creates accounts, transactions and obtain them. 
Search for accounts and transactions. In order to create successfully a debit transaction will the funds sufficient 
should be sufficient in the account (total available balance - debit amount >= 0). Currencies for transactions should be
already created in the currency table, and currency of the account must be the same as the currency of the transaction.
REST APIs are protected with Spring Security and JWT
1. Get the JWT based token from the authentication endpoint.
2. Extract token from the authentication result.
3. Set the HTTP header `Authorization` value as `Bearer jwt_token`.
4. Then send a request to access the protected resources. 
5. If the requested resource is protected, Spring Security will use our custom `Filter` to validate the JWT token, and build an `Authentication` object and set it in Spring Security specific `SecurityContextHolder` to complete the authentication progress.
6. If the JWT token is valid it will return the requested resource to client.

## Running

1.  Java version 8
2.  Maven 3 to build the application
3.  From root folder run the following cmd, to delete all previously compiled Java sources/resources and download
    dependencies, compile, test, package the project and install/copy your built as .jar) :
    ```
    mvn clean install
    ```
4.  Builds all the images and starts the containers :
    ```
    docker-compose up --build
    ```
        To stop and remove containers and networks (images) :
        ```
            1. Ctrl + c
            2. docker-compose down
        ``` 
        
        Other useful docker cmd
        Remove all images :
        ```    
        docker rmi $(docker images -a -q)
        ```    
        Remove one or more specific containers :
        ```    
        docker rm 'ID_or_Name' 'ID_or_Name'
        ```
        Remove one or more specific volumes :
        ```    
        docker volume rm volume_name volume_name
        ```   
5.  Start application with cmd :
    ``` 
    mvn spring-boot:run
    ``` 
    or run the jar archive manually with cmd :
    ```
    java -jar target/account-transactions-report-0.0.1-SNAPSHOT.jar
    ```

## API Endpoints

### HTTP GET
1.  Return all accounts
    ```
    http://localhost:4444/api/v1/accounts
    ```

2.  Return account by id
    ```
    http://localhost:4444/api/v1/account/{id}
    ```

3.  Return list of accounts by user id
    ```
    http://localhost:4444/api/v1/accounts/user?userId={user}
    ```

4.  Return list of transactions by account id    
    ```
    http://localhost:4444/api/v1/accounts/{id}/transactions
    ```

### HTTP POST
1.  Authentication endpoint 
    ```
    http://localhost:4444/auth/signin
    ```

2.  Creates new account
    ```
    http://localhost:4444/api/v1/account
    ```

3.  Creates transaction
    ```
    http://localhost:4444/api/v1/transaction
    ```
    

## Used technology

1.  Spring Boot v2 (Spring v5) and Spring Data JPA
2.  Swagger/OpenAPI version 2 (RESTful API Documentation Specification) 
    ```
    http://localhost:4444/api/v1/swagger-ui.html
    ```
3.  SLF4J logging facade for Java
4.  Undertow as web server to run the application
5.  Google Gson (a Java serialization/deserialization library)
6.  PostgreSQL DB
7.  Dockers (with PostgreSQL and Java 8 images)
8.  Flyway tool for DB migrations and versioning
9.  Undertow embedded web server
10. Authentication with JWT and OAUTH2

## Not Implemented

1.  CD/CI
2.  Integration tests