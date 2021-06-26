package gchat.dummy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_address")
@SecondaryTables({
    @SecondaryTable(name="t_city"),
    @SecondaryTable(name="t_country")
})
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String street1;
    private String street2;
    @CreationTimestamp
    private LocalDateTime createDateTime;
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Column(table="t_country")
    private String country;

    @Column(table="t_country")
    private LocalDateTime CountryCreateDateTime;

    @Column(table="t_city")
    private String city;
    @Column(table="t_city")
    private String state;
    @Column(table="t_city")
    private String zipcode;
    @Column(table="t_city")
    private LocalDateTime CityCreateDateTime;
}