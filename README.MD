# 👨‍💻To-do list

A api foi desenvolvida com o intuito de exercitar meus conhecimentos em Java e Spring Boot.

O projeto trata-se de um gerenciador de tarefas, onde é possível realizar as 4 operações do CRUD.

## 💿Como rodar o projeto
**Pré-requisitos**: Git e Java 17

``` shell
$ git clone https://github.com/lleonardus/todo-list.git
$ cd todo-list
$ ./mvnw spring-boot:run
```

Após esse processo, a api vai estar rodando em **http://localhost:8080** e o banco de dados (H2 database)
pode ser acessado em **http://localhost:8080/h2-console/**

## 🎯Endpoints

### 📜Tasks

- GET - `/tasks`: Exibe todas as tarefas registradas.
- GET - `/tasks/title/{title}`: Exibe todas as tarefas com um título específico.
- GET - `/tasks/id/{id}`: Exibe uma tarefa em específico.
- POST - `/tasks`: Cria uma nova tarefa.
- PUT - `/tasks/id/{id}`: Atualiza os campos title, description e deadline de uma tarefa em específico.
- PUT - `/tasks/id/{id}/status/{status}`: Atualiza o status de uma tarefa em específico. Os possíveis valores de um status são: to-do, doing e done.
- DELETE - `/tasks/id/{id}`: Deleta uma tarefa em específico.

### Exemplo de JSON para criar uma tarefa

```json
{
    "title": "Task 1",
    "description": "Creating new Task",
    "deadline": "2022-10-06"
}
```

### Exemplo de JSON para atualizar uma tarefa

```json
{
    "title": "Update",
    "description": "Updating Task",
    "deadline": "2022-11-06"
}
```

## 🧰Ferramentas Utilizadas

1. Java 17
2. Spring Boot
3. Spring Web
4. Spring Data JPA
5. H2 database
6. Spring Validation
7. Docker
8. Lombok
9. Model Mapper