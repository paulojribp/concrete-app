# Login simples App Project

To create a User we have a service named: `/user`

On *Heroku* service, you can make a **POST** request to `http://paulojr-concrete-app.heroku.com/user` with
the minimum following json:

```
{
    "name": "João da Silva",
    "email": "joao@silva.org",
    "password": "hunter2"
}
```

To make the app login, you can make a **POST** request to `http://paulojr-concrete-app.heroku.com/login`
with the minimum json:

```
{
    "email": "joao@silva.org",
    "password": "hunter2"
}
```

And to see the user profile, you'll need to make a **GET** request to `http://paulojr-concrete-app.heroku.com/profile/{userId}`.
In this request you will need to send a `token` parameter to JWS authentication in *Request Headers*.

