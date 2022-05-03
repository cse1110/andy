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

    private final Scoring scoring = mock(Scoring.class);

    private final UserRepository repo = mock(UserRepository.class);

    @Test
    void upvoteFeaturedPosts() {
        StackOverflow so = new StackOverflow(repo, scoring);
        User author = new User("Mauricio", 100);
        Post featuredPost = new Post(author, "post 1", true);
        when(scoring.pointsForFeaturedPost()).thenReturn(50);
        so.upvote(featuredPost, mock(User.class));
        assertThat(author.getPoints()).isEqualTo(150);
        verify(repo).update(author);
    }

    @Test
    void upvoteRegularPosts() {
        StackOverflow so = new StackOverflow(repo, scoring);
        User author = new User("Mauricio", 100);
        Post regularPost = new Post(author, "post 1", false);
        when(scoring.pointsForNormalPost()).thenReturn(20);
        so.upvote(regularPost, mock(User.class));
//        assertThat(author.getPoints()).isEqualTo(120);
        verify(repo).update(author);
    }
}
