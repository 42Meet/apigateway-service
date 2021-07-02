# Apigateway-Service

### 💻  실행 방법

1. Maven을 활용한 빌드 및 패키징

   ```
   mvn clean compile package
   ```

2. Dockerizing

   ```
   docker build -t kangjm2/apigateway:1.0 .
   ```

3. Container 실행

   ```
   docker run -d -p 80:80 --name apigateway \
   -e token.secret=sample \
   --network 42meet \
   kangjm2/apigateway:1.0
   ```

* DockerHub 이용시

  ```
  docker pull kangjm2/apigateway:1.0
  ```

