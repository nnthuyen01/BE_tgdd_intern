package com.group04.tgdd.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.test.context.ActiveProfiles;

@Data
@AllArgsConstructor
public class NotFoundException extends RuntimeException {
    String message;
}
