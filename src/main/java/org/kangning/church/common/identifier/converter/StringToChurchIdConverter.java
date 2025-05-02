package org.kangning.church.common.identifier.converter;

import org.kangning.church.common.identifier.ChurchId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToChurchIdConverter implements Converter<String, ChurchId> {
    @Override
    public ChurchId convert(String source) {
        return new ChurchId(Long.parseLong(source));
    }
}