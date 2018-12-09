# Hardware Shop
This is a sample application with the purpose of showing a simple shop example

## Domain
Hardware shop concerns itself with the shopping domain. The application consists of 2 rest services: product catalog and order handling.

The product catalog service exposes the following REST apis:  
* GET to retrieve all products   
`curl 'http://localhost:8080/products' -i -X GET`
* POST to create a product   
`curl 'http://localhost:8080/products' -i -X POST \
                     -H 'Accept: application/json' \
                     -H 'Content-Type: application/json' \
                     -d '{
                   "name" : "testProduct",
                   "price" : 1
                 }'`
* PUT to modify a product   
` curl 'http://localhost:8080/products/PRODUCT_ID' -i -X PUT \
                               -H 'Accept: application/json' \
                               -H 'Content-Type: application/json' \
                               -d '{
                             "name" : "testProduct",
                             "price" : 10
                           }'`

**see the product-catalog target/generetad-snippets for details**  

The order service exposes the following REST apis:
* GET to retrieve all orders details   
`curl 'http://localhost:8080/orders' -i -X GET`  
* GET to retrieve the order status and (re)calculate the total price   
`'http://localhost:8080/orders/e9cf1e0b-8536-46c6-9cd4-eb5ba6d1d7f1' -i -X GET`
* GET to retrieve the order details   
`'http://localhost:8080/orders/e9cf1e0b-8536-46c6-9cd4-eb5ba6d1d7f1/details' -i -X GET`
* GET to filter an order by confirm date range   
`'curl 'http://localhost:8080/orders/filter?start=2017-08-04&end=2019-08-04' -i -X GET`

* POST to create an order  
`curl 'http://localhost:8080/orders' -i -X POST \
                             -H 'Accept: application/json' \
                             -H 'Content-Type: application/json' \
                             -d '{
                           "customerName" : "name",
                           "customerEmail" : "asd@asd.asd",
                           "productIDs" : [ "productID" ]
                         }'`
* PUT to confirm an order   
`curl 'http://localhost:8080/orders/e9cf1e0b-8536-46c6-9cd4-eb5ba6d1d7f1/confirm' -i -X PUT`

**see the order-query target/generetad-snippets for details**

When an order is created with POST it is validated. An order is valid if all products are valid to be purchased.  
In this example a product is valid to be purchased if exists.
A valid order has APPROVED status
An invalid order has REJECTED status.

Only an order in APPROVED state can be CONFIRMED.

If a product changes in any of it's property, all the APPROVED order will be updated, the CONFIRMED order will not be updated.


 

## Technical Details
The application is built with Spring Boot and Axon Framework.
The application is extended with a custom @DTO annotation on controllers to automatically map
request/response DTO to/form domain objects

The product service is a standard CRUD service

The order service is implemented using CQRS+(a bit of)EventSourcing using Axon Framework

The application stores the data using H2 in ~/db/hs.

H2 console available at http://127.0.0.1:8080/console/


## Usage
In order to run the single node version you may execute the following commands: 
* `mvn clean install`
* `java -jar springbootapp/target/springbootapp-1.0-SNAPSHOT.jar`.