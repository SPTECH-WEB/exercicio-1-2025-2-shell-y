package school.sptech.prova_ac1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    //Criamos um atributo pro repositório para poder pegar os dados do banco:
    private UsuarioRepository usuarioRepository;
    //Construtor pro repository criado:
    public UsuarioController(UsuarioRepository usuario) {
        this.usuarioRepository = usuario;
    }

    //METHODS
    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodos() {
        List<Usuario> listaUsuarios = usuarioRepository.findAll();
        if(listaUsuarios.isEmpty()) return ResponseEntity.status(204).body(null);
        else return ResponseEntity.status(200).body(listaUsuarios);
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        Optional<Usuario> existeEmail = usuarioRepository.findByEmail(usuario.getEmail());
        Optional<Usuario> existeCpf = usuarioRepository.findByCpf(usuario.getCpf());
        if(existeEmail.isEmpty() && existeCpf.isEmpty() && usuario.getId() == null){
            Usuario usuarioCadastrado = usuarioRepository.save(usuario);
            return ResponseEntity.status(201).body(usuarioCadastrado);
        }else{
            return ResponseEntity.status(409).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if(usuarioExistente.isEmpty()) return ResponseEntity.status(404).body(null);
        else return ResponseEntity.status(200).body(usuarioExistente.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);

        if(usuarioExistente.isEmpty()) return ResponseEntity.status(404).body(null);
        else {
            usuarioRepository.deleteById(id);
            return ResponseEntity.status(204).body(null);
        }
    }

    @GetMapping("/filtro-data")
    public ResponseEntity<List<Usuario>> buscarPorDataNascimento(@RequestParam LocalDate nascimento) {
        List<Usuario> usuariosEncontrados = usuarioRepository.findByDataNascimentoGreaterThan(nascimento);

        if(usuariosEncontrados.isEmpty()) return ResponseEntity.status(204).body(null);
        else return ResponseEntity.status(200).body(usuariosEncontrados);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Integer id,@RequestBody Usuario usuario) {
        Optional<Usuario> idExistente = usuarioRepository.findById(id);
        List<Usuario> listaUsuarios = usuarioRepository.findByEmailOrCpf(usuario.getEmail(), usuario.getCpf());

        if(idExistente.isEmpty()){
            return ResponseEntity.status(404).body(null);
        }else if(listaUsuarios.size() >= 2){
            return ResponseEntity.status(409).body(null);
        }else{
            usuario.setId(id);
            return ResponseEntity.status(200).body(usuarioRepository.save(usuario));
        }
    }
}
