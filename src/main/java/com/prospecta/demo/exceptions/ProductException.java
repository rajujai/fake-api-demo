package com.prospecta.demo.exceptions;

import java.io.IOException;

public class ProductException extends IOException {
    public ProductException(final String message) {
        super(message);
    }
}
