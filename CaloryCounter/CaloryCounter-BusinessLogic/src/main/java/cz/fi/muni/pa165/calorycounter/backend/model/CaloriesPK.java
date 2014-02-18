/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pa165.calorycounter.backend.model;

import cz.fi.muni.pa165.calorycounter.serviceapi.dto.WeightCategory;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Martin Bryndza
 */
public class CaloriesPK implements Serializable {

    private Activity activity;
    private WeightCategory weightCat;

    public CaloriesPK() {

    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public WeightCategory getWeightCat() {
        return weightCat;
    }

    public void setWeightCat(WeightCategory weightCat) {
        this.weightCat = weightCat;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.activity);
        hash = 67 * hash + Objects.hashCode(this.weightCat);
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
        final CaloriesPK other = (CaloriesPK) obj;
        if (!Objects.equals(this.activity, other.activity)) {
            return false;
        }
        if (this.weightCat != other.weightCat) {
            return false;
        }
        return true;
    }

}
