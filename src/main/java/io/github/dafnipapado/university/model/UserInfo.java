package io.github.dafnipapado.university.model;

import io.github.dafnipapado.university.model.static_data.Region;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(unique = true, nullable = false)
    private String afm;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String telephone;

    @Column
    private String zipcode;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id")
    private Region region;

    public void addUser(User user){
        this.user = user;
        user.setUserInfo(this);
    }

//    @Override
//    public final boolean equals(Object o) {
//        if (!(o instanceof UserInfo userInfo)) return false;
//        return getId().equals(userInfo.getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return getId().hashCode();
//    }
}
