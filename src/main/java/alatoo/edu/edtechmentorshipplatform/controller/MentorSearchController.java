package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.mentor.MentorProfileSummaryDto;
import alatoo.edu.edtechmentorshipplatform.enums.Lang;
import alatoo.edu.edtechmentorshipplatform.services.MentorSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mentors/search")
@RequiredArgsConstructor
public class MentorSearchController {
    private final MentorSearchService searchService;

    @GetMapping
    public Page<MentorProfileSummaryDto> search(
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Lang lang,
        @RequestParam(required = false) Integer minExperience,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return searchService.search(
            categoryId, lang, minExperience, PageRequest.of(page, size)
        );
    }
}