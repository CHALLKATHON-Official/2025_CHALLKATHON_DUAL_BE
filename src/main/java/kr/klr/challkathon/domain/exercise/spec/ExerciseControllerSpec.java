package kr.klr.challkathon.domain.exercise.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import kr.klr.challkathon.domain.exercise.dto.request.ExerciseRecordReq;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Exercise", description = "운동 관련 API")
public interface ExerciseControllerSpec {

    @Operation(summary = "실내 운동 목록 조회", description = "실내 재활운동 목록을 조회합니다. 완료 여부가 포함됩니다.")
    @GetMapping("/api/v1/exercise/indoor")
    GlobalApiResponse<?> getIndoorExercises(
            @Parameter(hidden = true) String userUid);

    @Operation(summary = "운동 기록 저장", description = "운동 완료 후 기록을 저장합니다.")
    @PostMapping("/api/v1/exercise/record")
    GlobalApiResponse<?> recordExercise(
            @Parameter(hidden = true) String userUid,
            ExerciseRecordReq exerciseRecordReq);

    @Operation(summary = "오늘의 운동 기록 조회", description = "오늘 완료한 운동 기록을 조회합니다.")
    @GetMapping("/api/v1/exercise/records/today")
    GlobalApiResponse<?> getTodayExerciseRecords(
            @Parameter(hidden = true) String userUid);

    @Operation(summary = "운동 기록 조회", description = "특정 기간의 운동 기록을 조회합니다.")
    @GetMapping("/api/v1/exercise/records")
    GlobalApiResponse<?> getExerciseRecords(
            @Parameter(hidden = true) String userUid,
            LocalDate startDate,
            LocalDate endDate);
} 