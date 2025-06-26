package kr.klr.challkathon.domain.exercise.controller;

import kr.klr.challkathon.domain.exercise.dto.request.SimpleExerciseRecordReq;
import kr.klr.challkathon.domain.exercise.dto.request.WalkingExerciseRecordReq;
import kr.klr.challkathon.domain.exercise.dto.response.*;
import kr.klr.challkathon.domain.exercise.entity.Exercise;
import kr.klr.challkathon.domain.exercise.entity.ExerciseType;
import kr.klr.challkathon.domain.exercise.repository.ExerciseRepository;
import kr.klr.challkathon.domain.exercise.service.ExerciseService;
import kr.klr.challkathon.domain.exercise.spec.ExerciseControllerSpec;
import kr.klr.challkathon.global.customAnnotation.CurrentUser;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exercise")
@RequiredArgsConstructor
public class ExerciseController implements ExerciseControllerSpec {

    private final ExerciseService exerciseService;
    private final ExerciseRepository exerciseRepository;

    @GetMapping("/list")
    @Override
    public GlobalApiResponse<List<ExerciseRes>> getExerciseList() {
        List<Exercise> exercises = exerciseRepository.findAllActiveExercisesOrderedByRequiredFirst();
        
        List<ExerciseRes> exerciseItems = exercises.stream()
                .map(ExerciseRes::fromEntity)
                .collect(Collectors.toList());
        
        return GlobalApiResponse.success(exerciseItems);
    }

    @GetMapping("/indoor/status")
    @Override
    public GlobalApiResponse<IndoorExerciseStatusRes> getIndoorExerciseStatus(@CurrentUser String userUid) {
        IndoorExerciseStatusRes response = exerciseService.getIndoorExerciseStatus(userUid);
        return GlobalApiResponse.success(response);
    }

    @GetMapping("/outdoor/status")
    @Override
    public GlobalApiResponse<OutdoorExerciseStatusRes> getOutdoorExerciseStatus(@CurrentUser String userUid) {
        OutdoorExerciseStatusRes response = exerciseService.getOutdoorExerciseStatus(userUid);
        return GlobalApiResponse.success(response);
    }

    @PostMapping("/record/walking")
    @Override
    public GlobalApiResponse<String> recordWalkingExercise(
            @CurrentUser String userUid,
            @Valid @RequestBody WalkingExerciseRecordReq request) {
        String message = exerciseService.recordWalkingExercise(userUid, request);
        return GlobalApiResponse.success(message);
    }

    @PostMapping("/record/simple")
    @Override
    public GlobalApiResponse<String> recordSimpleExercise(
            @CurrentUser String userUid,
            @Valid @RequestBody SimpleExerciseRecordReq request) {
        String message = exerciseService.recordSimpleExercise(userUid, request);
        return GlobalApiResponse.success(message);
    }

    @GetMapping("/history")
    @Override
    public GlobalApiResponse<ExerciseHistoryRes> getExerciseHistory(
            @CurrentUser String userUid,
            @RequestParam(required = false) ExerciseType exerciseType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        ExerciseHistoryRes response = exerciseService.getExerciseHistory(userUid, exerciseType, startDate, endDate);
        return GlobalApiResponse.success(response);
    }
}
