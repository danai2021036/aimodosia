package gr.hua.dit.aimodotes.demo.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Aimodotis {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    @Column
    private String fname;

    @Column
    private String lname;

    @Column
    private String email;

    @Column
    private Integer phone;

    @Column
    private Integer AMKA;

    @Column
    private Character sex;

    @Column
    private Date last_donation;

    @Column
    private Integer age;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="app_form_id")
    private AppForm appForm;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="blood_test_id")
    private BloodTest bloodTest;

    @ManyToMany(cascade = { CascadeType.MERGE,
                            //CascadeType.PERSIST,
                            CascadeType.REFRESH})
    @JoinTable(
            name = "donationRequest_aimodotis",
            joinColumns = @JoinColumn(name = "aimodotis_id"),
            inverseJoinColumns = @JoinColumn(name = "donation_request_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"aimodotis_id","donation_request_id"})}
    )
    private List<DonationRequest> donationRequests;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Integer getAMKA() {
        return AMKA;
    }

    public void setAMKA(Integer AMKA) {
        this.AMKA = AMKA;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    public Date getLast_donation() {
        return last_donation;
    }

    public void setLast_donation(Date last_donation) {
        this.last_donation = last_donation;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Aimodotis(Integer id, String fname, String lname, String email, Integer phone, Integer AMKA, Character sex, Date last_donation, Integer age) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
        this.AMKA = AMKA;
        this.sex = sex;
        this.last_donation = last_donation;
        this.age = age;
    }
    public Aimodotis() {
    }


}
