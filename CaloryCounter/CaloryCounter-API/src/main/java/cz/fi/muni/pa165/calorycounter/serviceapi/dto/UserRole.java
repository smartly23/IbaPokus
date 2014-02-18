/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pa165.calorycounter.serviceapi.dto;

/**
 *
 * @author Martin Bryndza
 */
public enum UserRole {

    USER,
    ADMIN;

    private UserRole() {
    }

    public static UserRole getUserRole(int index) {
        switch (index) {
            case 1:
                return ADMIN;
            default:
                return USER;
        }
    }

    public static int getIndex(UserRole userRole) {
        switch (userRole) {
            case ADMIN:
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return this.name();
    }
}
