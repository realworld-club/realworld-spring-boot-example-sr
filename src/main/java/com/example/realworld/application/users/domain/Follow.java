package com.example.realworld.application.users.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @OneToMany
    @JoinTable(name = "TB_FOLLOWER",
            joinColumns = @JoinColumn(name = "FROM_USER"),
            inverseJoinColumns = @JoinColumn(name = "TO_USER")
    )
    @ToString.Exclude
    private Set<User> followers = new HashSet<>();

    public void addFollower(User toUser) {
        followers.add(toUser);
    }

    public boolean isFollowing(User toUser) {
        return followers.contains(toUser);
    }

    public void unFollow(User toUser) {
        followers.remove(toUser);
    }
}