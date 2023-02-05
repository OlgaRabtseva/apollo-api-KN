package utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Endpoint {
    ACCOUNT_BASE_URL("https://demoqa.com/Account/v1/"),
    BOOK_STORE_BASE_URL("https://demoqa.com/BookStore/v1/");

    private final String baseUrl;
}
