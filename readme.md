# StockCompany #
***
In this project  only backend for the service. There is no frontend.
This is a simple server-side project that help manage goods in your stocks.

There are some capabilities of the service:

* add a new good make request `POST /goods`, you must send a json with name of a new good, like this:
````json
{
  "name": "DVD"
}
````
* create a new stock make request `POST /stocks`, you must send a json with name of a new stock, like this:
````json
{
  "name": "NorthStock"
}
````

There is one way to add goods to stock - create a new document like this:
* add new goods to stock `POST /documents/addNewGoods`

````json
{
  "documentNumber": 110,
  "stockId": 2,
  "newGoods": [
    {
      "goodId": 1,
      "amount": 10,
      "price": 100
    },
    {
      "goodId": 2,
      "amount": 10,
      "price": 100
    },
    {
      "goodId": 3,
      "amount": 10,
      "price": 100
    }
  ]
}
````
There is few constraints to this operation
- document number must be unique;
- stock must be existed;

To get posted document make request `GET /documents/newgoods?documentNumber=110`
You can also create documents for sale good from specified stock or transfer them between two stocks.
Sale goods from stock make request `POST /documents/saleGoods` with json like this:
````json
{
  "documentNumber": 120,
  "stockIdFrom": 1,
  "saleGoods": [
    {
      "goodId": 1,
      "amount": 5,
      "price": 150
    },
    {
      "goodId": 2,
      "amount": 5,
      "price": 150
    },
    {
      "goodId": 3,
      "amount": 5,
      "price": 150
    }
  ]
}
````
After this operation you can see good balance with information from all 
stock by request `GET /report/balance`. In response you will see json like this:
````json
[
  {
    "vendorCode": "b15c7903-c8c8-4f94-946e-6f1937dab947",
    "name": "VHS",
    "balance": {
      "SouthStock": 10,
      "NorthStock": 5
    }
  },
  {
    "vendorCode": "6dc15dc5-4466-458c-a477-b04c2cd5a54d",
    "name": "DVD",
    "balance": {
      "SouthStock": 10,
      "NorthStock": 5
    }
  },
  {
    "vendorCode": "796f0634-b7ca-4383-b55f-7a85632f690a",
    "name": "PC",
    "balance": {
      "SouthStock": 10,
      "NorthStock": 5
    }
  }
]
````
Other feature of service - report with information about all goods in database with purchase prices 
and sale prices, you can get this information by request `GET /report/goods`
````json
{
    "content": [
        {
            "vendorCode": "b15c7903-c8c8-4f94-946e-6f1937dab947",
            "name": "VHS",
            "lastPurchasePrice": 100,
            "lastSalePrice": 150
        },
        {
            "vendorCode": "6dc15dc5-4466-458c-a477-b04c2cd5a54d",
            "name": "DVD",
            "lastPurchasePrice": 100,
            "lastSalePrice": 150
        },
        {
            "vendorCode": "796f0634-b7ca-4383-b55f-7a85632f690a",
            "name": "PC",
            "lastPurchasePrice": 100,
            "lastSalePrice": 150
        }
    ],
    "pageable": {
        "number": 0,
        "sort": {
            "sorted": false
        },
        "size": 100,
        "offset": 0,
        "unpaged": false,
        "sorted": false
    },
    "totalSize": 3,
    "totalPages": 1,
    "empty": false,
    "size": 100,
    "offset": 0,
    "pageNumber": 0,
    "numberOfElements": 3
}
````
How you can see service stored information about prices and amount of goods after sale some goods from NorthStock.

And other capabilities of service:
with goods:
* delete a good `DELETE /goods/{id}`;
* get all goods `GET /goods/list`;
* get a good by id `GET /goods/{id}`;
* update a good (you must know id of a good) `PUT /goods` with body 
````json
{
  "id": 1,
  "name": "new name of a DVD"
}
````
with stocks:
* delete a stock `DELETE /stocks/{id}`;
* get all stocks `GET /stocks/list`;
* get a stock by id `GET /stocks/{id}`;
* update a stock (you must know id of a stock) `PUT /stocks` with body
````json
{
  "id": 1,
  "name": "new name of a NorthStock"
}
````
***
All data is stored in memory with *H2 database*.<br>
Migration stores at `src/main/resources/db/migration/v1_schema.sql`<br>
Migration works with *flyway*.<br>
Tests cover main functionality of application controllers.
Application based on the *Micronaut* framework with using *jdbc* for create and store data.<br>
Main programming language is *java 11*.

### Running the application ###
* to test application:
`mvn test`
* to run application:
`mvn mn:run`

By default, it runs on http://localhost:8080.

