package com.mycompany.pharma.model;

public class Session {

    private static String rol; // "admin" o "user"
    private static String nombre;

    public static String getRol() {
        return rol;
    }

    public static String getNombre() {
        return nombre;
    }

    public static void setRol(String r) {
        rol = r;
    }

    public static void setNombre(String nombre) {
        Session.nombre = nombre;
    }
    
    
}

