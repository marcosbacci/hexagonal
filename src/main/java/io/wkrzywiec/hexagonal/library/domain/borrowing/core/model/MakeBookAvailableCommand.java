package io.wkrzywiec.hexagonal.library.domain.borrowing.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class MakeBookAvailableCommand {
    private Long bookId;
}
