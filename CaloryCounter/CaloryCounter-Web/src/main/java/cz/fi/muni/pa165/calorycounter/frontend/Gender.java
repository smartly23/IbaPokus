package cz.fi.muni.pa165.calorycounter.frontend;

public enum Gender {

    Male,
    Female,
    Other;

    private Gender() {
    }

    public String getName() {
        return this.name();
    }

    @Override
    public String toString() {
        return this.name();
    }
}
