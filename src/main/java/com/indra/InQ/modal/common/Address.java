package com.indra.InQ.modal.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
    private String firstLine;
    private String secondLine;
    private String city;
    private String state;
    private String country;
    private GeoLocation geoLocation;
}
