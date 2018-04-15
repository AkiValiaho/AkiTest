package AkiTest.assertz.jsonPath;

import lombok.Getter;

class JsonKey {
    @Getter
    private final String keyname;

    JsonKey(String keyName) {
        this.keyname = keyName;
    }

    @Override
    public int hashCode() {
        return keyname.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return keyname.equals(((JsonKey) obj).getKeyname());
    }
}
