# docker

- ./gradlew build
- docker build -t my-ktor-app .
- docker run -p 8080:8080 my-ktor-app

# tailwindcss

- npx tailwindcss -i ./src/main/resources/files/input.css -o ./build/resources/main/files/output.css --watch  
- npm run build-css 