package com.twparser.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Hashtags for posts
 */

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "HASHTAG")
public class Hashtag implements Serializable {

    /** Unique auto generated id. */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** Post id. */
    @Column(name = "post_id")
    private String postId;

    /** Post id. */
    @Column(name = "hashtag")
    private String hashtag;

    /** User id this post is relates to. */
    @Column(name = "user_id")
    private String userId;
}
