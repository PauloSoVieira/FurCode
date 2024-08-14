package org.mindera.fur.code.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AIServiceTest {

    private AIService aiService;

    @BeforeEach
    void setUp() {
        aiService = mock(AIService.class);
    }

    @Test
    void generateNewPetDescription_withValidPet_shouldSucceed() {
        when(aiService.generateNewPetDescription(new PetDTO())).thenReturn("");

        String result = aiService.generateNewPetDescription(new PetDTO());
        assertEquals("", result);
        assertDoesNotThrow(() -> aiService.generateNewPetDescription(new PetDTO()));
    }

    @Test
    void generateNewPetDescription_withNullPet_shouldFail() {
        doCallRealMethod().when(aiService).generateNewPetDescription(null); //ignore the mock and call the real method
        assertThrows(IllegalArgumentException.class, () -> aiService.generateNewPetDescription(null));
    }

    @Test
    void generateNewPetSearchQuery_withValidSearchQuery_shouldSucceed() {
        // TODO: implement test, not sure how
    }

    @Test
    void generateNewPetSearchQuery_withNullSearchQuery_shouldFail() {
        doCallRealMethod().when(aiService).generateNewPetSearchQuery(null);
        assertThrows(IllegalArgumentException.class, () -> aiService.generateNewPetSearchQuery(null));
    }

    @Test
    void generateNewPetSearchQuery_withEmptySearchQuery_shouldFail() {
        doCallRealMethod().when(aiService).generateNewPetSearchQuery("");
        assertThrows(IllegalArgumentException.class, () -> aiService.generateNewPetSearchQuery(""));
    }
}
