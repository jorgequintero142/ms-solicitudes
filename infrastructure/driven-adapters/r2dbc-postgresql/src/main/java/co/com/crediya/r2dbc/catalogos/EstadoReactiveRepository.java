package co.com.crediya.r2dbc.catalogos;

import co.com.crediya.r2dbc.entity.EstadoEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EstadoReactiveRepository extends ReactiveCrudRepository<EstadoEntity, Integer>, ReactiveQueryByExampleExecutor<EstadoEntity> {

}
