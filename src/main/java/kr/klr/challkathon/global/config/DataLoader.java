package kr.klr.challkathon.global.config;

import kr.klr.challkathon.domain.exercise.entity.Exercise;
import kr.klr.challkathon.domain.exercise.entity.ExerciseCategory;
import kr.klr.challkathon.domain.exercise.entity.ExerciseType;
import kr.klr.challkathon.domain.exercise.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    
    private final ExerciseRepository exerciseRepository;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadInitialExerciseData();
    }
    
    private void loadInitialExerciseData() {
        if (exerciseRepository.count() > 0) {
            log.info("운동 데이터가 이미 존재합니다. 초기 데이터 로딩을 건너뜁니다.");
            return;
        }
        
        log.info("초기 운동 데이터를 로딩합니다...");
        
        // 필수 운동 (실내)
        Exercise lightWalking = Exercise.builder()
                .name("가벼운 걷기")
                .description("실내에서 천천히 걷는 운동으로 혈액순환을 돕습니다.")
                .category(ExerciseCategory.REQUIRED)
                .type(ExerciseType.INDOOR)
                .durationMinutes(10)
                .caloriesPerMinute(3.0)
                .isRequired(true)
                .orderIndex(1)
                .build();
        
        Exercise legStretching = Exercise.builder()
                .name("다리 스트레칭")
                .description("다리 근육의 긴장을 완화하고 유연성을 높이는 스트레칭입니다.")
                .category(ExerciseCategory.REQUIRED)
                .type(ExerciseType.INDOOR)
                .durationMinutes(15)
                .caloriesPerMinute(2.0)
                .isRequired(true)
                .orderIndex(2)
                .build();
        
        Exercise walkingAssist = Exercise.builder()
                .name("걷기 보조 운동")
                .description("보조기구를 활용한 안전한 걷기 운동입니다.")
                .category(ExerciseCategory.REQUIRED)
                .type(ExerciseType.INDOOR)
                .durationMinutes(20)
                .caloriesPerMinute(2.5)
                .isRequired(true)
                .orderIndex(3)
                .build();
        
        // 함께하면 좋아요 (실내)
        Exercise standingExercise = Exercise.builder()
                .name("서서하기 운동")
                .description("서서 할 수 있는 간단한 운동으로 하체 근력을 강화합니다.")
                .category(ExerciseCategory.RECOMMENDED)
                .type(ExerciseType.INDOOR)
                .durationMinutes(10)
                .caloriesPerMinute(3.5)
                .isRequired(false)
                .orderIndex(4)
                .build();
        
        Exercise sittingLegExercise = Exercise.builder()
                .name("앉아서 다리 운동")
                .description("의자에 앉아서 할 수 있는 다리 근력 운동입니다.")
                .category(ExerciseCategory.RECOMMENDED)
                .type(ExerciseType.INDOOR)
                .durationMinutes(15)
                .caloriesPerMinute(2.5)
                .isRequired(false)
                .orderIndex(5)
                .build();
        
        Exercise balanceExercise = Exercise.builder()
                .name("균형 운동")
                .description("균형감각을 기르고 낙상을 예방하는 운동입니다.")
                .category(ExerciseCategory.RECOMMENDED)
                .type(ExerciseType.INDOOR)
                .durationMinutes(10)
                .caloriesPerMinute(2.0)
                .isRequired(false)
                .orderIndex(6)
                .build();
        
        // 데이터베이스에 저장
        exerciseRepository.save(lightWalking);
        exerciseRepository.save(legStretching);
        exerciseRepository.save(walkingAssist);
        exerciseRepository.save(standingExercise);
        exerciseRepository.save(sittingLegExercise);
        exerciseRepository.save(balanceExercise);
        
        log.info("초기 운동 데이터 로딩이 완료되었습니다. 총 {}개의 운동이 추가되었습니다.", 6);
    }
}
