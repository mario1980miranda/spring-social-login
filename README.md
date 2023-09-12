## How to run this APP

## VSCode

### launch.json to charge up .env file

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Current File",
      "request": "launch",
      "mainClass": "${file}"
    },
    {
      "type": "java",
      "name": "SpringSocialLoginApplication",
      "request": "launch",
      "mainClass": "com.code.truck.oauth.springsociallogin.SpringSocialLoginApplication",
      "projectName": "spring-social-login",
      "envFile": "${workspaceFolder}/.env"
    }
  ]
}
```

### When test profile is active

PostgreSQL docker must be running in order to start this app.

1. Start postgreSQL docker service from the docker compose :

```bash
docker compose up -d
```

2. Start springboot application
