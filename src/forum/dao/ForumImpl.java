package forum.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Comparator;

import forum.model.Post;

public class ForumImpl implements Forum {
	private static final int INITIAL_CAPACITY = 10;

	private static Comparator<Post> comparator = (o1, o2) -> {
		int res = o1.getAuthor().compareTo(o2.getAuthor());
		res = res != 0 ? res : o1.getDate().compareTo(o2.getDate());
		return res != 0 ? res : Integer.compare(o1.getPostId(), o2.getPostId());
	};

	private static Comparator<Post> comparatorByTitle = (o1, o2) -> {
		int res = o1.getTitle().compareTo(o2.getTitle());
		res = res != 0 ? res : o1.getDate().compareTo(o2.getDate());
		return res != 0 ? res : Integer.compare(o1.getPostId(), o2.getPostId());
	};
	private Post[] posts;
	private Post[] postsByTitle;
	private int size;

	public ForumImpl() {
		posts = new Post[INITIAL_CAPACITY];
		postsByTitle = new Post[INITIAL_CAPACITY];
	}

	// O(n)

	@Override
	public boolean addPost(Post post) {
		if (post == null || getPostById(post.getPostId()) != null) {
			return false;
		}
		if (posts.length == size) {
			posts = Arrays.copyOf(posts, posts.length * 2);
			postsByTitle = Arrays.copyOf(postsByTitle, postsByTitle.length * 2);
		}
		// O(log(n))
		int index = Arrays.binarySearch(posts, 0, size, post, comparator);
		index = index >= 0 ? index : -index - 1;
		System.arraycopy(posts, index, posts, index + 1, size - index);
		posts[index] = post;
		// O(log(n))
		index = Arrays.binarySearch(postsByTitle, 0, size, post, comparatorByTitle);
		index = index >= 0 ? index : -index - 1;
		// O(n)
		System.arraycopy(postsByTitle, index, postsByTitle, index + 1, size - index);
		postsByTitle[index] = post;
		size++;
		return true;

	}

// O(n)
	@Override
	public boolean removePost(int postId) {
		int index = searchById(postId);
		if (index < 0) {
			return false;
		}
		// O(n)
		System.arraycopy(posts, index + 1, posts, index, size - index - 1);
		posts = Arrays.copyOf(posts, posts.length - 1);
		// O(n)
		for (int i = 0; i < size; i++) {
			if (posts[i].getPostId() == postId) {
				index = i;
			}
		}
		// O(n)
		System.arraycopy(postsByTitle, index + 1, postsByTitle, index, size - index - 1);
		posts = Arrays.copyOf(postsByTitle, postsByTitle.length - 1);

		size--;
		return true;
	}

//O(n)
	@Override
	public boolean updatePost(int postId, String content) {
		int index = searchById(postId);
		if (index < 0) {
			return false;
		}
		posts[index].setContent(content);
		return true;
	}

	// O (n)
	@Override
	public Post getPostById(int postId) {
		int index = searchById(postId);
		return index < 0 ? null : posts[index];
	}
	// O(n)

	private int searchById(int postId) {
		for (int i = 0; i < size; i++) {
			if (posts[i].getPostId() == postId) {
				return i;
			}
		}
		return -1;
	}
	//O(n) => O(log(n)) -> O(n)
	@Override
	public Post[] getPostsByAuthor(String author) {
		Post pattern = new Post(Integer.MIN_VALUE, null, author, null);
		pattern.setDate(LocalDateTime.MIN);
		// O(log(n))
		int from = -Arrays.binarySearch(posts, 0, size, pattern, comparator) - 1;
		pattern = new Post(Integer.MAX_VALUE, null, author, null);
		pattern.setDate(LocalDateTime.MAX);
		// O(log(n))
		int to = -Arrays.binarySearch(posts, 0, size, pattern, comparator) - 1;
 // O(n)
		return Arrays.copyOfRange(posts, from, to);

	}
	//O(n) => O(log(n)) -> O(n)
	@Override
	public Post[] getPostsByAuthor(String author, LocalDate dateFrom, LocalDate dateTo) {
		Post pattern = new Post(Integer.MIN_VALUE, null, author, null);
		pattern.setDate(dateFrom.atStartOfDay());
		int from = -Arrays.binarySearch(posts, 0, size, pattern, comparator) - 1;
		pattern = new Post(Integer.MAX_VALUE, null, author, null);
		pattern.setDate(LocalDateTime.of(dateTo, LocalTime.MAX));
		int to = -Arrays.binarySearch(posts, 0, size, pattern, comparator) - 1;

		return Arrays.copyOfRange(posts, from, to);
	}
	//O(n) => O(log(n)) -> O(n)
	@Override
	public Post[] getPostsByTitle(String title) {
		Post pattern = new Post(Integer.MIN_VALUE, title, null, null);
		pattern.setDate(LocalDateTime.MIN);
		int from = -Arrays.binarySearch(postsByTitle, 0, size, pattern, comparatorByTitle) - 1;
		pattern = new Post(Integer.MAX_VALUE, title, null, null);
		pattern.setDate(LocalDateTime.MAX);
		int to = -Arrays.binarySearch(postsByTitle, 0, size, pattern, comparatorByTitle) - 1;

		return Arrays.copyOfRange(postsByTitle, from, to);

	}
	//O(n) => O(log(n)) -> O(n)
	@Override
	public Post[] getPostsByTitle(String title, LocalDate dateFrom, LocalDate dateTo) {
		Post pattern = new Post(Integer.MIN_VALUE, title, null, null);
		pattern.setDate(dateFrom.atStartOfDay());
		int from = -Arrays.binarySearch(postsByTitle, 0, size, pattern, comparatorByTitle) - 1;
		pattern = new Post(Integer.MAX_VALUE, title, null, null);
		pattern.setDate(LocalDateTime.of(dateTo, LocalTime.MAX));
		int to = -Arrays.binarySearch(postsByTitle, 0, size, pattern, comparatorByTitle) - 1;

		return Arrays.copyOfRange(postsByTitle, from, to);
	}
	// O(1)
	@Override
	public int size() {

		return size;
	}
 // O(n)
	@Override
	public void printPosts() {
		for (int i = 0; i < posts.length; i++) {
			System.out.println(posts[i]);

		}
		System.out.println();

	}

}
