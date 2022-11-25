## 🎯Endpoints

### 📜Tasks

- `/tasks` - GET -- Exibe todas as tarefas registradas.
- `/tasks/title/{title}` - GET -- Exibe todas as tarefas com um título específico.
- `/tasks/id/{id}` - GET -- Exibe uma tarefa em específico.
- `/tasks` - POST -- Cria uma nova tarefa.
- `/tasks/id/{id}` - PUT -- Atualiza os campos title, description e deadLine de uma tarefa em específico.
- `/tasks/id/{id}/status/{status}` - PUT -- Atualiza o status de uma tarefa em específico. Os possíveis valores de um status são: to-do, doing e done.
- `/tasks/id/{id}` - DELETE -- Deleta uma tarefa em específico.

### Exemplo de JSON para criar uma tarefa

```json
{
    "title": "Task 1",
    "description": "Creating new Task",
    "deadLine": "2022-10-06"
}
```

### Exemplo de JSON para atualizar uma tarefa

```json
{
    "title": "Update",
    "description": "Updating Task",
    "deadLine": "2022-11-06"
}
```

## 🧰Ferramentas Utilizadas

1. Java 17
2. Spring Boot
3. Spring Web
4. Spring Data JPA
5. H2 database
6. Spring Validation
7. Lombok
8. Model Mapper
9. Intellij