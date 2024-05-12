/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.smoothpark;

import java.sql.Connection;
import java.sql.SQLException;

public class SmoothPark {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = conexionDB.getConnection();
            System.out.println("¡Conexión establecida correctamente!");
            
            // Crear un nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre("Pancracio");
            nuevoUsuario.setApellido("Pecera");
            nuevoUsuario.setTipoDocumento("CC");
            nuevoUsuario.setNumeroDocumento("123456789");
            nuevoUsuario.setCorreo("Pancracio@gmail.com");
            nuevoUsuario.setContrasena("000");
            nuevoUsuario.setTelefono("1322122");
            nuevoUsuario.setTipoUsuario("Admin");
            
            // Registrar el nuevo usuario y obtener su ID
            int usuarioID = UsuarioDAO.insertarUsuario(nuevoUsuario);
            
            System.out.println("¡Usuario registrado correctamente!"); // Mensaje agregado
            
            // Crear un nuevo vehículo
            Vehiculo nuevoVehiculo = new Vehiculo();
            nuevoVehiculo.setUsuarioID(usuarioID); // Asignar el usuarioID del nuevo usuario
            nuevoVehiculo.setMarca("Pancraciomovil");
            nuevoVehiculo.setModelo("Corolla");
            nuevoVehiculo.setPlaca("sff545");
            
            // Registrar el nuevo vehículo
            VehiculoDAO.insertarVehiculo(nuevoVehiculo);
            
            System.out.println("¡Vehículo registrado correctamente!");
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
