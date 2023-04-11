# url-shortener

A url-shortener application built using Java, Apache Cassandra, and Docker.  This is WIP.  This project is to better understand how to solve some fundamental complexities in distributed systems.  More updates/documentation to come.

## Installation

You will need to have Docker installed and running.  I have about 8GB for Docker running on my machine, since this service relies on 2 Cassandra instances (which can use between 2-4 GB each out of the box).  From there, you can run the following commands:

```
docker compose build
docker compose up
```

This will build and start the main application.  From here, a server should be running on port 8080.  This gets mapped to your local machine.

## Usage

For now, there is just a backend API server.  A frontend component will get added in later.

To use the backend API server, you can use cURL or Postman to issue HTTP requests to the server.

```
curl --location 'http://localhost:8080/api/v1' \
--header 'Content-Type: text/plain' \
--data 'https://www.new-url.com'
```

This will return a short URL for the provided URL in the data param.

Then, you can take any short URL that you have been provided and request the affiliated long URL via the following:

```
curl --location 'http://localhost:8080/api/v1/yLyMHJK'
```

In this case, yLyMHJK is the short URL that was provided in the response from a POST request had been issued.

## Testing

There are separate docker files for the integration tests.  These run separately from the main build because they require a running container which should be separate from the main application.

```
docker compose -f integration-tests-docker-compose.yml build

docker compose -f integration-tests-docker-compose.yml up
```

Note that you can run the tests for the api-server from the `api-server` directory.  For the following commands, `mvn test` will run the unit tests, and `mvn verify` will run the integration tests.

```
cd api-server
mvn test
mvn verify
```

## Unique ID Generators

The URL Shortener can generate its unique URLs a number of different ways.  Some are more straight forward while others are more performant.  In order to generate a unique URL, the system needs a way to encode a unique ID into a short URL.  This can be done using a base encoder.  I have chosen to implement a base62 encoder in this application to transform a unique numerical ID into a short string.  This means that we can represent a base10 number (0-9) in 62 different ways.  The number 0 can be represented by the base62 encoded value of 0.  1 becomes encoded into 1, 9 is encoded into 9.  When we hit 10, that becomes encoded into the character 'a'.

Here is the set of numbers used in a base 62 encoder

```
0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ
```

Since a unique ID generator such as described above might not meet all business requirements (short URLs might be >= 9 characters, for example) I am also working on a second unique ID generator.  This WIP uses a counter which increments by 1 over time to generate the unique URLs.  This adds support shorter URLs over a longer period of time.

### Time Increasing Unique Number Generator

The first unique ID generator increases based off of the time.  It only outputs numerical values up to 64 bits.  Depending on the configuration set in the Java class, the unique ID generator could generate unique IDs for several days or a few hundred years.  The tuning of the configuration properties will dictate:

* The length of the short URL returned by the application.
* The length of time that the service will be able to generate unique URLs.
* How performant it will be.
* How many distinct servers are available in the system at any given time to generate unique IDs.

Here are the configuration properties in the Java code:

```
public static final int WORKER_BITS = 4;
public static final int SEQUENCE_BITS = 9;
public static final long EPOCH_TIME = 1680484979000L;
private static final long SEQUENCE_MAX = (long) Math.pow(2, SEQUENCE_BITS);
```

The `WORKER_BITS` variable describes how many servers that can be made available in the system.

The `SEQUENCE_BITS` describe the max count of unique IDs that are possible for every millisecond of time that has passed. I.E. if the variable is set to 9, then it means the system can have 2^9 = 512 unique IDs per millisecond.

The `EPOCH_TIME` variable is the timestamp from which the URL Shortener service originated.  This allows a wider timestamp range for which to generate URLs because we can subtract this from the current timestamp to give a smaller timestamp to start which provides more time before the app will run out of shorter URLs we can generate.  I.E. as time goes on, the URLs will become longer because the timestamp will become longer.  Subtracting the epoch will allow for more short URLs to be generated for some time.

The `SEQUENCE_MAX` variable is the maximum number for a counter (which resides in the last `SEQUENCE_BITS` unique ID bits) before the program needs to wait for the next timestamp to occur.  I.E. if the `SEQUENCE_MAX` is 512 (2^9), then we can generate 512 unique IDs per millisecond timestamp.  I.E. we could have:

* timestamp=1234 sequence = 0
* timestamp=1234 sequence = 1
* timestamp=1234 sequence = 2
* timestamp=1234 sequence = ...
* timestamp=1234 sequence = 512
* \*\*Sequence starts over here as it reached the max and the program waits for the next timestamp to occur\*\*
* timestamp=1235 sequence = 0

### Tradeoffs when choosing the configuration properties

The values for these properties are important for a few reasons.

* As the `SEQUENCE_BITS` number becomes smaller, the performance degrades.  This is because the smaller the number, the more often the program has to reset the value to 0 and restart the sequence.  The program also has to wait for the next millisecond timestamp to occur.  Keeping a larger number increases performance.  When set to 9, the program is able to generate 1.25 million unique IDs per second on my development machine.  However, changing this to 5 results in a runtime of 40 seconds to generate 1.25 million unique IDs.
* The `WORKER_BITS` dictates how many servers you can have in a system.  2^4 means you have can 16 unique servers (labeled from 0-15) in the system.  Each server needs to have a unique ID in this range for the system to work properly.  Increasing this number, or any number, means that the length of the URLs will become longer unless another number is decreased to even things out.
* The `EPOCH_TIME` variable can be set to whenever the application first runs to allow for a greater longevity in short URLs before the length of the short URL increases.  Once the application is first deployed this number should not change.

### The math behind choosing the right properties

Setting the properties dictactes performance and the length of the short URL (among other things).  The business requirements will dictacte how to set these.

If the application only needs 2 servers, then the number of `WORKER_BITS` can be set to 2, for example.  Redundancy should be a factory in making this decision because if 1 server dies that would be a single point of failure.

The length of `WORKER_BITS` + `SEQUENCE_BITS` dictates how long your short URL will be.  If you demand a unique URL of 7 characters or less, then that means the largest number that you can pass to the Base62Encoder is:

```
62^7 = 3521614606207
```

If we have chosen 4 `WORKER_BITS` and 8 `SEQUENCE_BITS` this means that there are 12 bits all together to represent those numbers.  2^12 is 4096.  Since bitshifting to the left or the right multiples and divides (respectively) by the numbers preceding it, this means that we can use the following formula to determine the maximum amount of time for which we can generate unique URLs based off of our `EPOCH_TIME` property:

```
(MAX_UNIQUE_NUMBERS / 2^(WORKER_BITS + SEQUENCE_BITS)) + EPOCH_TIME
```

Applying this to the example where we want 7 unique characters, 8 sequence bits and 4 worker bits:

```
(62^7 / (2^(8+4))) + 1680484979000
(3521614606207 / 4096) + 1680484979000 = 
859769190 + 1680484979000 = 1681344748190
```

This results in (1681344748190) April 12, 8:12 PM according to the epoch of 1970.  This is only a few days away from the time of this writing, so if 7 characters or less is desired for the short URL, this approach may not work for your needs, and it might be worth while to consider an incrementing counter based approach instead.

However, if it is acceptable to generate URLs that are 9 characters or longer, than you can use the same formula to arrive at a lifespan of 52 years for unique short URLs.  I also updated `SEQUENCE_BITS` to be 9 to improve performance as well:

```
(62^9 / (2^(9+4))) + 1680484979000
(3521614606207 / 8192) + 1680484979000 = 
859769190 + 1680484979000 = 1681344748190
```

This number is roughly 52 years in the future from the time of this writing.