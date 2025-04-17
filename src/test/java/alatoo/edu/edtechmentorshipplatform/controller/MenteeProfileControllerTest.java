package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.users.MenteeProfileRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.users.MenteeProfileResponseDto;
import alatoo.edu.edtechmentorshipplatform.enums.Lang;
import alatoo.edu.edtechmentorshipplatform.service.MenteeProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MenteeProfileControllerTest {

    @Mock
    private MenteeProfileService menteeProfileService;

    @InjectMocks
    private MenteeProfileController menteeProfileController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(menteeProfileController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateProfile() throws Exception {
        MenteeProfileRequestDto requestDto = new MenteeProfileRequestDto();
        requestDto.setEducationLevel("Bachelor");
        requestDto.setFieldOfStudy("Computer Science");
        requestDto.setCareerGoal("Software Developer");
        requestDto.setPreferredLanguage(Lang.EN);

        MenteeProfileResponseDto responseDto = new MenteeProfileResponseDto(UUID.randomUUID(), "Bachelor", "Computer Science", "Software Developer", "English");

        when(menteeProfileService.createProfile(any(MenteeProfileRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/mentee-profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.educationLevel").value("Bachelor"))
                .andExpect(jsonPath("$.fieldOfStudy").value("Computer Science"))
                .andExpect(jsonPath("$.careerGoal").value("Software Developer"))
                .andExpect(jsonPath("$.preferredLanguage").value("English"));

        verify(menteeProfileService, times(1)).createProfile(any(MenteeProfileRequestDto.class));
    }

    @Test
    public void testUpdateProfile() throws Exception {
        UUID profileId = UUID.randomUUID();
        MenteeProfileRequestDto requestDto = new MenteeProfileRequestDto();
        requestDto.setEducationLevel("Master");
        requestDto.setFieldOfStudy("Data Science");
        requestDto.setCareerGoal("Data Scientist");
        requestDto.setPreferredLanguage(Lang.RU);

        MenteeProfileResponseDto responseDto = new MenteeProfileResponseDto(profileId, "Master", "Data Science", "Data Scientist", "English");

        when(menteeProfileService.updateProfile(eq(profileId), any(MenteeProfileRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/mentee-profiles/{profileId}", profileId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.educationLevel").value("Master"))
                .andExpect(jsonPath("$.fieldOfStudy").value("Data Science"))
                .andExpect(jsonPath("$.careerGoal").value("Data Scientist"))
                .andExpect(jsonPath("$.preferredLanguage").value("English"));

        verify(menteeProfileService, times(1)).updateProfile(eq(profileId), any(MenteeProfileRequestDto.class));
    }

    @Test
    public void testGetProfileById() throws Exception {
        UUID profileId = UUID.randomUUID();
        MenteeProfileResponseDto responseDto = new MenteeProfileResponseDto(profileId, "Bachelor", "Computer Science", "Software Developer", "English");

        when(menteeProfileService.getProfileById(profileId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/mentee-profiles/{profileId}", profileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.educationLevel").value("Bachelor"))
                .andExpect(jsonPath("$.fieldOfStudy").value("Computer Science"))
                .andExpect(jsonPath("$.careerGoal").value("Software Developer"))
                .andExpect(jsonPath("$.preferredLanguage").value("English"));

        verify(menteeProfileService, times(1)).getProfileById(profileId);
    }

    @Test
    public void testDeleteProfile() throws Exception {
        UUID profileId = UUID.randomUUID();

        doNothing().when(menteeProfileService).deleteProfile(profileId);

        mockMvc.perform(delete("/api/mentee-profiles/{profileId}", profileId))
                .andExpect(status().isNoContent());

        verify(menteeProfileService, times(1)).deleteProfile(profileId);
    }

    @Test
    public void testGetAllProfiles() throws Exception {
        MenteeProfileResponseDto profile1 = new MenteeProfileResponseDto(UUID.randomUUID(), "Bachelor", "Computer Science", "Software Developer", "English");
        MenteeProfileResponseDto profile2 = new MenteeProfileResponseDto(UUID.randomUUID(), "Master", "Data Science", "Data Scientist", "English");

        when(menteeProfileService.getAllProfiles()).thenReturn(List.of(profile1, profile2));

        mockMvc.perform(get("/api/mentee-profiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].educationLevel").value("Bachelor"))
                .andExpect(jsonPath("$[1].educationLevel").value("Master"));

        verify(menteeProfileService, times(1)).getAllProfiles();
    }
}
