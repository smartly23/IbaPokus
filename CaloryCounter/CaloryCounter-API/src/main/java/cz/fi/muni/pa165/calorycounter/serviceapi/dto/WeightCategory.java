package cz.fi.muni.pa165.calorycounter.serviceapi.dto;

/**
 * This entity represents weight categories by intervals for all common human
 * weights in lbs.
 *
 * @author Martin Pasko (smartly23)
 */
public enum WeightCategory {

    _130_("130-154 lbs"),
    _155_("155-179 lbs"),
    _180_("180-204 lbs"),
    _205_("205+ lbs");
    private String showedCategory;

    private WeightCategory(String showedCategory) {
        this.showedCategory = showedCategory;
    }

    public String getShowedCategory() {
        return showedCategory;
    }

    public String getName() {
        return this.name();
    }

    public static WeightCategory getCategory(int index) {
        switch (index) {
            case 0:
                return _130_;
            case 1:
                return _155_;
            case 2:
                return _180_;
            default:
                return _205_;
        }
    }

    public static int getIndex(WeightCategory cat) {
        switch (cat) {
            case _130_:
                return 0;
            case _155_:
                return 1;
            case _180_:
                return 2;
            default:
                return 3;
        }
    }

    @Override
    public String toString() {
        return showedCategory;
    }
}
