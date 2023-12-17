package com.biblioteca.servicios;

import com.biblioteca.dao.Usuario;
import com.biblioteca.dto.UsuarioDTO;

/**
 * Interface donde se declaran los metodos que son necesarios para el paso de una entidad
 * usuario (DAO) a usuarioDTO
 */
public interface IUsuarioToDto {
	
	/**
	 * Pasa un usuario DAO en su DTO
	 * @param u El usuario a transformar
	 * @return El DTO del usuario
	 */
	public UsuarioDTO usuarioToDto(Usuario u);
}
