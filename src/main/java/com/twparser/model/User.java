package com.twparser.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents User entity
 */

@Getter
@Setter
@ToString
@Entity
@Table(name = "USER")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User implements Serializable{
    /** Unique data-user-id attribute. */
    //@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    /** Identifier of the user in Twitter. */
    @Id @Column(name = "profile_identifier")
    private String profileIdentifier;

    /** Name of the user in Twitter. */
    @Id @Column(name = "user_name")
    private String userName;

    /** Tells if profile open or private. */
    @Column(name = "is_public")
    private boolean isProfilePublic;

    /** Total number of user's followers. */
    @Column(name = "followers")
    private int numberOfFollowers;

    /** Total number of user's followings. */
    @Column(name = "followings")
    private int numberOfFollowings;

    /** Total number of user's tweets. */
    @Column(name = "tweets")
    private int tweets;

    /** Total number of user's likes. */
    @Column(name = "likes")
    private int likes;

    /** Url of profile picture. */
    @Column(name = "profile_avatar_url")
    private String profileAvatarUrl;

}
