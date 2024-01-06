package run.ice.fun.domain.hunter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.query.spi.QueryParameterBindings;
import org.hibernate.type.descriptor.jdbc.BasicBinder;
import org.springframework.data.annotation.CreatedDate;
import run.ice.lib.core.serialize.Serializer;

import java.time.LocalDateTime;

/**
 * @author DaoDao
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "domain_hunter", name = "domain")
public class Domain implements Serializer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sld")
    private String sld;

    @Column(name = "tld")
    private String tld;

    @Column(name = "avail")
    private Boolean avail;

    @Column(name = "price")
    private Long price;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "valid")
    private Boolean valid;

}
