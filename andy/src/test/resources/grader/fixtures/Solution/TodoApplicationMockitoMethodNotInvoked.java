package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;

class TodoApplicationTest {

    private TodoApplication todoApp;

    private PersonService personServiceMock;

    private TodoService todoServiceMock;

    private final Long userId = 42L;

    private final String username = "tony-stark";

    @BeforeEach
    void setup() {
        personServiceMock = Mockito.mock(PersonService.class);
        todoServiceMock = Mockito.mock(TodoService.class);
        todoApp = new TodoApplication(todoServiceMock, personServiceMock);
    }

    // error in test: instead of completeToDo(), addToDo() should be invoked.
    @Test
    void addTodoTest() {
        String todo = "Find all the infinity stones.";
        Mockito.when(personServiceMock.findUsernameById(anyLong())).thenReturn(username);
        Mockito.when(todoServiceMock.addTodo(anyString(), anyString())).thenReturn(true);
        assertThat(todoApp.addTodo(userId, todo)).isTrue();
        Mockito.verify(personServiceMock).findUsernameById(userId);
        Mockito.verify(todoServiceMock).completeTodo(todo);
//        Mockito.verify(todoServiceMock).addTodo(username, todo);
    }

    @Test
    void retrieveTodosTest() {
        List<String> todos = List.of("Find all the infinity stones.", "Snap your fingers.", "Save the world.");
        Mockito.when(personServiceMock.findUsernameById(anyLong())).thenReturn(username);
        Mockito.when(todoServiceMock.retrieveTodos(anyString())).thenReturn(todos);
        assertThat(todoApp.retrieveTodos(userId, "the")).containsExactlyInAnyOrder("Find all the infinity stones.",
                "Save the world.");
        Mockito.verify(personServiceMock).findUsernameById(userId);
        Mockito.verify(todoServiceMock).retrieveTodos(username);
    }

    @Test
    void completeAllTodosTest() {
        List<String> todos = List.of("Find all the infinity stones.", "Snap your fingers.", "Save the world.");
        Mockito.when(personServiceMock.findUsernameById(anyLong())).thenReturn(username);
        Mockito.when(todoServiceMock.retrieveTodos(anyString())).thenReturn(todos);
        Mockito.doNothing().when(todoServiceMock).completeTodo(anyString());
        todoApp.completeAllTodos(userId);
        Mockito.verify(personServiceMock).findUsernameById(userId);
        Mockito.verify(todoServiceMock).retrieveTodos(username);
        Mockito.verify(todoServiceMock).completeTodo(todos.get(0));
        Mockito.verify(todoServiceMock).completeTodo(todos.get(1));
        Mockito.verify(todoServiceMock).completeTodo(todos.get(2));
    }
}
