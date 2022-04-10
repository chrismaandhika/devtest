Prerequisite to Run
-------------------
- Java 11 has already installed.

- Maven has already installed.

- PostgreSQL database has already created and initiated with tables in oauth2api/src/main/resources/oauth2api_db_init.sql

Rest API Test
-------------
There will be three scopes in the API, read, write, and delete.
Read scope is required to access get contacts endpoint, write scope is required to access create contact and update contact endpoints,
and delete scope is required to access delete contact endpoint.

We will have two users based on oauth2api_db_init.sql previously applied, which are super_user and normal_user.
super_user will have all scopes, while normal_user will have read and write scopes, but not delete scope.
both users can be authenticated with same password, which is: password.

There are five endpoints, which are:
- POST /token             -- to request token or to refresh token.
- GET /phone_contacts     -- to get all contacts owned by a user inferred from the token.
- POST /phone_contacts     -- to create a contact that will be owned by a user inferred from the token.
- PUT /phone_contacts     -- to edit a contact that is owned by a user inferred from the token.
- DELETE /phone_contacts     -- to delete a contact that is owned by a user inferred from the token.

To run the API, first go to oauth2api/, then run mvn clean package.
The command will build a jar, oauth2api-1.0.jar inside oauth2api/target/.
If you prefer to run it from other directory, then copy the jar into the desired directory.

Then in the directory where the jar resided, replace the command below with correct values according to your PostgreSQL setup:
```
java -jar oauth2api-1.0.jar --keystore.path=keystore.jks --keystore.password=password --keystore.key.alias=jwtkeypair --spring.datasource.url=jdbc:postgresql://<db_host>:<db_port>/<db_name> --spring.datasource.username=<db_username> --spring.datasource.password=<db_password> --token.url=<token_url> --server.port=<port>
```

for example:
```
java -jar oauth2api-1.0.jar --keystore.path=keystore.jks --keystore.password=password --keystore.key.alias=jwtkeypair --spring.datasource.url=jdbc:postgresql://localhost:5432/oauth2api --spring.datasource.username=oauth2api --spring.datasource.password=password --token.url=http://localhost:8081/token --server.port=8081
```
To do some testings to the API, you use Postman and import Oauth2API.postman_collection.json that is resided in:
oauth2api/src/main/resources/ <br>
or you can use swagger UI in http://<host>:<port>/swagger-ui.html

Before you access any of phone contact endpoints, you need to get access token from the /token endpoint.
Put the access token as Bearer Authentication when accessing any phone contact endpoints.
Refer to the result screenshots to get details of accessing each endpoints.
