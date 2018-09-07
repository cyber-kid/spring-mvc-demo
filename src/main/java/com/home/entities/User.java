package com.home.entities;

import com.home.dto.UserInfoDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USERS")
public class User implements UserDetails {
    @Id
    @TableGenerator(name = "userId",
            table="ID_GEN",
            pkColumnName = "GEN_NAME",
            valueColumnName = "GEN_VAL",
            pkColumnValue = "userId",
            allocationSize = 1)
    @GeneratedValue(generator = "userId")
    private int id;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "ENCRYPTED_PASSWORD")
    private String encryptedPasswd;
    @Column(name = "GENDER")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "ENABLED")
    private int enabled;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USERS_ROLES",
        joinColumns = {@JoinColumn(name = "USER_ID")},
        inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
    private List<Role> roles;
    @Column(name = "DOB")
    @Temporal(value = TemporalType.DATE)
    private Date dob;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEncryptedPasswd() {
        return encryptedPasswd;
    }

    public void setEncryptedPasswd(String encryptedPasswd) {
        this.encryptedPasswd = encryptedPasswd;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return getEncryptedPasswd();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getEnabled() == 1;
    }

    public UserInfoDto convertToDto() {
        UserInfoDto userInfoDto = new UserInfoDto();

        userInfoDto.setFirstName(getFirstName());
        userInfoDto.setLastName(getLastName());
        userInfoDto.setEmail(getEmail());
        userInfoDto.setDob(getDob());
        userInfoDto.setGender(getGender().getGenderDisplayName());

        return userInfoDto;
    }
}
