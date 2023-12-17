package com.biblioteca.servicios;

import org.springframework.stereotype.Service;

import com.biblioteca.dao.Usuario;
import com.biblioteca.dto.UsuarioDTO;

/**
 * Servicio que implementa los metodos de la interface {@link IUsuarioToDto} 
 * y en esta clase es donde se entra al detalle de la logica de dichos métodos
 * para el paso de la entidad usuario (DAO) a usuarioDTO
 */
@Service
public class UsuarioToDtoImpl implements IUsuarioToDto {

	@Override
	public UsuarioDTO usuarioToDto(Usuario u) {
		
		UsuarioDTO dto = new UsuarioDTO();
		dto.setNombreUsuario(u.getNombreUsuario());
		dto.setApellidosUsuario(u.getApellidosUsuario());
		dto.setDniUsuario(u.getDniUsuario());
		dto.setTlfUsuario(u.getTlfUsuario());
		dto.setEmailUsuario(u.getEmailUsuario());
		dto.setClaveUsuario(u.getClaveUsuario());
		dto.setToken(u.getToken());
		dto.setExpiracionToken(u.getExpiracionToken());
		
		return dto;
	}

}
