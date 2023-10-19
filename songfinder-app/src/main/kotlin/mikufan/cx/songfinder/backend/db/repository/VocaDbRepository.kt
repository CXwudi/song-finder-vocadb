package mikufan.cx.songfinder.backend.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.PagingAndSortingRepository

@NoRepositoryBean
interface VocaDbRepository<T, ID> : CrudRepository<T, ID>, ListCrudRepository<T, ID>, PagingAndSortingRepository<T, ID>
