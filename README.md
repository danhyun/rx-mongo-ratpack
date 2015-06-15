# rx-mongo-ratpack
Test project 

Insert (to localhost):

`curl -d 'firstName=josh' -d 'lastName=durbin' -d 'age=33' http://localhost:5050/people`

Update (to localhost):

`curl -X PUT -d firstName="Billy" -d lastName="Bob" -d age="53" http://localhost:5050/person/557f56fc051d1fb82a5eeeb6`

Delete (to localhost):

`curl -X DELETE http://localhost:5050/person/557f56fc051d1fb82a5eeeb6`

List all (to localhost):

`curl http://localhost:5050/people`
