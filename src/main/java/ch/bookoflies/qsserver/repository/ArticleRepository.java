package ch.bookoflies.qsserver.repository;

import ch.bookoflies.qsserver.persistent.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("articleRepository")
public interface ArticleRepository extends JpaRepository<Article, String> {

    @Query("SELECT u FROM User u WHERE u.name = :name")
    Optional<Article> findByName(String name);
}
