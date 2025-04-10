FROM amazoncorretto:21

COPY /app/target/ms-product-1.0.0.jar /app/ms-product-1.0.0.jar


WORKDIR /app

EXPOSE 5000

CMD ["java", "-jar", "ms-product-1.0.0.jar"]
