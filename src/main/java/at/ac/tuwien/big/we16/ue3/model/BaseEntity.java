package at.ac.tuwien.big.we16.ue3.model;

import javax.persistence.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Rares on 11-May-16.
 */
@MappedSuperclass
public class BaseEntity {

    private static final AtomicInteger idCount = new AtomicInteger(0);
    @Transient
    private final long lId;

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    public BaseEntity() {
        lId = idCount.getAndIncrement();
        id = ""+lId;
    }

    public String getId(){
        return id;
    }
}
