# careless-clinic
Clinic with that doesn't care about your data

Spring Security for authentication and authorization, and Spring Data JPA for data access.


With more time, we could leverage modern application design patterns that support secure coding practices.
Some examples are Spring Modulith for domain driven design separation - allowing compartmentalized modules for security, data access, and business logic.


We could go further here - implement more security anti-patterns (like broken access control, insecure direct object references, improper error handling that leaks
information, etc.), All in the spirit of an educational focus.




## JWT

```shell
$ scripts/sign-jwt.py --key private.pem '{"user": "jay"}'
eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoiamF5IiwiaWF0IjoxNzYyNzIwODI5fQ.KJXgF5VZr6dHKFt57FmSdr7e_L5IoQTgbLFVG1qY7JqU_qQiOglX3ogyZz3M5x41F_oxcIc8QNmzSpTNOPQC0A
```

