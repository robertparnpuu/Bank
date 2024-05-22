## Explanations

* Using UUIDs for easier persistence when scaling the database horizontally.
* Sending messages to RabbitMQ after all checks of data validity are done. Made with the expectation that validity of the request is paramount to and expected from the other Consumers. 
* Catching exceptions using an @ExceptionHandler so the request response can be manipulated easier.
* Focusing on integration tests, as the functionality of the application is quite simple, I do not find further Unit Tests necessary.

## Performance estimation

Tested performance using Apache Bench (ab) cli tooling on WSL with application Docker container running on Windows, using a Asus Zenbook

Create transaction was chosen as the benchmarking functionality, as it is the most demanding.

### Results

5000 requests with 5 running concurrently: 0 failures, 179.19 requests per second, 27.903 ms per request (mean)

5000 requests with 100 running concurrently: 0 failures, 157.30 requests per second, 635.714 ms per request (mean)

5000 requests with 500 running concurrently: 0 failures, 138.44 requests per second, 3611.635 ms per request (mean)

## What to consider for horizontal scaling

* For an actual application, proper logging and security measures must be developed
* Kubernetes or something similar to scale and manage multiple instances of the service.
* Proper load balancing between multiple machines, as with the load test, requests get unacceptably long with many running at the same time 
* An idea would be to have the HTTP request receiver be separate middleware which sends messages, and the main application to be a consumer from a message queue.
  * This is both to be able to scale them separately and to make the whole application run concurrently with other services which are consumers of the MQ.
  * Depends on the business logic of other services.
* Setting up database replication and sharding