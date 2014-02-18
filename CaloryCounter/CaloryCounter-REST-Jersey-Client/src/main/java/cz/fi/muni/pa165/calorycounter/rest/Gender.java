
package cz.fi.muni.pa165.calorycounter.rest;

public enum Gender {

    Male("Male"),
    Female("Female"),
    Other("Other");
    private String gender;

    private Gender(String stringedGender) {
        gender = stringedGender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return gender;
    }
}