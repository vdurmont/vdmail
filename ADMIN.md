# Admin features

## Overview

To get an overview of the current state of the app:

```
curl http://vdmail.herokuapp.com/admin/status
```

The response is like this:

```
{
  "stats": {
    "num_users": 3, // The number of users (when we send an email to someone, a user is created)
    "num_active_users": 3, // The number of active users (users who actually signed up)
    "num_emails_sent": 4
  },
  "providers": { // The state of the providers
    "mandrill": "ACTIVE",
    "sendgrid": "ACTIVE"
  }
}
```

## Provider status

The status of the providers in the previous request can be:

* `ACTIVE`: we can send emails with this provider
* `INACTIVE`: we simulate a failure with this provider
* `NOT_CONFIGURED`: this provider has not been configured

## Activate/deactivate a provider

Providers:
* `mandrill`
* `sendgrid`

Activate a provider:

```
curl  -X POST \
      http://vdmail.herokuapp.com/admin/providers/<PROVIDER>
```

Deactivate a provider:

```
curl  -X DELETE \
      http://vdmail.herokuapp.com/admin/providers/<PROVIDER>
```
