# local
- to build the environment "./gradlew build"
- to run the project "./gradlew run"

# tailwindcss
- npm run build:tailwind
- ./gradlew run

# name of the app
- tennis.caritos.com
- king of the court
- courtking

## deployment
- build the jar from localhost
  - ./gradlew build
- transfer build to host
  - cd /Users/eladio/src/busy-bee/build/libs
  - scp com.caritos.busy-bee-all.jar root@67.205.148.123:/root/busy-bee/build/libs/
- run docker compose on remote host
  - cd busy-bee
  - docker-compose build --no-cache
  - docker-compose down
  - docker-compose up -d
