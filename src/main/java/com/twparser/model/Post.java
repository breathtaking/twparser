package com.twparser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents Post entity
 */

@Getter
@Setter
@Entity
@Table(name = "POST")
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    /** Unique post id and PK in db. */
    @Id @GeneratedValue
    @Column(name = "post_id")
    private String postId;

    /** User id this post is relates to. */
    @Column(name = "user_id")
    private String userId;

    /** Link to the post in Twitter. */
    @Column(name = "post_url")
    private String postUrl;

    /** Post title. */
    @Column(name = "post_title")
    private String postTitle;

    /** Total number of likes for this post. */
    @Column(name = "number_of_likes")
    private int numberofLikes;

    /** Total number of retwits for this post. */
    @Column(name = "number_of_retwits")
    private int numberOfRetwits;

    /** Total number of comments for this post. */
    @Column(name = "number_of_comments")
    private int numberOfComments;

    /** URL of an image related to post. */
    @Column(name = "image_url")
    private String imageUrl;

    /** Date the post has been posted. */
    @Column(name = "post_date")
    private LocalDateTime postDate;
}
