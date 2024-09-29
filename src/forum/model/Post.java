package forum.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
	private int postId;
	private String title;
	private String author;
	private String content;
	private LocalDateTime date;
	private int likes;
	
	public Post(int postId, String title, String author, String content) {
		this.postId = postId;
		this.title = title;
		this.author = author;
		this.content = content;
		date = LocalDateTime.now();

	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public int getPostId() {
		return postId;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public int getLikes() {
		return likes;
	}
	public int addLike() {
		return ++likes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(postId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		return postId == other.postId;
	}

	@Override
	public String toString() {
		return "Post [postId=" + postId + ", title=" + title + ", author=" + author + ", content=" + content + ", date="
				+ date + ", likes=" + likes + "]";
	}
	

	
	
	
	
	
	
	

}
