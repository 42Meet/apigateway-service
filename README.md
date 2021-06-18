# Apigateway-Service

### 💻  실행 방법

1. Maven을 활용한 빌드 및 패키징

   ```
   mvn clean build package
   ```

2. Dockerizing

   ```
   docker build -t 42meet/apigateway:1.0 .
   ```

3. Container 실행

   ```
   docker run -d -p 8000:8000 --name apigateway \
   -e "eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/" \
   -e "token.secret=my_secret_token" \
   42meet/apigateway:1.0
   ```

* DockerHub 이용시

  ```
  docker pull kangjm2/apigateway:1.0
  ```

