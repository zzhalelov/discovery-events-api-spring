package kz.zzhalelov.server.compilation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
}