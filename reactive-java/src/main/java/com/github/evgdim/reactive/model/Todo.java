package com.github.evgdim.reactive.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Todo {
    private final Long id;
    private final Long userId;
    private final String title;
    private final Boolean completed;
}
