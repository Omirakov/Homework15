package org.skypro.skyshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ShopControllerAdvice {

    @ExceptionHandler(NoSuchProductException.class)
    public ResponseEntity<ShopError> errorNoSuchProductException(NoSuchProductException e) {
        ShopError error = new ShopError("PRODUCT_NOT_FOUND", "Искомый продукт в магазине не найден.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
