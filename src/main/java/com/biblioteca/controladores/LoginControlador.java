package com.biblioteca.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.biblioteca.dao.Usuario;
import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.servicios.IUsuarioServicio;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Clase que ejerce de controlador de la vista de login/registro para gestionar las
 * solicitudes relacionadas con la autenticación y registro de usuarios.
 */
@Controller
public class LoginControlador {

	@Autowired
	private IUsuarioServicio usuarioServicio;

	/**
	 * Gestiona la solicitud HTTP GET para /auth/login y muestra la página de inicio de sesión
	 * @param model Modelo que se utiliza para enviar un usuarioDTO vacio a la vista.
	 * @return La vista de inicio de sesión (login.html).
	 */
	@GetMapping("/auth/login")
	public String login(Model model) {
		// Se agrega un nuevo objeto UsuarioDTO al modelo para el formulario de login
		model.addAttribute("usuarioDTO", new UsuarioDTO());
		return "login";
	}

	/**
	 * Gestiona la solicitud HTTP GET para mostrar la página de registro.
	 * @param model Modelo que se utiliza para enviar un usuarioDTO vacio a la vista.
	 * @return La vista de registro de usuario (registrar.html).
	 */
	@GetMapping("/auth/registrar")
	public String registrarGet(Model model) {
		model.addAttribute("usuarioDTO", new UsuarioDTO());
		return "registro";
	}

	/**
	 * Procesa la solicitud HTTP POST para registro de un nuevo usuario.
	 * @param  usuarioDTO El objeto UsuarioDTO que recibe en el modelo y contiene los
	 *         datos del nuevo usuario.
	 * @return La vista de inicio de sesión (login.html) si fue exitoso el registro; 
	 * 		   de lo contrario, la vista de registro de usuario (registro.html).
	 */
	@PostMapping("/auth/registrar")
	public String registrarPost(@ModelAttribute UsuarioDTO usuarioDTO, Model model) {

		UsuarioDTO nuevoUsuario = usuarioServicio.registrar(usuarioDTO);
		
		if (nuevoUsuario != null && nuevoUsuario.getDniUsuario() != null) {
			// Si el usuario y el DNI no son null es que el registro se completo correctamente
			model.addAttribute("mensajeRegistroExitoso", "Registro del nuevo usuario OK");
			return "login";
		} else {
			// Se verifica si el DNI ya existe para mostrar error personalizado en la vista
			if (usuarioDTO.getDniUsuario() == null) {
				model.addAttribute("mensajeErrorDni", "Ya existe un usuario con ese DNI");
				return "registro";
			} else {
				model.addAttribute("mensajeErrorMail", "Ya existe un usuario con ese email");
				return "registro";
			}
		}
	}

	/**
	 * Gestiona la solicitud HTTP GET para llevar a la página de home una vez logeado con exito.
	 * @return La vista de home.html
	 */
	@GetMapping("/privada/home")
	public String loginCorrecto(Model model, Authentication authentication) {
		model.addAttribute("nombreUsuario", authentication.getName());
		System.out.println(authentication.getAuthorities());
		return "home";
	}
	
	@GetMapping("/privada/listado")
	public String listadoUsuarios(Model model, HttpServletRequest request,Authentication authentication) {
		List<UsuarioDTO> usuarios = usuarioServicio.buscarTodos();
		System.out.println(usuarios);
		model.addAttribute("usuarios", usuarios);
		if(request.isUserInRole("ROLE_ADMIN")) {
			return "listado";	
		} 
		model.addAttribute("noAdmin", "No eres admin");
		model.addAttribute("nombreUsuario", authentication.getName());
		return "home";
	}
	
	@GetMapping("/privada/eliminar/{id}")
	public String eliminarUsuario(@PathVariable Long id, Model model, HttpServletRequest request) {
		Usuario usuario = usuarioServicio.buscarPorId(id);
		List<UsuarioDTO> usuarios = usuarioServicio.buscarTodos();
		if(request.isUserInRole("ROLE_ADMIN") && usuario.getRol().equals("ROLE_ADMIN")) {
			model.addAttribute("noSePuedeEliminar", "No se puede eliminar a un admin");
			model.addAttribute("usuarios", usuarios);
			return "listado";
		}
		usuarioServicio.eliminar(id);
		return "redirect:/privada/listado";
		
	}
	

}
