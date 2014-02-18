package cz.fi.muni.pa165.calorycounter.backend.model;

import cz.fi.muni.pa165.calorycounter.serviceapi.dto.WeightCategory;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

/**
 * This entity represents activity with calories for specific weight category.
 *
 * @author Jak Kucera (Greld)
 */
@Entity
@IdClass(value = CaloriesPK.class)
public class Calories implements Serializable {

    private int amount;
    @Id
    @Enumerated(EnumType.STRING)
    private WeightCategory weightCat;
    @Id
    @ManyToOne(optional = false)
    private Activity activity;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public WeightCategory getWeightCat() {
        return weightCat;
    }

    public void setWeightCat(WeightCategory weightCat) {
        this.weightCat = weightCat;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.weightCat);
        hash = 73 * hash + Objects.hashCode(this.activity);
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
        final Calories other = (Calories) obj;
        if (this.weightCat != other.weightCat) {
            return false;
        }
        if (!Objects.equals(this.activity, other.activity)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Calories{ amount=" + amount + ", weightCat=" + weightCat + ", activity=" + activity + '}';
    }
}
