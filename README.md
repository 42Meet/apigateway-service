# Apigateway-Service

### π»  μ€ν λ°©λ²

1. Mavenμ νμ©ν λΉλ λ° ν¨ν€μ§

   ```
   mvn clean compile package
   ```

2. Dockerizing

   ```
   docker build -t kangjm2/apigateway:1.0 .
   ```

3. Container μ€ν

   ```
   docker run -d -p 80:80 --name apigateway \
   -e token.secret=sample \
   --network 42meet \
   kangjm2/apigateway:1.0
   ```

* DockerHub μ΄μ©μ

  ```
  docker pull kangjm2/apigateway:1.0
  ```

