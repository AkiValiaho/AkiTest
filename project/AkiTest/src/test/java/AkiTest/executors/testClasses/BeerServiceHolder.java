package AkiTest.executors.testClasses;

import annotations.AkiMock;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by vagrant on 3/3/17.
 */
public class BeerServiceHolder {
@Getter
@Setter
@AkiMock
private final BeerService beerService;

@Getter
@Setter
@AkiMock
private NoargsBeerService noargsBeerService;

    public BeerServiceHolder(BeerService beerService) {
        this.beerService = beerService;
    }
}
