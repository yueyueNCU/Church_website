package org.kangning.church.common.exception.church;

public class ChurchNameDuplicateException extends RuntimeException{
    public ChurchNameDuplicateException(){
        super("教會名稱重複");
    }

}
