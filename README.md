# bitcoin-api

This application uses [Spring Boot](https://spring.io/projects/spring-boot) to stand up a single endpoint (`/address/{address}`) which serves a list of unspent transaction outputs for the specified bitcoin address.  This endpoint in turn makes a request to the [blockchain.info API](https://www.blockchain.com/api/blockchain_api) (`/rawaddr` endpoint) to retrieve transaction data for the given address.  It then filters the list of all transaction outputs to just those that were _sent_ to the address in question and have not been subsequently used as inputs themselves to other transactions.  However, instead of doing this latter operation, it was much easier to just use the (undocumented?) `spent` flag specified on each output.  Moreover, it would also have been possible to use the `/unspent` endpoint, which returns the requisite data directly.
  
The project also makes use of the [Spotify Dockerfile Plugin](https://github.com/spotify/dockerfile-maven) which integrates Docker builds with the maven build process to produce Docker images.  

### Requirements

* Java 8
* Maven
* Docker


### Build

```
./mvnw clean package
```


### Run


```
docker run -it --rm -p 127.0.0.1:8080:8080/tcp com.jonlorusso/bitcoin-api:1.0.0
curl http://localhost:8080/address/3FjVHH5CuERomLc469aXMB1TJEEoRnbzqQ
```

