/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smoothpark;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuarioDAO {

    // Insertar un nuevo usuario en la base de datos y devolver su ID
    public static int insertarUsuario(Usuario usuario) throws SQLException {
        // Verificar si el correo ya está registrado
        if (existeCorreo(usuario.getCorreo())) {
            throw new SQLException("El correo ya está registrado en la base de datos.");
        }

        String sql = "INSERT INTO usuario (nombre, apellido, tipoDocumento, numeroDocumento, correo, contrasena, telefono, tipoUsuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = conexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getApellido());
            statement.setString(3, usuario.getTipoDocumento());
            statement.setString(4, usuario.getNumeroDocumento());
            statement.setString(5, usuario.getCorreo());
            statement.setString(6, usuario.getContrasena());
            statement.setString(7, usuario.getTelefono());
            statement.setString(8, usuario.getTipoUsuario());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Devolver el ID generado
                } else {
                    throw new SQLException("No se pudo obtener el ID del usuario. No se realizaron cambios en la base de datos.");
                }
            } else {
                throw new SQLException("No se pudo insertar el usuario. No se realizaron cambios en la base de datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            throw e;
        }
    }

    // Verificar si el correo ya está registrado en la base de datos
    private static boolean existeCorreo(String correo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE correo = ?";

        try (Connection connection = conexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, correo);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    // Consultar un usuario por ID
    public static Usuario consultarUsuario(int usuarioID) throws SQLException {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE usuarioID = ?";

        try (Connection connection = conexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, usuarioID);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    usuario = new Usuario();
                    usuario.setUsuarioID(resultSet.getInt("usuarioID"));
                    usuario.setNombre(resultSet.getString("nombre"));
                    usuario.setApellido(resultSet.getString("apellido"));
                    usuario.setTipoDocumento(resultSet.getString("tipoDocumento"));
                    usuario.setNumeroDocumento(resultSet.getString("numeroDocumento"));
                    usuario.setCorreo(resultSet.getString("correo"));
                    usuario.setContrasena(resultSet.getString("contrasena"));
                    usuario.setTelefono(resultSet.getString("telefono"));
                    usuario.setTipoUsuario(resultSet.getString("tipoUsuario"));
                }
            }
        }
        return usuario;
    }

    // Actualizar un usuario
    public static void actualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET nombre=?, apellido=?, tipoDocumento=?, numeroDocumento=?, correo=?, contrasena=?, telefono=?, tipoUsuario=? WHERE usuarioID=?";

        try (Connection connection = conexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getApellido());
            statement.setString(3, usuario.getTipoDocumento());
            statement.setString(4, usuario.getNumeroDocumento());
            statement.setString(5, usuario.getCorreo());
            statement.setString(6, usuario.getContrasena());
            statement.setString(7, usuario.getTelefono());
            statement.setString(8, usuario.getTipoUsuario());
            statement.setInt(9, usuario.getUsuarioID());

            statement.executeUpdate();
        }
    }

    // Eliminar un usuario por ID
    public static void eliminarUsuario(int usuarioID) throws SQLException {
        // Verificar si hay vehículos asociados al usuario
        if (tieneVehiculos(usuarioID)) {
            throw new SQLException("No se puede eliminar el usuario porque tiene vehículos asociados.");
        }

        String sql = "DELETE FROM usuario WHERE usuarioID=?";

        try (Connection connection = conexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, usuarioID);

            statement.executeUpdate();
        }
    }

    // Verificar si el usuario tiene vehículos asociados
    private static boolean tieneVehiculos(int usuarioID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM vehiculo WHERE usuarioID = ?";

        try (Connection connection = conexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, usuarioID);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
