# Pumped - Backend

## Develop locally

1. Copy `.env.template` to `.env`and adjust as you need

2. Install [taskfile](https://taskfile.dev/installation/)
   1. For npm use `npm install -g @go-task/cli`
3. Run `task dev` 
   1. > this will start the required databases inside a docker compose based on the .env
4. Run one of the predefined run configurations in IntelliJ
   1. `start backend` will start the backend in a default configuration
5. To stop the docker containers use `task dev-down`

## Run prod

1. Install [taskfile](https://taskfile.dev/installation/)
   1. For npm use `npm install -g @go-task/cli`
2. Run `task prod`