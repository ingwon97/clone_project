package com.project.cloneproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
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
//  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  @JsonIgnore
  @OneToMany(mappedBy = "member", orphanRemoval = true, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  final List<Friend> friends = new ArrayList<>();


  //소셜 로그인


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

}
