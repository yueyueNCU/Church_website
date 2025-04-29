package org.kangning.church.common.domain;

public interface Identifier<T> {

    /** 取出包裝的真實值 */
    T value();
}