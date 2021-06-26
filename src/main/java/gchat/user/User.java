package gchat.user;

import gchat.group.ChatGroup;
import gchat.socialprovider.SocialLogin;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_detail")
@Data
@ToString
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Size(max = 255)
  @Column(unique = true, nullable = false)
  private String username;

  @Size(min = 4)
  private String password;

  @ManyToMany(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Set<ChatGroup> chatGroups;

  @OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY)
  @JoinColumn(name="user_id")
  @ToString.Exclude
  private Set<SocialLogin> socialLogins;

  @ElementCollection(fetch = FetchType.EAGER)
  List<Role> roles;

  public User(){}

  public User(String username, String password){
    this.username = username;
    this.password = password;
  }

  @CreationTimestamp
  private LocalDateTime createDateTime;

  @UpdateTimestamp
  private LocalDateTime updateDateTime;

}
