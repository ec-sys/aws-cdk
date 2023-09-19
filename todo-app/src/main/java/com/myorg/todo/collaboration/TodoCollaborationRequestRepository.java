package com.myorg.todo.collaboration;

import com.myorg.todo.person.Person;
import com.myorg.todo.todo.Todo;
import org.springframework.data.repository.CrudRepository;

public interface TodoCollaborationRequestRepository extends CrudRepository<TodoCollaborationRequest, Long> {
    TodoCollaborationRequest findByTodoAndCollaborator(Todo todo, Person person);
    TodoCollaborationRequest findByTodoIdAndCollaboratorId(Long todoId, Long collaboratorId);
}