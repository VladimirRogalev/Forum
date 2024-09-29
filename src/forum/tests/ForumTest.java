package forum.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import forum.dao.Forum;
import forum.dao.ForumImpl;
import forum.model.Post;

class ForumTest {

	Forum forum;
	Post[] posts = new Post[6];
	Comparator<Post> comparator = (o1, o2) -> Integer.compare(o1.getPostId(), o2.getPostId());

	@BeforeEach
	void setUp() throws Exception {
		forum = new ForumImpl();
		posts[0] = new Post(0, "title1", "author1", "content");
		posts[1] = new Post(1, "title2", "author2", "content");
		posts[2] = new Post(2, "title3", "author3", "content");
		posts[3] = new Post(3, "title3", "author2", "content");
		posts[4] = new Post(4, "title1", "author1", "content");
		posts[5] = new Post(5, "title1", "author1", "content");
		for (int i = 0; i < posts.length - 1; i++) {
			forum.addPost(posts[i]);
		}

	}

	@Test
	void testAddPost() {
		assertFalse(forum.addPost(null));
		assertTrue(forum.addPost(posts[5]));
		assertEquals(6, forum.size());
		assertFalse(forum.addPost(posts[4]));
		assertEquals(6, forum.size());

	}

	@Test
	void testRemovePost() {
		assertTrue(forum.removePost(4));
		assertEquals(4, forum.size());
		assertFalse(forum.removePost(4));
		assertEquals(4, forum.size());
	}

	@Test
	void testUpdatePost() {
		assertTrue(forum.updatePost(2, "new cont"));
		assertEquals("new cont", forum.getPostById(2).getContent());
	}

	@Test
	void testGetPostById() {
		assertEquals(posts[1], forum.getPostById(1));
		assertNull(forum.getPostById(5));

	}

	@Test
	void testGetPostsByAuthorString() {
		Post[] actual = forum.getPostsByAuthor("author2");
		Arrays.sort(actual, comparator);
		Post[] expected = { posts[1], posts[3] };
		assertArrayEquals(expected, actual);
		actual = forum.getPostsByAuthor("author150");
		expected = new Post[0];
		assertArrayEquals(expected, actual);
	}

	@Test
	void testGetPostsByAuthorStringLocalDateLocalDate() {
		posts[0].setDate(LocalDateTime.now().minusDays(7));
		posts[1].setDate(LocalDateTime.now().minusDays(4));
		posts[2].setDate(LocalDateTime.now().minusDays(9));
		posts[3].setDate(LocalDateTime.now().minusDays(1));
		posts[4].setDate(LocalDateTime.now().minusDays(15));
		posts[5].setDate(LocalDateTime.now().minusDays(10));
		forum = new ForumImpl();
		for (int i = 0; i < posts.length; i++) {
			forum.addPost(posts[i]);
		}
		Post[] actual = forum.getPostsByAuthor("author2", LocalDate.now().minusDays(5), LocalDate.now().minusDays(2));
		Arrays.sort(actual, comparator);
		Post[] expected = { posts[1]};
		assertArrayEquals(expected, actual);
		forum.printPosts();
		
	}
	


	@Test
	void testSize() {
		assertEquals(5, forum.size());
	}

}
