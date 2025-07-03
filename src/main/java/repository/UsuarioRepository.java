/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.List;
import models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projection.DataUserProjection;
import projection.usuariobyrolProjection;

/**
 *
 * @author Samuel
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = "select\n"
            + "	id,\n"
            + "	nombre,\n"
            + "	rol,\n"
            + "	correo\n"
            + "from\n"
            + "	usuarios u\n"
            + "where\n"
            + "	u.correo = :usuario\n"
            + "	and u.contrasena = :password", nativeQuery = true)
    public DataUserProjection login(@Param("usuario") String usuario, @Param("password") String password);

    Usuario findByCorreoAndContrasena(String correo, String contrasena);

    Usuario findByCorreo(String correo);

    @Query(value = "select nombre || ' ' || apellido as usuario,\n"
            + "	r.id as idRol\n"
            + "from usuarios u\n"
            + "inner join roles r on\n"
            + "u.id_rol = r.id\n"
            + "where r.rol = :rol", nativeQuery = true)
    public List<usuariobyrolProjection> usuariobyRol(@Param("rol")String rol);
    

}
