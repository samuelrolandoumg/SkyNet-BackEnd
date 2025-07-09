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

    List<Usuario> findBySupervisorId(Long idSupervisor);

    @Query(value = "select\n"
            + "	u.id,\n"
            + "	u.nombre,\n"
            + "	r.rol,\n"
            + "	u.correo\n"
            + "from usuarios u \n"
            + "inner join roles r on\n"
            + "u.id_rol = r.id\n"
            + "where\n"
            + "	u.correo = :usuario\n"
            + "	and u.contrasena = :password", nativeQuery = true)
    public DataUserProjection login(@Param("usuario") String usuario, @Param("password") String password);

    Usuario findByCorreoAndContrasena(String correo, String contrasena);

    Usuario findByCorreo(String correo);

    @Query(value = "select nombre || ' ' || apellido as usuario,\n"
            + "	u.id as idUsuario\n"
            + "from usuarios u\n"
            + "inner join roles r on\n"
            + "u.id_rol = r.id\n"
            + "where r.rol = :rol", nativeQuery = true)
    public List<usuariobyrolProjection> usuariobyRol(@Param("rol") String rol);

    @Query(value = "select nombre || ' ' || apellido as usuario,\n"
            + "	u.id as idUsuario\n"
            + "from usuarios u\n"
            + "inner join roles r on\n"
            + "u.id_rol = r.id\n"
            + "where u.id_supervisor = :idSupervisor", nativeQuery = true)
    public List<usuariobyrolProjection> tecnicobySupervisor(@Param("idSupervisor") Long idSupervisor);

}
