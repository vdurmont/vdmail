# Tutorial

This tutorial will guide you through signup, login and sending an email.

## Signup

You first have to create a user account:

```
curl  -H "Content-Type: application/json" \
      -d "{ \"name\": \"Clark Kent\", \"email\": \"superman@vdmail.vdurmont.com\", \"password\": \"my_password\" }" \
      http://vdmail.herokuapp.com/users
```

The response will look like:

```
{
  "id": 4,
  "created_date": "2015-03-30T03:53:42.258Z",
  "name": "Clark Kent",
  "address":"superman@vdmail.vdurmont.com"
}
```

## Login

To login, you have to create a session. We will use basic authentication for this endpoint.

```
curl  -u superman@vdmail.vdurmont.com:my_password \
      -X POST \
      http://vdmail.herokuapp.com/sessions
```

The response will look like:

```
{
  "id": 2,
  "created_date": "2015-03-30T03:57:07.061Z",
  "token": "d86233da-9b57-4bf4-83eb-b0d81b48ad22",
  "expiration_date": "2015-03-30T04:57:07.060Z",
  "user":{
    "id": 4,
    "created_date": "2015-03-30T03:53:42.258Z",
    "name": "Clark Kent",
    "address":"superman@vdmail.vdurmont.com"
  }
}
```

The `token` will be used for authentication from now on.

## Send an email

You have to post the email you want to send:

```
curl  -H "Authorization: Token d86233da-9b57-4bf4-83eb-b0d81b48ad22" \
      -H "Content-Type: application/json" \
      -d "{ \"recipient\": { \"address\": \"batman@vdmail.vdurmont.com\" }, \"subject\": \"Hello from NYC\", \"content\": \"Hi! How are you doing these days?\" }" \
      http://vdmail.herokuapp.com/emails
```

The email is sent! The response will look like:

```
{
  "id": 5,
  "created_date": "2015-03-30T04:00:15.389Z",
  "sender": {
    "id": 4,
    "created_date": "2015-03-30T03:53:42.258Z",
    "name": "Clark Kent",
    "address":"superman@vdmail.vdurmont.com"
  },
  "recipient": {
    "id":5,
    "created_date":"2015-03-30T04:00:15.387Z",
    "address":"batman@vdmail.vdurmont.com"
  },
  "subject": "Hello from NYC",
  "content": "Hi! How are you doing these days?",
  "provider": "MANDRILL"
}
```
