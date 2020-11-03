# URL Shortener
Shortens a URL and redirects you to it when navigating to the shortened id.

# To Run with docker
- Build the image: ./mvnw spring-boot:build-image
- Run docker-compose: docker-compose up

# To Run without docker
- adjust the spring.redis.host property in applications.properties to work with your Redis instance.
- run the ./mvnw spring-boot:run command
