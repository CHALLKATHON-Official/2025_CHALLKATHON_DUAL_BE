package kr.klr.challkathon.domain.exercise.controller;

import kr.klr.challkathon.domain.auth.utils.AuthUtil;
import kr.klr.challkathon.domain.exercise.dto.request.ExerciseRecordReq;
import kr.klr.challkathon.domain.exercise.dto.response.ExerciseListRes;
import kr.klr.challkathon.domain.exercise.dto.response.ExerciseRecordRes;
import kr.klr.challkathon.domain.exercise.service.ExerciseService;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/exercise")
@RequiredArgsConstructor
@Tag(name = "Exercise", description = "운동 관련 API")
public class ExerciseController {
    
    private final ExerciseService exerciseService;
    private final AuthUtil authUtil;
    
    @Operation(summary = "실내 운동 목록 조회", description = "실내 재활운동 목록을 조회합니다. 완료 여부가 포함됩니다.")
    @GetMapping("/indoor")
    public ResponseEntity<GlobalApiResponse<ExerciseListRes>> getIndoorExercises(
            HttpServletRequest request) {
        
        String userUid = authUtil.getCurrentUserUid(request);
        ExerciseListRes response = exerciseService.getIndoorExercises(userUid);
        
        return ResponseEntity.ok(GlobalApiResponse.success(response));
    }
    
    @Operation(summary = "운동 기록 저장", description = "운동 완료 후 기록을 저장합니다.")
    @PostMapping("/record")
    public ResponseEntity<GlobalApiResponse<ExerciseRecordRes>> recordExercise(
            HttpServletRequest request,
            @Valid @RequestBody ExerciseRecordReq exerciseRecordReq) {
        
        String userUid = authUtil.getCurrentUserUid(request);
        ExerciseRecordRes response = exerciseService.recordExercise(userUid, exerciseRecordReq);
        
        return ResponseEntity.ok(GlobalApiResponse.success(response));
    }
    
    @Operation(summary = "오늘의 운동 기록 조회", description = "오늘 완료한 운동 기록을 조회합니다.")
    @GetMapping("/records/today")
    public ResponseEntity<GlobalApiResponse<List<ExerciseRecordRes>>> getTodayExerciseRecords(
            HttpServletRequest request) {
        
        String userUid = authUtil.getCurrentUserUid(request);
        List<ExerciseRecordRes> response = exerciseService.getTodayExerciseRecords(userUid);
        
        return ResponseEntity.ok(GlobalApiResponse.success(response));
    }
    
    @Operation(summary = "운동 기록 조회", description = "특정 기간의 운동 기록을 조회합니다.")
    @GetMapping("/records")
    public ResponseEntity<GlobalApiResponse<List<ExerciseRecordRes>>> getExerciseRecords(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        String userUid = authUtil.getCurrentUserUid(request);
        List<ExerciseRecordRes> response = exerciseService.getExerciseRecords(userUid, startDate, endDate);
        
        return ResponseEntity.ok(GlobalApiResponse.success(response));
    }
}
