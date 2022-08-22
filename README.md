# Temperature Sensor

An API to be used by a client connected to a temperature sensor. The sensor sends to the
client a continuous stream of temperature data that must be sent to the API. It's possible for the
client not to have an internet connection in which case, the data is stored locally and synchronized,
in bulk, to the API as soon as the network connection is established. The client displays this
information in a chart where data can be seen per hour or daily, however since the client has
resource limitations, it relies on the API to aggregate data.

## Requirements

1. Java - 1.11.x

2. Maven - 3.x.x

3. Docker(20.10.17), Compose(2.7.0)


## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/norouziMahsa/temperature-sensor
```

**2. Run Kafka instance**

since for this application we are using Kafka, we need to have a Kafka instance up and running,
a docker compose file is placed in the root directory, so we can run this command:

```bash
docker-compose up
```

**3. Build and run the app using maven**

you can run the app using

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:7575>.

## Explore Rest APIs

The app defines following APIs:

    POST /temperature/publish (client should call this API while sending data in order to save temperetures)
    curl -X POST http://localhost:7575/temperature/publish -H 'Content-Type: application/json' -d '[{  "timestamp": 1572505200000,"value" : 12 }]'

    GET /temperature/hourly (to retrieve the aggregated temperature data hourly)
    curl -X GET 'http://localhost:7575/temperature/hourly?from_timestamp=1572505200000'

    GET /temperature/daily (to retrieve the aggregated temperature data daily)
    curl -X GET 'http://localhost:7575/temperature/daily?from_timestamp=1572505200000'

Example aggregated result for Hourly and Daily endpoints:
    
     { "timestamps": [ 1572505200000, 1572510600000 ], "temperatures": [ 24.87, 32.28 ] }
    

You can test them using postman or any other rest client.

## Rest API Documentation

You can find the APIs documentation for this application on

<http://localhost:7575/swagger-ui/index.html/>

## How Does It Work

- Client
  * Client sends its data to the application server, client can send one or multiple temperature data, 
    a data format can be like this :
    ```json
    [{  "timestamp": 1572505200000,"value" : 12 }]
    

- Server
  * Application server consists of two main parts, Consumer and Producer,
    Producer is responsible to receive and collect client data and to persist them in a kafka
    topic, by using this approach client can communicate to the server very fast and with the
    minimum waiting response time,
    Consumer is responsible to retrieve data from the topic, convert them to appropriate data type
    and finally persist them in a database,
    Application server may contain one or more kafka consumer.
    

- Database
  * In this project we have used a relational database (h2 - in memory) and because performance
    is critical on retrieve endpoints, we can define a clustered index (the physical storage order
    of our data) on our timestamp column.
    (h2 console is accessible from this address http://localhost:7575/h2-console)
    

- Caching
  * We can enable caching on our services so tempratures for a given “fromTimestamp” can be
    cached. With caching, we store such frequently accessed data in memory to avoid hitting
    the costly backends every time when user requests the data. For this project I have used
    in-memory cache but in a production environment we can use other distributed caching solutions.
    


    
