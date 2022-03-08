# JwtSecurity-SpringBoot
## Project Title [TryingJWT]

## EndPoints:
  * `/api/login`:
  - All is permitted to login first to get tokens (access / refresh)
    -- use access token to access all resources (i,e add it to Autorization header)
  * `/api/users`
    - list all users in the system.
  * `/role/save`
      - save role to the system ( send role as request body).
  * `/user/{username}`
      - fetch user by username (i,e send username as pathvariable).
  * `/role/addToUser`
      - add role to user ( send Role as request body).
  *  `/token/refresh`
      - refresh access token (send refresh token as Bearer ).


## References 
 *  https://www.javainuse.com/spring/boot-jwt
 *  https://www.youtube.com/watch?v=VVn9OG9nfH0

## Authors
 Khaled Mohamed Abdelghany
