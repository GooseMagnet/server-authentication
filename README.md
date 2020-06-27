### Register a user 
email: `test@test.test`

name: `testName`

password: `testPassword`
```shell script
curl -X POST -H 'Content-Type: application/json' -d '{"email": "test@test.test", "name": "testName", "password": "testPassword"}' localhost:8080/register
```

### Login as user 
email `test@test.test` 

password `testPassword`
```shell script
curl -X POST -H 'Content-Type: application/json' -d '{"email": "test@test.test", "password": "testPassword"}' localhost:8080/login
```

### Access authorized resource
```shell script
curl -H 'Content-Type: application/json' --cookie 'JSESSIONID=08CC6D12B2B32E09B6053629E3412C5B;' localhost:8080/home 
```