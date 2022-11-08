package com.project.book;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookSearchControllerTest {
    @Autowired
    public MockMvc mock;

    @Nested
    class BookListSearchTest{

    }

    @Nested
    class BookInfoSearchTest{

    }

    @Nested
    class UserHistoryTest{

    }

    @Nested
    class RankTest{

    }
}
