# Master Card Payments Coding Assignment

## Introduction
This Spring Boot application is for managing the Intra-bank activities like money transfer, viewing transaction statements for the accounts.

## Information
This is a simple spring-boot application with basic auth spring security.

We have various endpoints for different services. See the Postman collection attached in the root of the Project.
We are using H2 database hence no need to install any database
We are using Jacoco for code coverage
We are using open api 3.0  for documentation once the application is started it is available under the path below
Open API path : http://localhost:8080/api-docs/
It is protected by basic auth 
Username : admin
Password : admin
Swagger UI : http://localhost:8080/swagger-ui/index.html#/

## System should be:

• accessible by Restful Webservices

• able to tell account balance in real time

• able to get mini statement for last 20 transactions

• able to transfer amount in real time

• able to fetch accounts details from accounts service (new / deleted)

Create accounts with POST create url(postman collection) before proceed with other operations.

