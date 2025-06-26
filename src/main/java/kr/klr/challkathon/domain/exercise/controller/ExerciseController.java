package kr.klr.challkathon.domain.exercise.controller;

import kr.klr.challkathon.domain.exercise.dto.request.SimpleExerciseRecordReq;
import kr.klr.challkathon.domain.exercise.dto.request.WalkingExerciseRecordReq;
import kr.klr.challkathon.domain.exercise.dto.response.*;
import kr.klr.challkathon.domain.exercise.entity.Exercise;
import kr.klr.challkathon.domain.exercise.entity.ExerciseType;
import kr.klr.challkathon.domain.exercise.repository.ExerciseRepository;
import kr.klr.challkathon.domain.exercise.service.ExerciseService;
import kr.klr.challkathon.domain.user.entity.User;
import kr.klr.challkathon.global.customAnnotation.CurrentUser;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exercise")
@RequiredArgsConstructor
@Tag(name = "Exercise", description = "운동 관련 API")
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ExerciseRepository exerciseRepository;

    @GetMapping("/list")
    @Operation(summary = "운동 목록 조회", description = "사용 가능한 모든 운동 목록을 조회합니다.")
    public GlobalApiResponse<List<ExerciseRes>> getExerciseList() {
        List<Exercise> exercises = exerciseRepository.findAllActiveExercisesOrderedByRequiredFirst();
        
        List<ExerciseRes> exerciseItems = exercises.stream()
                .map(ExerciseRes::fromEntity)
                .collect(Collectors.toList());
        
        return GlobalApiResponse.success(exerciseItems);
    }
    @GetMapping("/indoor/status")
    @Operation(summary = "실내운동 현황 조회", description = "오늘의 진행 상황과 운동 목록을 조회합니다.")
    public GlobalApiResponse<IndoorExerciseStatusRes> getIndoorExerciseStatus(@CurrentUser User user) {
        IndoorExerciseStatusRes response = exerciseService.getIndoorExerciseStatus(user);
        return GlobalApiResponse.success(response);
    }

    @GetMapping("/outdoor/status")
    @Operation(summary = "실외운동 현황 조회", description = "최고 거리 기록과 전날 기록을 조회합니다.")
    public GlobalApiResponse<OutdoorExerciseStatusRes> getOutdoorExerciseStatus(@CurrentUser User user) {
        OutdoorExerciseStatusRes response = exerciseService.getOutdoorExerciseStatus(user);
        return GlobalApiResponse.success(response);
    }

    @PostMapping("/record/walking")
    @Operation(summary = "가벼운 걷기 운동 기록", description = "가벼운 걷기 운동의 상세 데이터를 기록합니다.")
    public GlobalApiResponse<String> recordWalkingExercise(
            @CurrentUser User user,
            @Valid @RequestBody WalkingExerciseRecordReq request) {
        String message = exerciseService.recordWalkingExercise(user, request);
        return GlobalApiResponse.success(message);
    }

    @PostMapping("/record/simple")
    @Operation(summary = "일반 운동 기록", description = "가벼운 걷기를 제외한 나머지 운동의 시간을 기록합니다.")
    public GlobalApiResponse<String> recordSimpleExercise(
            @CurrentUser User user,
            @Valid @RequestBody SimpleExerciseRecordReq request) {
        String message = exerciseService.recordSimpleExercise(user, request);
        return GlobalApiResponse.success(message);
    }

    @GetMapping("/history")
    @Operation(summary = "운동 기록 조회", description = "기간별/타입별 운동 기록을 조회합니다.")
    public GlobalApiResponse<ExerciseHistoryRes> getExerciseHistory(
            @CurrentUser User user,
            @RequestParam(required = false) ExerciseType exerciseType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        ExerciseHistoryRes response = exerciseService.getExerciseHistory(user, exerciseType, startDate, endDate);
        return GlobalApiResponse.success(response);
    }
}
