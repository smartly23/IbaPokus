package cz.fi.muni.pa165.calorycounter.backend.dto.convert;

import cz.fi.muni.pa165.calorycounter.serviceapi.dto.ActivityDto;
import cz.fi.muni.pa165.calorycounter.backend.model.Activity;
import cz.fi.muni.pa165.calorycounter.backend.model.Calories;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.WeightCategory;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Conversion between ActivityRecord DTO and entity back and forth.
 *
 * @author Martin Bryndza (martin-bryndza)
 */
@Component
public class ActivityConvert {

    final static Logger log = LoggerFactory.getLogger(ActivityConvert.class);

    public List<Calories> fromDtoToEntitiesList(ActivityDto dto) {
        List<Calories> cals = new LinkedList<>();
        for (WeightCategory wc : WeightCategory.values()) {
            cals.add(fromDtoToEntity(dto, wc));
        }
        return cals;
    }

    public Calories fromDtoToEntity(ActivityDto dto, WeightCategory category) {
        Calories cal = new Calories();
        Activity activity = new Activity();
        activity.setName(dto.getActivityName());
        activity.setDeleted(dto.isDeleted());
        activity.setId(dto.getActivityId());
        cal.setActivity(activity);
        cal.setAmount(dto.getCaloriesAmount(category));
        cal.setWeightCat(category);
        return cal;
    }

    public ActivityDto fromEntitiesListToDto(List<Calories> entities) {
        ActivityDto dto = new ActivityDto();
        if (entities.isEmpty()) {
            log.warn("List of Calories passed in argument is empty. Nothing to convert fromEntitiesListToDto");
            return dto;
        }
        Activity activity = entities.iterator().next().getActivity();
        dto.setActivityName(activity.getName());
        dto.setDeleted(activity.isDeleted());
        dto.setActivityId(activity.getId());
        for (Calories cal : entities) {
            if (!cal.getActivity().getName().equals(dto.getActivityName())) {
                throw new IllegalArgumentException("Calories given are not of the same Activity. Expected " + dto.getActivityName() + "; Current " + cal.getActivity().getName());
            }
            dto.setCaloriesAmount(cal.getWeightCat(), cal.getAmount());
        }
        return dto;
    }
}
