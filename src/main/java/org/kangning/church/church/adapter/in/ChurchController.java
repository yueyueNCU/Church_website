package org.kangning.church.church.adapter.in;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.adapter.in.security.UserPrincipal;
import org.kangning.church.auth.domain.User;
import org.kangning.church.church.adapter.in.dto.ChurchResponse;
import org.kangning.church.church.adapter.in.dto.CreateChurchRequest;
import org.kangning.church.church.application.port.in.ChurchResult;
import org.kangning.church.church.application.port.in.ChurchUseCase;
import org.kangning.church.church.application.port.in.CreateChurchCommand;
import org.kangning.church.church.domain.Church;
import org.kangning.church.common.identifier.ChurchId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/church")
@RequiredArgsConstructor
public class ChurchController {

    private final ChurchUseCase churchUseCase;

    @PostMapping
    public ResponseEntity<ChurchResponse> createChurch(
            @Valid @RequestBody CreateChurchRequest request,
            Authentication authentication
    ){

        UserPrincipal principal=(UserPrincipal) authentication.getPrincipal();

        CreateChurchCommand command = new CreateChurchCommand(request.name(), request.address(), request.description());

        Church church=churchUseCase.createChurch(principal.id(), command);
        ChurchResponse response = new ChurchResponse(church.getId(), church.getName(), church.getAddress(), church.getDescription(), church.getCreatedAt());

        return ResponseEntity
                .created(URI.create("/api/church/" + church.getId().value()))
                .body(response);
    }


    @GetMapping("/me")
    public ResponseEntity<List<ChurchResponse>> myChurches(
            Authentication authentication) {

        UserPrincipal principal=(UserPrincipal) authentication.getPrincipal();

        List<ChurchResult> result =churchUseCase.getMyChurches(principal.id());

        List<ChurchResponse> response = result.stream()
                .map(e -> new ChurchResponse(
                        e.id(), e.name(), e.address(), e.description(), e.createdAt()))
                .toList();

        if (response.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok(response); // 200 OK
    }

    /* ---------- 4. 模糊搜尋教會 (選用) ---------- */
    @GetMapping("/search")
    public ResponseEntity<List<ChurchResponse>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "0")  int offset) {

        List<ChurchResult> result=churchUseCase.searchByNameContaining(keyword, size, offset);

        List<ChurchResponse> response = result.stream()
                .map(e -> new ChurchResponse(
                        e.id(), e.name(), e.address(), e.description(), e.createdAt()))
                .toList();

        if (response.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok(response); // 200 OK
    }



}
