package delft;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The service which performs operations on the Person table in the database.
 */
interface PersonService {

    String findUsernameById(Long id);
}

/** The service which performs operations on the TODO table in the database. */
interface TodoService {

    List<String> retrieveTodos(String username);

    boolean addTodo(String username, String newTodo);

    void completeTodo(String todo);
}

class TodoApplication {

    private final TodoService todoService;

    private final PersonService personService;

    public TodoApplication(TodoService todoService, PersonService personService) {
        this.todoService = todoService;
        this.personService = personService;
    }

    /**
     * Adds a todo for a user based on their id.
     *
     * @param userId
     *            the id of the
     * @param newTodo
     *            the new todo
     * @return true, if the todo was successfully added
     */
    public boolean addTodo(Long userId, String newTodo) {
        String user = personService.findUsernameById(userId);
        return todoService.addTodo(user, newTodo);
    }

    /**
     * Searches all the ToDos related to a user. Select only todos that contain the
     * keyword. It also updates the last visualization date of that todo.
     *
     * @param userId
     *            the id of the user
     * @param keyword
     *            keyword to be searched for
     * @return list of todos
     */
    public List<String> retrieveTodos(Long userId, String keyword) {
        String user = personService.findUsernameById(userId);
        List<String> allTodos = todoService.retrieveTodos(user);
        return allTodos.stream().filter(t -> t.contains(keyword)).collect(Collectors.toList());
    }

    /**
     * Completes all ToDos from a specific user.
     *
     * @param userId
     *            the id of the user
     */
    public void completeAllTodos(Long userId) {
        String user = personService.findUsernameById(userId);
        List<String> allTodos = todoService.retrieveTodos(user);
        for (String todo : allTodos) {
            todoService.completeTodo(todo);
        }
    }
}
