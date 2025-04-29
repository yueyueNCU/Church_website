package org.kangning.church.church.adapter.in;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kangning.church.church.adapter.in.dto.ChurchResponse;
import org.kangning.church.church.adapter.in.dto.CreateChurchRequest;
import org.kangning.church.church.adapter.in.dto.CreateChurchResponse;
import org.kangning.church.church.application.port.in.ChurchResult;
import org.kangning.church.church.application.port.in.ChurchUseCase;
import org.kangning.church.church.application.port.in.CreateChurchCommand;
import org.kangning.church.church.application.port.in.CreateChurchResult;
import org.kangning.church.church.domain.Church;
import org.kangning.church.common.identifier.ChurchId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/church")
@RequiredArgsConstructor
public class ChurchController {

    private final ChurchUseCase churchUseCase;

    @PostMapping
    public ResponseEntity<CreateChurchResponse> createChurch(
            @Valid @RequestBody CreateChurchRequest request,
            Authentication authentication
    ){
        CreateChurchCommand command = new CreateChurchCommand(request.name(), request.address(), request.description());

        ChurchId churchId=churchUseCase.createChurch(authentication.getName(), command);
        CreateChurchResponse response = new CreateChurchResponse(churchId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Void> join(
            @PathVariable Long id,
            Authentication authentication) {

        churchUseCase.joinChurch(
                authentication.getName(),
                new ChurchId(id)
        );
        return ResponseEntity.accepted().build();
    }

    /* ---------- 3. 取得自己已核准教會 ---------- */
    @GetMapping("/me")
    public List<ChurchResponse> myChurches(
            Authentication authentication) {

        List<ChurchResult> result =churchUseCase.getMyChurches(authentication.getName());

        return result.stream()
                .map( e-> new ChurchResponse(
                        e.id(),
                        e.name(),
                        e.address(),
                        e.description(),
                        e.createdAt()))
                .toList();
    }

    /* ---------- 4. 模糊搜尋教會 (選用) ---------- */
    @GetMapping("/search")
    public List<ChurchResponse> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "0")  int offset) {

        List<ChurchResult> result=churchUseCase.searchByNameContaining(keyword, size, offset);
        return result.stream()
                .map( e-> new ChurchResponse(
                        e.id(),
                        e.name(),
                        e.address(),
                        e.description(),
                        e.createdAt()))
                .toList();
    }



}
