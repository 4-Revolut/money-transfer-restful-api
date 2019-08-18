# money-transfer
Restful api simulate money transfer between accounts

### To Try out locally

#### Tech
java: 8+  
Maven: 3.3.9+

#### Build 
###### from project root-directory 'money-transfer'
`mvn clean install`

#### Run jetty server
###### change directory to 'money-transfer-backend'
`mvn jetty:run`

#### Run functional api tests
###### change directory to 'money-transfer-test'
`mvn test -Dtest=*ApiTest`

### Already deployed solution

#### 'money-transfer-api' is deployed on Heroku cloud-platform:
###### http://money-transfer-revolut.herokuapp.com/4-Revolut/money-transfer/1.0

#### You can use swagger-editor gui to try out functionality:
###### https://app.swaggerhub.com/apis/4-Revolut/money-transfer/1.0

###### or any rest-api client available in your arsenal, like curl:
`curl -X POST "http://money-transfer-revolut.herokuapp.com/4-Revolut/money-transfer/1.0/user" -H "accept: */*" -H "Content-Type: application/json" -d "{\"id\":\"string\",\"email\":\"user@example.com\",\"firstname\":\"string\",\"lastname\":\"string\",\"accountArray\":[{\"id\":\"string\",\"name\":\"string\",\"balance\":0}]}"`