package gchat.dummy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TAddressService {
    private final TAddressRepository tAddressRepository;

    @PostConstruct
    public void init(){
//        TAddress taddress = TAddress.builder().street1("aa10").street2("bb1")
//                .city("noida1").state("UP1").zipcode("201301")
//                .build();
//        tAddressRepository.save(taddress);
//
//        taddress.setCountry("India IN");
//        taddress.setCountryCreateDateTime(LocalDateTime.now());
//        tAddressRepository.save(taddress);
        Optional<TAddress> tAddressOpt = tAddressRepository.findById(5L);
        if(tAddressOpt.isPresent()){
            TAddress taddress = tAddressOpt.get();
            taddress.setCountry("US");
            tAddressRepository.save(taddress);
        }

    }
}
