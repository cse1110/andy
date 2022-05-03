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

class StackOverflowTest {
    @Test
    void postFeaturedTest() {

        UserRepository userRepo = spy(new UserRepository() {
            @Override
            public void update(User user) {}
        });
        Scoring score = mock(Scoring.class);

        StackOverflow sO = new StackOverflow(userRepo, score);

        User u = new User("philip", 5);
        Post p = new Post(u, "message", true);

        when(score.pointsForFeaturedPost()).thenReturn(5);

        sO.upvote(p, u);
        verify(userRepo).update(u);
        assertThat(u.getPoints()).isEqualTo(10);
    }

    @Test
    void postNotFeaturedTest() {
        UserRepository userRepo = spy(new UserRepository() {
            @Override
            public void update(User user) {}
        });
        Scoring score = mock(Scoring.class);

        StackOverflow sO = new StackOverflow(userRepo, score);

        User u = new User("philip", 5);
        Post p = new Post(u, "message", false);

        when(score.pointsForNormalPost()).thenReturn(1);

        sO.upvote(p, u);
        verify(userRepo).update(u);
        assertThat(u.getPoints()).isEqualTo(6);
    }
}