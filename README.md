# Core Bank Application

## Prerequisites

Ensure you have the following installed: 
* Java 21
* Docker

## Building the application

To build the file, navigate to the root folder and run 

```./gradlew build```

## Running the application
To run the application, navigate to the root folder and run

``docker compose up``

## Using the application

The application can be interacted with through the following endpoints on port 8080:

* GET /api/account/{accountId} - Get data related to specified account
* POST /api/account - Create an account with JSON body
  * ```
    {
        "customerId": "UUID",
        "country": "string",
        "currencies": ["string"]
    } 
    ```
* GET /api/transaction/{accountId} - Get all transactions related to account
* POST /api/transaction - Create a new transaction with JSON body
  * ```
    {
        "accountId": "UUID",
        "amount": "floating point number",
        "currency": "'EUR' | 'SEK' | 'GBP' | 'USD'",
        "direction": "'IN' | 'OUT'",
        "description": "string"
    }
    ```
    