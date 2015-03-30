# VDMail API Documentation

## Introduction

VDMail API is based in the REST architecture in order to keep a predictable behavior.

* We use the common HTTP verbs to perform actions on resources: GET, POST, PUT, DELETE, PATCH
* JSON will be returned in all responses dfrom this API.
* HTTP status codes will indicate a success or the type of error that occurred.

All dates are in ISO 8601 format (example: "2015-03-30T00:16:12.244Z").

## Authentication

Each authenticated request must provide a valid token for a [Session](#sessions) in an `Authorization` header:

```
curl  -H "Authorization: Token <YOUR_TOKEN>" \
      http://vdmail.herokuapp.com
```

## HTTP status codes

We use HTTP status codes to indicate if a request failed or was a success.

* **2xx: success**
  * 200: success
  * 201: resource created
  * 204: no content
* **4xx: client error**
  * 400: bad request (missing or invalid attribute, malformed request...)
  * 401: unauthorized (you need to provide a valid authentication token)
  * 403: forbidden (you have been authenticated but you are not allowed to perform this operation)
  * 404: not found (we did not find the resource you are requesting)
  * 409: conflict (we cannot perform the request because there is a conflict: the action has already been done or the resource is reserved)
  * 419: authentication timeout (the authentication token you provided is expired)
* **5xx: server error**
  * 500: generic server error

Each error will return a JSON object like this one:
```
{
  "error": "The name of the error",
  "message": "A message providing more details on the error"
}
```

## <a name="sessions"></a>Sessions

### Model

The Session object:

```
{
  "id": 42, // int, unique identifier of the session
  "created_date": "2015-03-30T00:16:12.244Z", // The date of creation of the session
  "expiration_date": "2015-03-30T01:16:12.241Z", // The date of expiration of this session
  "user": { ... }, // A User object
  "token": "778ef4ad-fc5b-49d9-8901-43e138c997a8"
}
```

### Create a session

Issue a basic authenticated POST request on `/sessions`.

```
curl  -u <EMAIL_ADDRESS>:<PASSWORD> \
      -X POST \
      http://vdmail.herokuapp.com/sessions
```

### Delete a session

This method requires authentication.  

```
curl  -H "Authorization: Token <YOUR_TOKEN>" \
      -X DELETE \
      http://vdmail.herokuapp.com/sessions/<SESSION_ID>
```

## Users

### Model

The User object:

```
{
  "id": 42, // int, unique identifier of the user
  "created_date": "2015-03-30T00:16:12.244Z", // The date of creation of the user
  "address": "batman@vdmail.vdurmont.com", // The email address of the user
  "name": "Bruce Wayne" // The name of the user (optinal)
}
```

### Create a user

You have to provide the following properties:
* name
* address
* password

```
curl  -H "Content-Type: application/json" \
      -d "{ \"name\": \"Clark Kent\", \"address\": \"superman@vdmail.vdurmont.com\", \"password\": \"my_password\" }" \
      http://vdmail.herokuapp.com/users
```

### Get a user

This method requires authentication.  

```
curl  -H "Authorization: Token <YOUR_TOKEN>" \
      http://vdmail.herokuapp.com/users/<USER_ID>
```

### Get a user's contacts

This method requires authentication.  
The contacts are the previous recipients of this user (people who he sent email to).

```
curl  -H "Authorization: Token <YOUR_TOKEN>" \
      http://vdmail.herokuapp.com/users/<USER_ID>/contacts
```

This request will return a list of Users ordered by last sent email.

## Emails

### Model

The Email object:

```
{
  "id": 42,
  "created_date": "2015-03-30T00:16:12.244Z",
  "sender": { ... }, // A User object
  "recipient": { ... }, // A User object
  "subject": "Hello from Gotham City",
  "content": "Hi! How are you doing these days?",
  "provider": "MANDRILL" // The provider used to send the email: MANDRILL, SENDGRID, CONSOLE (debug only)
}
```

### Send an email

This method requires authentication.  
You have to provide the following properties:
* subject
* content
* recipient.address

```
curl  -H "Authorization: Token <YOUR_TOKEN>" \
      -H "Content-Type: application/json" \
      -d "{ \"recipient\": { \"address\": \"superman@vdmail.vdurmont.com\" }, \"subject\": \"Hello from Gotham City\", \"content\": \"Hi! How are you doing these days?\" }" \
      http://vdmail.herokuapp.com/users
```
