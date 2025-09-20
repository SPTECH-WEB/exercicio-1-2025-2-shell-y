package school.sptech.prova_ac1;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
    public Optional<Usuario> findByEmail(String email);
    public Optional<Usuario> findByCpf(String cpf);
    public List<Usuario> findByEmailOrCpf(String email,String cpf);
    public List<Usuario> findByDataNascimentoGreaterThan(LocalDate dataNascimento);
}
