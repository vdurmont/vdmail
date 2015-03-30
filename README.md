# VDMail

Demo project: webservice that provides an abstraction between mail providers.

## Demo

[http://vdmail.herokuapp.com](http://vdmail.herokuapp.com)

## Local Installation

You have to provide a few configuration variables. Copy the `sample.env` file to `.env` and edit it with your api keys and preferences.

```
mvn clean install
foreman start web
```

The API will be available on [http://localhost:5000](http://localhost:5000).

## Documentation

See [DOCUMENTATION.md](./DOCUMENTATION.md).

## Technologies

* Java 8
* Spring Framework

## Todo

* Set up a real database instead of HSQL
* Pagination on the resources collections
* Lots of other things :)
