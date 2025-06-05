/*
    * @author: Derek Rojas Mendoza
    * @author: Joseph León Cabezas
*/

package com.example.backend.dao

import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

@Repository
open class Dao {
    protected var conexion: Connection? = null;

    @Throws(SQLException::class, ClassNotFoundException::class)
    protected fun conectar() {
        Class.forName("oracle.jdbc.driver.OracleDriver")
        conexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "root")
    }

    @Throws(SQLException::class)
    protected fun desconectar() {
        if (conexion?.isClosed == false) {
            conexion?.close()
        }
    }
}