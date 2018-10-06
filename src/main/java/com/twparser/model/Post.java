package com.twparser.model;

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

    /** Total number of likes for this post. */
    @Column(name = "number_of_likes")
    private long numberofLikes;

    /** Total number of retwits for this post. */
    @Column(name = "number_of_retwits")
    private long numberOfRetwits;

    /** Total number of comments for this post. */
    @Column(name = "number_of_comments")
    private long numberOfComments;

    /** URL of an image related to post. */
    @Column(name = "image_url")
    private String imageUrl;

    /** Date the post has been posted. */
    @Column(name = "post_date")
    private LocalDateTime postDate;
}
