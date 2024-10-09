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
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuarioConValidacion(@PathVariable Long id, @RequestBody Usuario usuarioDetails) {
        // Buscar el usuario por su ID
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar el nuevo nombre antes de actualizar
        if (usuarioDetails.getNombre() != null && usuarioDetails.getNombre().length() >= 3) {
            usuario.setNombre(usuarioDetails.getNombre());
        } else {
            return ResponseEntity.badRequest().body("El nombre debe tener al menos 3 caracteres."); // Mensaje de error si no es válido
        }

        // Validar el nuevo email antes de actualizar
        if (usuarioDetails.getEmail() != null && usuarioDetails.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            usuario.setEmail(usuarioDetails.getEmail());
        } else {
            return ResponseEntity.badRequest().body("El formato del email no es válido."); // Mensaje de error si no es válido
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
