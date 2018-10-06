package com.twparser.model;


import com.sun.istack.internal.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Represents User entity
 */

@Getter
@Setter
@Entity
@Table(name = "USER")
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    public User(String profileUrl, boolean isProfilePublic, int numberofFollowers, int numberOfPosts) {
        this.profileUrl = profileUrl;
        this.isProfilePublic = isProfilePublic;
        this.numberofFollowers = numberofFollowers;
        this.numberOfPosts = numberOfPosts;
    }

    /** Unique User id and PK for db. */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;

    /** Identifier of the user in Twitter. */
    @Column(name = "profile_url")
    private String profileUrl;

    /** Tells if profile open or private. */
    @Column(name = "is_public")
    private boolean isProfilePublic;

    /** Total number of user's followers. */
    @Column(name = "followers")
    private int numberofFollowers;

    /** Total number of user's posts. */
    @Column(name = "number_of_posts")
    private int numberOfPosts;

    @Override
    public String toString() {
        return profileUrl.toString();
    }
}
