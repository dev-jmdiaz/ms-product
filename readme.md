# Inditex Product Service

This service is built with **Spring Boot 3.4+** and **Java 21**.

---

## 🚀 Requirements

Make sure you have the following installed on your system:

- Java 21 (JDK)
- Maven
- Docker

---

## ⚙️ How to Set Up the Project

### 1. Clone the repository

```bash
https://github.com/dev-jmdiaz/ms-product.git
cd ms-product
```


### 2. Start the environment with Docker

```bash
docker-compose build --no-cache  
  ```


 ```bash
  docker-compose up -d simulado influxdb grafana app
   ```
### 3. Test the service at [http://localhost:5000](http://localhost:5000)

Make a GET request to:
```http
http://localhost:5000/product/1/similar
```


## Performance Testing
To perform efficiency tests on the service and visualize the results in Grafana, execute the following command:

```bash
docker-compose run --rm k6 run scripts/test.js
  ```

Access the Grafana dashboard at:

http://localhost:3000/d/Le2Ku9NMk/k6-performance-test?orgId=1&from=now-5m&to=now

## ⚡ Service Behavior

1. The service has a timeout of **3 seconds** when consuming external services.
2. If a request exceeds this time, the process continues to run in the background.
3. Once the information is available, it is cached to improve performance for future requests.
4. The cache is configured with a default expiration of **1 minute**.


```application
spring:
  cache:
    time: 60 # Time the cache is maintained
    maxSize: 1000 # Maximum size of the cache
    cache-names: products
    caffeine:
      spec: maximumSize=500,expireAfterAccess=10m
  main:
    web-application-type: reactive

resilience4j:
  circuitbreaker:
    timeoutDuration: 3 # Timeout for the services to be consumed
```

## 🛠️ Error Handling
Exceptions for invalid requests are handled. It also correctly manages cases when no information is found while consuming external services.
