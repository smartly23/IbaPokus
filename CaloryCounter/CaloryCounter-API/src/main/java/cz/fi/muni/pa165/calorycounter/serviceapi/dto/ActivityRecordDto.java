package cz.fi.muni.pa165.calorycounter.serviceapi.dto;

import java.util.Date;
import java.util.Objects;

/**
 * DTO for ActivityRecord entity.
 *
 * @author Martin Pasko (smartly23)
 */
public class ActivityRecordDto {

    private Long activityRecordId;
    private Long userId;
    private String activityName;
    private WeightCategory weightCategory;
    private int duration;
    private Date activityDate;
    private int caloriesBurnt;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getActivityRecordId() {
        return activityRecordId;
    }

    public void setActivityRecordId(Long activityId) {
        this.activityRecordId = activityId;
    }

    public WeightCategory getWeightCategory() {
        return weightCategory;
    }

    public void setWeightCategory(WeightCategory weightCategory) {
        this.weightCategory = weightCategory;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public int getCaloriesBurnt() {
        return caloriesBurnt;
    }

    public void setCaloriesBurnt(int caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.activityRecordId) + Objects.hashCode(this.userId);
        return hash;
    }

    // two DTOs are equal, if both userId and activityRecordId are equal
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ActivityRecordDto other = (ActivityRecordDto) obj;
        if (!Objects.equals(this.activityRecordId, other.activityRecordId)) {   // if both null, then true
            return false;
        }
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ActivityRecordDto{" + "activityRecordId=" + activityRecordId + ", userId=" + userId
                + ", activityName=" + activityName + ", weightCatNum=" + weightCategory + ", duration="
                + duration + ", activityDate=" + activityDate + ", caloriesBurnt=" + caloriesBurnt + '}';
    }
}
