package kr.klr.challkathon.domain.exercise.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import kr.klr.challkathon.domain.exercise.dto.request.SimpleExerciseRecordReq;
import kr.klr.challkathon.domain.exercise.dto.request.WalkingExerciseRecordReq;
import kr.klr.challkathon.domain.exercise.dto.response.*;
import kr.klr.challkathon.domain.exercise.entity.ExerciseType;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Tag(name = "Exercise", description = "운동 관련 API")
public interface ExerciseControllerSpec {

    @Operation(summary = "운동 목록 조회", description = "사용 가능한 모든 운동 목록을 조회합니다.")
    GlobalApiResponse<List<ExerciseRes>> getExerciseList();

    @Operation(summary = "실내운동 현황 조회", description = "오늘의 진행 상황과 운동 목록을 조회합니다.")
    GlobalApiResponse<IndoorExerciseStatusRes> getIndoorExerciseStatus(
            @Parameter(hidden = true) String userUid);

    @Operation(summary = "실외운동 현황 조회", description = "최고 거리 기록과 전날 기록을 조회합니다.")
    GlobalApiResponse<OutdoorExerciseStatusRes> getOutdoorExerciseStatus(
            @Parameter(hidden = true) String userUid);

    @Operation(summary = "가벼운 걷기 운동 기록", description = "가벼운 걷기 운동의 상세 데이터를 기록합니다.")
    GlobalApiResponse<String> recordWalkingExercise(
            @Parameter(hidden = true) String userUid,
            @Valid @RequestBody WalkingExerciseRecordReq request);

    @Operation(summary = "일반 운동 기록", description = "가벼운 걷기를 제외한 나머지 운동의 시간을 기록합니다.")
    GlobalApiResponse<String> recordSimpleExercise(
            @Parameter(hidden = true) String userUid,
            @Valid @RequestBody SimpleExerciseRecordReq request);

    @Operation(summary = "운동 기록 조회", description = "기간별/타입별 운동 기록을 조회합니다.")
    GlobalApiResponse<ExerciseHistoryRes> getExerciseHistory(
            @Parameter(hidden = true) String userUid,
            @RequestParam(required = false) ExerciseType exerciseType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);
}