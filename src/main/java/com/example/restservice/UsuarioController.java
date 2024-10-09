package com.example.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createUsuario(@RequestBody Usuario usuario) {
        // Validar que el nombre empiece con una letra mayúscula
        if (usuario.getNombre() == null || !Character.isUpperCase(usuario.getNombre().charAt(0))) {
            return ResponseEntity.badRequest().body("El nombre debe comenzar con una letra mayúscula.");
        }

        // Guardar el nuevo usuario
        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetails) {
        // Buscar el usuario por su ID
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar que el nuevo nombre empiece con una letra mayúscula
        if (usuarioDetails.getNombre() != null && !Character.isUpperCase(usuarioDetails.getNombre().charAt(0))) {
            return ResponseEntity.badRequest().body("El nombre debe comenzar con una letra mayúscula.");
        }

        // Actualizar el nombre y el email del usuario
        if (usuarioDetails.getNombre() != null) {
            usuario.setNombre(usuarioDetails.getNombre());
        }
        if (usuarioDetails.getEmail() != null) {
            usuario.setEmail(usuarioDetails.getEmail());
        }

        // Guardar los cambios en la base de datos
        final Usuario updatedUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(updatedUsuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuarioRepository.delete(usuario); // Eliminar el usuario
        return ResponseEntity.noContent().build(); // Retorna 204 No Content si se elimina exitosamente
    }
}
