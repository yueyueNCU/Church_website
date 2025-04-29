package org.kangning.church.church.adapter.in;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kangning.church.church.adapter.in.dto.ChurchResponse;
import org.kangning.church.church.adapter.in.dto.CreateChurchRequest;
import org.kangning.church.church.adapter.in.dto.CreateChurchResponse;
import org.kangning.church.church.application.port.in.ChurchResult;
import org.kangning.church.church.application.port.in.ChurchUseCase;
import org.kangning.church.church.application.port.in.CreateChurchCommand;
import org.kangning.church.common.identifier.ChurchId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/church")
@RequiredArgsConstructor
//📦 1. 教會基本管理功能
//
//方法	路由	說明
//GET	/api/church/{id}	查看某個教會詳細資料（例如：名字、地址、簡介、建立時間）
//PUT	/api/church/{id}	更新教會資料（只有 LEADER 或 SITE_ADMIN 可以）
//DELETE	/api/church/{id}	刪除教會（通常限 SITE_ADMIN，或者教會無成員時）
//        📦 2. 教會成員管理（重要！）
//
//方法	路由	說明
//GET	/api/church/{id}/members	列出教會所有成員（包含 PENDING 和 APPROVED）
//PATCH	/api/church/{id}/members/{userId}/approve	核准某個使用者加入教會（變更成 APPROVED）
//DELETE	/api/church/{id}/members/{userId}	將成員從教會移除（踢除）
//        📦 3. 教會搜尋與推薦
//
//方法	路由	說明
//GET	/api/church/search	（你已經有了）模糊搜尋教會
//GET	/api/church/recommend	（可加）推薦教會列表（例如熱門教會、新成立教會）

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

        return ResponseEntity
                .created(URI.create("/api/church/" + churchId.value()))
                .body(response);
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



    @GetMapping("/me")
    public ResponseEntity<List<ChurchResponse>> myChurches(
            Authentication authentication) {

        List<ChurchResult> result =churchUseCase.getMyChurches(authentication.getName());

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
