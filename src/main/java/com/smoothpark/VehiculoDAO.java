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

public class VehiculoDAO {

    // Insertar un nuevo vehículo en la base de datos y devolver su ID
    public static int insertarVehiculo(Vehiculo vehiculo) throws SQLException {
        String sql = "INSERT INTO vehiculo (placa, marca, modelo, usuarioID) VALUES (?, ?, ?, ?)";

        try (Connection connection = conexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, vehiculo.getPlaca());
            statement.setString(2, vehiculo.getMarca());
            statement.setString(3, vehiculo.getModelo());
            statement.setInt(4, vehiculo.getUsuarioID());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Devolver el ID generado
                } else {
                    throw new SQLException("No se pudo obtener el ID del vehículo. No se realizaron cambios en la base de datos.");
                }
            } else {
                throw new SQLException("No se pudo insertar el vehículo. No se realizaron cambios en la base de datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar vehículo: " + e.getMessage());
            throw e;
        }
    }

    // Eliminar un vehículo por ID
    public static void eliminarVehiculo(int vehiculoID) throws SQLException {
        String sql = "DELETE FROM vehiculo WHERE vehiculoID=?";

        try (Connection connection = conexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, vehiculoID);

            statement.executeUpdate();
        }
    }

    // Verificar si hay vehículos asociados a un usuario
    public static boolean tieneVehiculos(int usuarioID) throws SQLException {
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
