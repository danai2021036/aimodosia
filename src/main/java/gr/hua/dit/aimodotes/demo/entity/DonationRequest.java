package gr.hua.dit.aimodotes.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
public class DonationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate date;

    @Column
    private String location;

    @ManyToMany(cascade = {//CascadeType.DETACH,
            CascadeType.MERGE,
            //CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinTable(
            name = "donationRequest_aimodotis",
            joinColumns = @JoinColumn(name = "donation_request_id"),
            inverseJoinColumns = @JoinColumn(name = "aimodotis_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"aimodotis_id","donation_request_id"})}
    )
    private List<Aimodotis> aimodotes;

    @ManyToOne(cascade = {//CascadeType.DETACH,
            CascadeType.REFRESH,
            //CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinColumn(name = "secretary_id")
    private Secretary secretary;


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Aimodotis> getAimodotes() {
        return aimodotes;
    }

    public void setAimodotes(List<Aimodotis> aimodotes) {
        this.aimodotes = aimodotes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void addAimodotis(Aimodotis aimodotis){
        this.aimodotes.add(aimodotis);
    }

    public Secretary getSecretary() {
        return secretary;
    }

    public void setSecretary(Secretary secretary) {
        this.secretary = secretary;
    }

    public DonationRequest(String location, LocalDate date) {
        this.location = location;
        this.date = date;
    }

    public DonationRequest() {
    }
}
