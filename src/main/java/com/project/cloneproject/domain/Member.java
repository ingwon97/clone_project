package com.project.cloneproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  @JsonIgnore
  private String password;

  private String profileImg;

  @Column(unique = true)
  private String socialId;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING) //DB갈 때 올 때 값을 String으로 변환해줘야함
  private RoleEnum role;



  // 내가 등록한 것
  @JsonIgnore
  @OneToMany(mappedBy = "member", orphanRemoval = true, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  List<Friend> friends = new ArrayList<>();

  public Member(String nickname, String username, String password, String profileImg) {
    this.nickname = nickname;
    this.username = username;
    this.password = password;
    this.profileImg = profileImg;
    this.socialId = null;
    this.role = RoleEnum.USER;
  }


  //소셜 로그인
  public Member(String username, String nickname, String password, String profileImage, String socialId) {
    this.username = username;
    this.nickname = nickname;
    this.password = password;
    this.profileImg = profileImage;
    this.role = RoleEnum.USER;
    this.socialId = socialId;
  }

  public List<Friend> getFriends() {
    return friends;
  }

  /*

  @OneToMany(mappedBy = "fromMember")
  @Column
  @JsonIgnore
  @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
  List<Friend> fromMembers = new ArrayList<>();

  @OneToMany(mappedBy = "toMember")
  List<Friend> toMembers = new ArrayList<>();*/


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Member member = (Member) o;
    return id != null && Objects.equals(id, member.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
    return passwordEncoder.matches(password, this.password);
  }
}
