package com.library.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) {

    @Test
    fun `POST then GET returns the book`() {
        val dto = BookCreateDto("Test Book", "Test Author", 2020, 999.0, 1)
        val json = objectMapper.writeValueAsString(dto)

        val response = mockMvc.perform(
            post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.title").value("Test Book"))
            .andReturn()

        val created = objectMapper.readValue(response.response.contentAsString, BookResponseDto::class.java)

        mockMvc.perform(get("/api/books/${created.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Test Book"))
    }

    @Test
    fun `GET non-existing returns 404`() {
        mockMvc.perform(get("/api/books/99999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `POST with invalid year returns 400`() {
        val invalid = BookCreateDto("X", "Y", 3000, 100.0, 1)
        mockMvc.perform(
            post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Bad Request"))
    }
}