package com.home.entities;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "ROLES")
public class Role implements GrantedAuthority {
    @Id
    @TableGenerator(name = "roleId",
            table="ID_GEN",
            pkColumnName = "GEN_NAME",
            valueColumnName = "GEN_VAL",
            pkColumnValue = "roleId",
            allocationSize = 1)
    @GeneratedValue(generator = "roleId")
    private int id;
    @Column(name = "ROLE")
    private String roleName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }

    @Override
    public String getAuthority() {
        return roleName;
    }
}
