# Setup

You need:

- JDK 17

For powershell you can do:

$env:JAVA_HOME="JDK 17 Path" ; ./mvnw spring-boot:run

Create docker env

docker run --name documentdb -p 5432:5432 -e POSTGRES_USER=jpmorgan -e POSTGRES_PASSWORD=documentpass -e POSTGRES_DB=docdb -d postgres