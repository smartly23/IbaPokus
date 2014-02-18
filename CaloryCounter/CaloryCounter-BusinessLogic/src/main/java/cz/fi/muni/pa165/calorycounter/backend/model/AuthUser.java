package cz.fi.muni.pa165.calorycounter.backend.model;

import cz.fi.muni.pa165.calorycounter.serviceapi.dto.UserRole;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.WeightCategory;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * This entity represents registered, logged and authenticated user.
 *
 * @author Martin Pasko (smartly23)
 */
@Entity
public class AuthUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    private String name;
    @Column(columnDefinition = "VARCHAR(6)")
    private String gender;
    private int age;
    @Column(unique = true, columnDefinition = "varchar(50)", nullable = false)
    private String username;
    @Column(columnDefinition = "varchar(200)")
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @Enumerated(EnumType.STRING)
    private WeightCategory weightCat;
    @OneToMany(mappedBy = "authUser", cascade = {CascadeType.REMOVE})
    private List<ActivityRecord> records;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public WeightCategory getWeightCat() {
        return weightCat;
    }

    public void setWeightCat(WeightCategory weightCat) {
        this.weightCat = weightCat;
    }

    public List<ActivityRecord> getRecords() {
        return records;
    }

    public void setRecords(List<ActivityRecord> records) {
        this.records = records;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AuthUser other = (AuthUser) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AuthUser{ name= " + name + ", gender= " + gender + ", age= " + age + " }";
    }
}
