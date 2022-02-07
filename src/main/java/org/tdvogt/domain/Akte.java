package org.tdvogt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Akte.
 */
@Entity
@Table(name = "akte")
public class Akte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "aktenthema")
    private String aktenthema;

    @Column(name = "organisations_einheit")
    private String organisationsEinheit;

    @Column(name = "akten_meter")
    private Long aktenMeter;

    @Column(name = "haengend")
    private Boolean haengend;

    @Column(name = "standort")
    private String standort;

    @ManyToOne
    @JsonIgnoreProperties(value = { "aktes" }, allowSetters = true)
    private Raum raum;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Akte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAktenthema() {
        return this.aktenthema;
    }

    public Akte aktenthema(String aktenthema) {
        this.setAktenthema(aktenthema);
        return this;
    }

    public void setAktenthema(String aktenthema) {
        this.aktenthema = aktenthema;
    }

    public String getOrganisationsEinheit() {
        return this.organisationsEinheit;
    }

    public Akte organisationsEinheit(String organisationsEinheit) {
        this.setOrganisationsEinheit(organisationsEinheit);
        return this;
    }

    public void setOrganisationsEinheit(String organisationsEinheit) {
        this.organisationsEinheit = organisationsEinheit;
    }

    public Long getAktenMeter() {
        if (aktenMeter == null) {
            aktenMeter = 1L;
        }
        return this.aktenMeter;
    }

    public Akte aktenMeter(Long aktenMeter) {
        this.setAktenMeter(aktenMeter);
        return this;
    }

    public void setAktenMeter(Long aktenMeter) {
        this.aktenMeter = aktenMeter;
    }

    public Boolean getHaengend() {
        return this.haengend;
    }

    public Akte haengend(Boolean haengend) {
        this.setHaengend(haengend);
        return this;
    }

    public void setHaengend(Boolean haengend) {
        this.haengend = haengend;
    }

    public String getStandort() {
        generateStandort();
        return this.standort;
    }

    public Akte standort(String standort) {
        this.setStandort(standort);
        return this;
    }

    public void setStandort(String standort) {
        generateStandort();
    }

    private void generateStandort() {
        this.standort = raum != null ? raum.getGesamtStandort() : "";
    }

    public Raum getRaum() {
        return this.raum;
    }

    public void setRaum(Raum raum) {
        this.raum = raum;
    }

    public Akte raum(Raum raum) {
        this.setRaum(raum);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Akte)) {
            return false;
        }
        return id != null && id.equals(((Akte) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Akte{" +
            "id=" + getId() +
            ", aktenthema='" + getAktenthema() + "'" +
            ", organisationsEinheit='" + getOrganisationsEinheit() + "'" +
            ", aktenMeter=" + getAktenMeter() +
            ", haengend='" + getHaengend() + "'" +
            ", standort='" + getStandort() + "'" +
            "}";
    }
}
