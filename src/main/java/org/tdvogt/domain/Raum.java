package org.tdvogt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Raum.
 */
@Entity
@Table(name = "raum")
public class Raum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "gebaeude")
    private String gebaeude;

    @Column(name = "raumnummer")
    private Long raumnummer;

    @Column(name = "etage")
    private Long etage;

    @Column(name = "zusatz")
    private String zusatz;

    @OneToMany(mappedBy = "raum")
    @JsonIgnoreProperties(value = { "raum" }, allowSetters = true)
    private Set<Akte> aktes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Raum id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGebaeude() {
        return this.gebaeude;
    }

    public Raum gebaeude(String gebaeude) {
        this.setGebaeude(gebaeude);
        return this;
    }

    public void setGebaeude(String gebaeude) {
        this.gebaeude = gebaeude;
    }

    public Long getRaumnummer() {
        return this.raumnummer;
    }

    public Raum raumnummer(Long raumnummer) {
        this.setRaumnummer(raumnummer);
        return this;
    }

    public void setRaumnummer(Long raumnummer) {
        this.raumnummer = raumnummer;
    }

    public Long getEtage() {
        return this.etage;
    }

    public Raum etage(Long etage) {
        this.setEtage(etage);
        return this;
    }

    public void setEtage(Long etage) {
        this.etage = etage;
    }

    public String getZusatz() {
        return this.zusatz;
    }

    public Raum zusatz(String zusatz) {
        this.setZusatz(zusatz);
        return this;
    }

    public void setZusatz(String zusatz) {
        this.zusatz = zusatz;
    }

    public Set<Akte> getAktes() {
        return this.aktes;
    }

    public void setAktes(Set<Akte> aktes) {
        if (this.aktes != null) {
            this.aktes.forEach(i -> i.setRaum(null));
        }
        if (aktes != null) {
            aktes.forEach(i -> i.setRaum(this));
        }
        this.aktes = aktes;
    }

    public Raum aktes(Set<Akte> aktes) {
        this.setAktes(aktes);
        return this;
    }

    public Raum addAkte(Akte akte) {
        this.aktes.add(akte);
        akte.setRaum(this);
        return this;
    }

    public Raum removeAkte(Akte akte) {
        this.aktes.remove(akte);
        akte.setRaum(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Raum)) {
            return false;
        }
        return id != null && id.equals(((Raum) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Raum{" +
            "id=" + getId() +
            ", gebaeude='" + getGebaeude() + "'" +
            ", raumnummer=" + getRaumnummer() +
            ", etage=" + getEtage() +
            ", zusatz='" + getZusatz() + "'" +
            "}";
    }

    public String getGesamtStandort() {
        String geb = this.gebaeude != null ? this.gebaeude : "";
        String rn = this.raumnummer != null ? this.raumnummer < 10L ? "0" + this.raumnummer : this.raumnummer.toString() : "";
        String et = this.etage != null ? this.etage.toString() : "";
        String zu = this.zusatz != null ? this.zusatz : "";
        return geb + ", " + zu + et + rn;
    }
}
