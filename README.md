# My Dictionary
![result2](https://user-images.githubusercontent.com/45550613/140232736-f992a95b-de53-4d42-a906-4348e881cce0.gif)

You can use this application from **[here](https://tc-mydictionary.herokuapp.com).**  
【Test user】  
Email: test@gmail.com  
Password: testuser01

## About The Project
This application mainly has two features.
1. Search Word  
You can use this feature when you find an English word you don't know. If you want to try to remember the word, you can register the word to your dictionary by pushing "REGISTER" button.

2. Word List  
You can see words you registered. If you remember a word, you can hide the word by checking "Remember?" and pushing "UPDATE" button below. Also, You can filter words by word name, date and Remember or not. You can sort words by word name and date as well. If you click "Hide" button, which is in "Definition & Example" cell, you can hide all definitions and examples. But you can see them when you do mouse-over.

### Built with
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [Thymeleaf](https://www.thymeleaf.org/)
- [MDBootstrap](https://mdbootstrap.com/)

### Why were these Frameworks chosen?
I have experience of Spring boot and wanted organise knowledge of it. That's why I chose Spring Boot (Data JPA, Security).
I don't have plenty of knowledge of Front-End, so I chose MDBootstrap.

### How to bring words and words definitions?
This application uses [Oxford Dictionaries API](https://developer.oxforddictionaries.com/).
