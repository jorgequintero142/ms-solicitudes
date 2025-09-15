package co.com.crediya.r2dbc.solicitud;

import co.com.crediya.r2dbc.entity.SolicitudEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SolicitudReactiveRepository extends ReactiveCrudRepository<SolicitudEntity, Integer>, ReactiveQueryByExampleExecutor<SolicitudEntity> {

}
