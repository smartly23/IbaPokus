/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pa165.calorycounter.backend.dao;

import cz.fi.muni.pa165.calorycounter.backend.model.Activity;
import java.util.List;

/**
 * DAO interface - for operations on the persistence layer on Activity entities.
 *
 * @author Martin Bryndza (martin.bryndza)
 */
public interface ActivityDao extends Dao<Activity, Long> {

    void restore(Long id);

    Activity get(String name);

    List<Activity> getAll();

    List<Activity> getActive();

    List<Activity> getDeleted();
}
