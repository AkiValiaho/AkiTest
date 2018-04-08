package AkiTest.executors.testClasses;

import annotations.AkiMock;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by vagrant on 3/3/17.
 */
@NoArgsConstructor
public class BeerServiceHolder {
@Getter
@Setter
@AkiMock
private BeerService beerService;

@Getter
@Setter
@AkiMock
private NoargsBeerService noargsBeerService;

    public BeerServiceHolder(BeerService beerService) {
        this.beerService = beerService;
    }
}
