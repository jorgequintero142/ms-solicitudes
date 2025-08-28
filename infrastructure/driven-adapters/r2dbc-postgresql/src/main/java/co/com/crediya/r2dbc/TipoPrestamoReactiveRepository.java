package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entity.SolicitudEntity;
import co.com.crediya.r2dbc.entity.TipoPrestamoEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TipoPrestamoReactiveRepository extends ReactiveCrudRepository<TipoPrestamoEntity, Integer>, ReactiveQueryByExampleExecutor<TipoPrestamoEntity> {

}
