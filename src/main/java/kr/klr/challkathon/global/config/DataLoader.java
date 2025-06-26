package kr.klr.challkathon.global.config;

import kr.klr.challkathon.domain.exercise.entity.Exercise;
import kr.klr.challkathon.domain.exercise.entity.ExerciseCategory;
import kr.klr.challkathon.domain.exercise.entity.ExerciseType;
import kr.klr.challkathon.domain.exercise.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ExerciseRepository exerciseRepository;

    @Override
    public void run(String... args) throws Exception {
        loadExerciseData();
    }

    private void loadExerciseData() {
        if (exerciseRepository.count() == 0) {
            // 필수 운동
            createExercise("가벼운 걷기", "실내에서 하는 가벼운 걷기 운동", 
                    ExerciseType.INDOOR, ExerciseCategory.REQUIRED, true, 1);
            createExercise("다리 스트레칭", "다리 근육을 풀어주는 스트레칭", 
                    ExerciseType.INDOOR, ExerciseCategory.REQUIRED, true, 2);
            createExercise("걷기 보조 운동", "걷기를 위한 보조 근력 운동", 
                    ExerciseType.INDOOR, ExerciseCategory.REQUIRED, true, 3);

            // 추천 운동
            createExercise("서서하기 운동", "서서 할 수 있는 간단한 운동", 
                    ExerciseType.INDOOR, ExerciseCategory.RECOMMENDED, false, 4);
            createExercise("앉아서 다리 운동", "의자에 앉아서 하는 다리 운동", 
                    ExerciseType.INDOOR, ExerciseCategory.RECOMMENDED, false, 5);
            createExercise("균형 운동", "균형감각을 기르는 운동", 
                    ExerciseType.INDOOR, ExerciseCategory.RECOMMENDED, false, 6);

            // 실외 운동
            createExercise("야외 걷기", "실외에서 하는 걷기 운동", 
                    ExerciseType.OUTDOOR, ExerciseCategory.REQUIRED, false, 7);
            createExercise("조깅", "가벼운 조깅", 
                    ExerciseType.OUTDOOR, ExerciseCategory.RECOMMENDED, false, 8);
        }
    }

    private void createExercise(String name, String description, ExerciseType type, 
                              ExerciseCategory category, boolean isRequired, int orderIndex) {
        Exercise exercise = Exercise.builder()
                .name(name)
                .description(description)
                .type(type)
                .category(category)
                .isRequired(isRequired)
                .isActive(true)
                .orderIndex(orderIndex)
                .durationMinutes(30) // 기본 30분
                .caloriesPerMinute(3.0) // 분당 3칼로리
                .build();
        
        exerciseRepository.save(exercise);
    }
}
