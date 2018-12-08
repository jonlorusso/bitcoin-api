# bitcoin-api

This application uses [Spring Boot](https://spring.io/projects/spring-boot) to stand up a single endpoint (`/address/{address}`) which serves a list of unspent transaction outputs for the specified bitcoin address.  This endpoint in turn makes a request to the [blockchain.info API](https://www.blockchain.com/api/blockchain_api) to retrieve transaction data for the given address.  It then filters the list of all transaction outputs to just those that were sent to the address and have not been used as inputs themselves to other transactions.  While it is possible to do this latter check given just the data returned by the `/rawaddr` call (by removing all transaction inputs from the list of outputs), it is much easier to just use the (undocumented?) `spent` flag.

The project also makes use of the [Spotify Dockerfile Plugin](https://github.com/spotify/dockerfile-maven) which integrates Docker build with the maven build process to produce Docker images.  

### Build

```
./mvnw package
```

### Run

Install the dependencies and devDependencies and start the server.

```
docker run -it --rm -p 127.0.0.1:8080:8080/tcp com.jonlorusso/bitcoin-api:1.0.0
```

Then in a browser, open:

```
http://localhost:8080/address/3FjVHH5CuERomLc469aXMB1TJEEoRnbzqQ
```

