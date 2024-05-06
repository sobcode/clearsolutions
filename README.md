# clearsolutions
Test task for a Trainee/Junior Java Developer in ClearSolutions
</br>
</br>
</br>
**Environment:** Java, SpringBoot, MySQL
</br>
</br>
</br>
**Endpoints/JSON examples:**
</br>
1) Create user: (POST) http://localhost:8081/api/users

request body (JSON):

{ 

"email" : "{username}@{domain}", 

"firstName" : "{first name}", 

"lastName" : "{last name}", 

"birthDate" : "{YYYY-MM-DD}", 

"address" : "{address}", // optional field

"phoneNumber" : "{plus symbol(+) and 5-13 digits}" // optional field

}


2) Read users: (GET) http://localhost:8081/api/users

   2.1) Read users with birth date range: (GET) http://localhost:8081/api/users?from=YYYY-MM-DD&toYYYY-MM-DD

   2.2) Read users with birth date range (only from): (GET) http://localhost:8081/api/users?from=YYYY-MM-DD

   2.3) Read users with birth date range (only to): (GET) http://localhost:8081/api/users?to=YYYY-MM-DD

   * ADDITIONAL PAGINATION AND SORTING: ?size={number}&page={number}&sort={field name},{ASC/DESC}


3) Read user by id: (GET) http://localhost:8081/api/users/{id}


4) Delete user: (DELETE) http://localhost:8081/api/users/{id}


5) Update user with all fields specified: (PUT) http://localhost:8081/api/users/{id}

request body (JSON):

{ 

"email" : "{username}@{domain}", 

"firstName" : "{first name}", 

"lastName" : "{last name}", 

"birthDate" : "{YYYY-MM-DD}", 

"address" : "{address}",

"phoneNumber" : "{plus symbol(+) and 5-13 digits}"

}


6) Update user with one/some specified fields: (PATCH) http://localhost:8081/api/users/{id}

request body (JSON):

{ 

"email" : "{username}@{domain}", // optional field

"firstName" : "{first name}", // optional field

"lastName" : "{last name}", // optional field

"birthDate" : "{YYYY-MM-DD}", // optional field

"address" : "{address}", // optional field

"phoneNumber" : "{plus symbol(+) and 5-13 digits}" // optional field

}
</br>
</br>
</br>
**Requirements** of the test task:

1. It has the following fields:

    1.1. Email (required). Add validation against email pattern
   
    1.2. First name (required)
   
    1.3. Last name (required)
   
    1.4. Birth date (required). Value must be earlier than current date
   
    1.5. Address (optional)
   
    1.6. Phone number (optional)
   
3. It has the following functionality:

    2.1. Create user. It allows to register users who are more than [18] years old. The value [18] should be taken from properties file.
   
    2.2. Update one/some user fields
   
    2.3. Update all user fields
   
    2.4. Delete user
   
    2.5. Search for users by birth date range. Add the validation which checks that “From” is less than “To”.  Should return a list of objects
   
5. Code is covered by unit tests using Spring 
6. Code has error handling for REST
7. API responses are in JSON format
8. Use of database is not necessary. The data persistence layer is not required.
9. Any version of Spring Boot. Java version of your choice
10. You can use Spring Initializer utility to create the project: Spring Initializr
