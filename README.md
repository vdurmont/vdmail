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

See [DOCUMENTATION.md](./DOCUMENTATION.md) for the full API documentation.

See [TUTO.md](./TUTO.md) for a quick tutorial explaining how to signup, login and send an email.

See [ADMIN.md](./ADMIN.md) for an overview of the admin features.

## Technologies

* Java 8
* Spring Framework
* HSQLDB

## Todo

* Set up a real database instead of HSQL
* Pagination on the resources collections
* Add providers
* Create a frontend
* Lots of other things :)
