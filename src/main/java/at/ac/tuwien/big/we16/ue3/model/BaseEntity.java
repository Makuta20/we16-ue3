package at.ac.tuwien.big.we16.ue3.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created by Rares on 11-May-16.
 */
@MappedSuperclass
public class BaseEntity {

    @Id
    protected String id;

    public String getId(){
        return id;
    }
}
