# Account Transactions Report RESTful API Description

This is a simple project that returns the list of money account transactions created in the current calendar month for 
a given customer, and the total amount in GBP; creates accounts, transactions and obtain them. 
Search for accounts and transactions. In order to create successfully a debit transaction will the funds sufficient 
should be sufficient in the account (total available balance - debit amount >= 0). Currencies for transactions should be
already created in the currency table, and currency of the account must be the same as the currency of the transaction.


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
8. To verify that the application has started correctly go to :
    ``` 
    http://localhost:4444/api/test
    ``` 
    The following message should appear in the browser :
    ```
    Hello from account transactions report!
    ```

## API Endpoints

### HTTP GET
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

### HTTP POST
1.  Creates new account
    ```
    http://localhost:4444/api/account
    ```

2.  Creates transaction   
    ```
    http://localhost:4444/api/transaction
    ```
    

## Used technology

1.  Spring Boot v2 (Spring v5) and Spring Data JPA
2.  Swagger/OpenAPI v.2 (RESTful API Documentation Specification) 
    ```
    http://localhost:4444/api/swagger-ui.html
    ```
3.  SLF4J logging facade for Java
4.  Undertow as web server to run the application
5.  Google Gson (a Java serialization/deserialization library)
6.  PostgreSQL DB
7.  Dockers (with PostgreSQL and Java 8 images)
8.  Flyway tool for DB migrations and versioning
9.  Undertow embedded web server

## Not Implemented

1.  Security
2.  Authentication (JWT)
3.  Authorization