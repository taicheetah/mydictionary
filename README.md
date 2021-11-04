# My Dictionary
![result2](https://user-images.githubusercontent.com/45550613/140232736-f992a95b-de53-4d42-a906-4348e881cce0.gif)

You can use this application from [here](https://tc-mydictionary.herokuapp.com).  
【Test user】  
Email: test@gmail.com  
Password: testuser01

## About The Application
This application mainly has two features.
1. Search Word  
You can use this feature when you find an English word you don't know. If you want to try to remember the word, you can register the word to your dictionary by pushing "REGISTER" button.

2. Word List  
You can see words you registered. If you remember a word, you can hide the word by checking "Remember?" and pushing "UPDATE" button below. Also, You can filter words by word name, date and Remember or not. You can sort words by word name and date as well. (You have to chose your time zone when you sign up, then the app show you appropriate time following the time zone.) If you click "Hide" button, which is in "Definition & Example" cell, you can hide all definitions and examples. But you can see them when you do mouse-over.

## Why was this application made?
First, I needed portfolio that is only in English.  
Second, I wanted a good tool for expanding English vocabulary.  
Third, I had never released my application to the public, so I wanted have such experinence.

## What did I learn from the application?
- Managing Time Zones
- Managing Pagination
- Managing Entity relationships using JPA
- Managing BootStrap
- How to change specific properties like datasource url depending on environment
- How to use container-based cloud Platform as a Service like Heroku

## Built with
#### Back-End  
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)

#### Front-End  
- [Thymeleaf](https://www.thymeleaf.org/)
- [MDBootstrap](https://mdbootstrap.com/)

#### DB
- [PostgreSQL](https://www.postgresql.org/)

#### Infrastructure
- [Heroku](https://www.heroku.com/)

### Why were these technologies chosen?
I have experience of Spring boot and wanted organise knowledge of it. That's why I chose Spring Boot (Data JPA, Security, Thymeleaf). I don't have plenty of knowledge of Front-End, so I chose MDBootstrap. Heroku has enough features for me to open this application so I chose it. Heroku has a [database service](https://www.heroku.com/postgres) that use PostgreSQL, so I chose PostgreSQL as database.

### Where does the application bring words and definitions from?
This application uses [Oxford Dictionaries API](https://developer.oxforddictionaries.com/).

## ER diagram
<img width="510" alt="Screenshot 2021-11-04 at 16 34 34" src="https://user-images.githubusercontent.com/45550613/140381952-b148306c-5b88-49d8-b82a-620b6006230e.png">
