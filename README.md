# URL Shortener

This is a simple URL shortener service built with Spring Boot. Project details - https://roadmap.sh/projects/url-shortening-service

## Prerequisites

- Java 21
- Maven 3.6+

## Getting Started

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/url-shortener.git
   cd url-shortener
   ```

2. Build the project:
   ```
   mvn clean install
   ```

3. Run the application:
   ```
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`.

## API Endpoints

### 1. Create a Short URL

- **POST** `/api/shorten`
- Body: `{ "url": "https://example.com/long/url" }`

### 2. Get Original URL

- **GET** `/api/shorten/{shortCode}`

### 3. Redirect to Original URL

- **GET** `/api/{shortCode}`

### 4. Update Short URL

- **PUT** `/api/shorten/{shortCode}`
- Body: `{ "url": "https://example.com/new/long/url" }`

### 5. Delete Short URL

- **DELETE** `/api/shorten/{shortCode}`

### 6. Get URL Statistics

- **GET** `/api/shorten/{shortCode}/stats`

## Testing

You can use tools like cURL, Postman, or any HTTP client to test the API endpoints.

Example using cURL:

1. Create a short URL:
   ```
   curl -X POST -H "Content-Type: application/json" -d '{"url":"https://example.com/long/url"}' http://localhost:8080/api/shorten
   ```

2. Get statistics for a short URL:
   ```
   curl http://localhost:8080/api/shorten/abc123/stats
   ```

## Database

The application uses an H2 in-memory database. You can access the H2 console at `http://localhost:8080/h2-console` with the following details:

- JDBC URL: `jdbc:h2:mem:urlshortener`
- Username: `sa`
- Password: `password`

## Contributing

Please feel free to submit issues, fork the repository and send pull requests!

## License

This project is licensed under the MIT License.
