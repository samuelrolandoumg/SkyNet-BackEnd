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
import projection.UsuarioListarProjection;
import projection.usuarioById;
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
            + "	u.id as idUsuario,\n"
            + "	r.rol as rol\n"
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

    @Query(value = "SELECT u.id as idUsuario,\n"
            + "           u.nombre || ' ' || u.apellido as nombreTecnico,\n"
            + "           r.rol\n"
            + "    FROM usuarios u\n"
            + "    JOIN roles r ON u.id_rol = r.id\n"
            + "    WHERE r.rol = 'SUPERVISOR'", nativeQuery = true)
    public List<UsuarioListarProjection> listarSupervisores();

    @Query(value = "SELECT u.id as idUsuario,\n"
            + "   u.nombre || ' ' || u.apellido as nombreTecnico,\n"
            + "       r.rol\n"
            + "FROM usuarios u\n"
            + "JOIN roles r ON u.id_rol = r.id\n"
            + "WHERE r.rol = 'TECNICO'\n"
            + "  AND u.id_supervisor = :idSupervisor", nativeQuery = true)
    List<UsuarioListarProjection> listarTecnicosPorSupervisor(@Param("idSupervisor") Long idSupervisor);

    @Query(value = "SELECT DISTINCT r.rol\n"
            + "FROM usuarios u\n"
            + "INNER JOIN roles r ON u.id_rol = r.id\n"
            + "where r.id = :idRol", nativeQuery = true)
    public String rolById(@Param("idRol") Long idRol);

    
    @Query(value = "select id as idUsuario,\n"
            + "		u.nombre || ' ' || u.apellido as usuario\n"
            + "from usuarios u \n"
            + "where u.id = :idUsuario", nativeQuery = true)
    public usuarioById obtenerDatoUsuario(@Param("idUsuario") Long idUsuario);

}
