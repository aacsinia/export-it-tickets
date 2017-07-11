Requirements:
Java 8
Maven

Commands to run:
mvn install
cd rest/
mvn clean spring-boot:run [--test.items.count=1000000]
    test.items.count - how many item to create for testing purpose

To see api documentation:
http://localhost:8080/v2/api-docs
