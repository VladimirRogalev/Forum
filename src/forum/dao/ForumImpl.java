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

	@Override
	public boolean addPost(Post post) {
		if (post == null || getPostById(post.getPostId()) != null) {
			return false;
		}
		if (posts.length == size) {
			posts = Arrays.copyOf(posts, posts.length * 2);
		}

		int index = Arrays.binarySearch(posts, 0, size, post, comparator);
		index = index >= 0 ? index : -index - 1;
		System.arraycopy(posts, index, posts, index + 1, size - index);
		posts[index] = post;

		index = Arrays.binarySearch(postsByTitle, 0, size, post, comparator);
		index = index >= 0 ? index : -index - 1;
		System.arraycopy(postsByTitle, index, postsByTitle, index + 1, size - index);
		postsByTitle[index] = post;
		size++;
		return true;

	}

	@Override
	public boolean removePost(int postId) {
		int index = searchById(postId);
		if (index < 0) {
			return false;
		}
		System.arraycopy(posts, index + 1, posts, index, size - index - 1);
		posts = Arrays.copyOf(posts, posts.length - 1);

		for (int i = 0; i < size; i++) {
			if (posts[i].getPostId() == postId) {
				index = i;
			}
		}

		System.arraycopy(postsByTitle, index + 1, postsByTitle, index, size - index - 1);
		posts = Arrays.copyOf(postsByTitle, postsByTitle.length - 1);

		size--;
		return true;
	}

	@Override
	public boolean updatePost(int postId, String content) {
		int index = searchById(postId);
		if (index < 0) {
			return false;
		}
		posts[index].setContent(content);
		return true;
	}

	@Override
	public Post getPostById(int postId) {
		int index = searchById(postId);
		return index < 0 ? null : posts[index];
	}

	private int searchById(int postId) {
		for (int i = 0; i < size; i++) {
			if (posts[i].getPostId() == postId) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Post[] getPostsByAuthor(String author) {
		Post pattern = new Post(Integer.MIN_VALUE, null, author, null);
		pattern.setDate(LocalDateTime.MIN);
		int from = -Arrays.binarySearch(posts, 0, size, pattern, comparator) - 1;
		pattern = new Post(Integer.MAX_VALUE, null, author, null);
		pattern.setDate(LocalDateTime.MAX);
		int to = -Arrays.binarySearch(posts, 0, size, pattern, comparator) - 1;

		return Arrays.copyOfRange(posts, from, to);

	}

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

	@Override
	public Post[] getPostsByTitle(String title) {
		Post pattern = new Post(Integer.MIN_VALUE, title, null, null);
		pattern.setDate(LocalDateTime.MIN);
		int from = -Arrays.binarySearch(postsByTitle, 0, size, pattern, comparator) - 1;
		pattern = new Post(Integer.MAX_VALUE, title, null, null);
		pattern.setDate(LocalDateTime.MAX);
		int to = -Arrays.binarySearch(postsByTitle, 0, size, pattern, comparator) - 1;

		return Arrays.copyOfRange(postsByTitle, from, to);

	}

	@Override
	public Post[] getPostsByTitle(String title, LocalDate dateFrom, LocalDate dateTo) {
		Post pattern = new Post(Integer.MIN_VALUE, title, null, null);
		pattern.setDate(dateFrom.atStartOfDay());
		int from = -Arrays.binarySearch(postsByTitle, 0, size, pattern, comparator) - 1;
		pattern = new Post(Integer.MAX_VALUE, title, null, null);
		pattern.setDate(LocalDateTime.of(dateTo, LocalTime.MAX));
		int to = -Arrays.binarySearch(postsByTitle, 0, size, pattern, comparator) - 1;


		return Arrays.copyOfRange(postsByTitle, from, to);
	}
	

	@Override
	public int size() {

		return size;
	}

	@Override
	public void printPosts() {
		for (int i = 0; i < posts.length; i++) {
			System.out.println(posts[i]);

		}
		System.out.println();

	}

}
