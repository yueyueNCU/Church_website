package org.kangning.church.common.exception.church;

public class ChurchNotFoundException extends RuntimeException {
    public ChurchNotFoundException(){
        super("查無教會");
    }
}
