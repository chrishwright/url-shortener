# url-shortener

A url-shortener application built using Java, Apache Cassandra, and Docker.  This is WIP.  This project is to better understand how to solve some fundamental complexities in distributed systems.  More updates/documentation to come.

## Installation

You will need to have Docker installed and running.  I have about 8GB for Docker running on my machine, since this service relies on 2 Cassandra instances.  From there, you can run the following commands:

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

In this case, yLyMHJK is the short URL that was provided after a POST request had been issued.