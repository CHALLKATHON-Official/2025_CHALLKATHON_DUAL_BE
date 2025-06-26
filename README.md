# 🩺 워크메이트 (WalkMate)

**재활 운동 모니터링 및 케어 지원 앱**

재활이 필요한 환자와 그들을 돌보는 보호자를 위한 **모바일 헬스케어 솔루션**입니다. 안전하고 체계적인 자가 재활 관리를 돕습니다.

## 📌 서비스 개요
<img width="933" alt="image" src="https://github.com/user-attachments/assets/5ce9cf0c-b4ea-479d-bbce-0a5865c2c56b" />
<img width="933" alt="image" src="https://github.com/user-attachments/assets/7d5f29fa-4efa-4583-b8dc-09fd5fcbf9aa" />

### 환자용 기능

* **일일 건강 요약**: 걸음 수, 운동 시간, 거리, 통증 수준 자동 기록  
* **운동 진행 관리**: 실내/실외 운동 완료율 및 운동 시간 시각화  
* **통증 기록**: 운동 직후 통증 평가 (5부위, 0~3점), 메모 저장  
* **운동 성과 조회**: 기간별 운동 기록, 실내/실외 필터, 칼로리/거리/시간 확인  

### 보호자용 기능

* **환자 실시간 현황 보기**: 걸음 수, 운동 시간, 통증 점수 대시보드  
* **위치 추적 및 긴급 알림**: 환자 위치 확인 및 통증 급증 시 연락 기능  
* **환자 정보 열람**: 이름, 나이, 질환, 연락처 및 주간 걸음 수 확인  

## 👥 팀원 소개

| 역할           | 이름                | 담당                                                                 |
|----------------|---------------------|----------------------------------------------------------------------|
| 팀장           | 이수혁              | 총괄                                                                  |
| 기획           | 김유현, 김재우, 이수혁 | 서비스 기획                                                           |
| 디자인         | 김유현              | UI/UX 설계, Figma를 활용한 컴포넌트 디자인 및 프로토타이핑            |
| 프론트엔드 개발 | 김재우, 이수혁       | React + TypeScript 기반 웹(Vite, Vercel) 및 모바일 앱(Expo) 개발       |
| 백엔드 개발     | 이수혁              | Spring Boot 기반 API 서버 개발, DB 모델링, 예외 처리, Docker 설정, 배포 자동화 |

## 🛠️ 사용 기술 스택

### 📱 모바일 앱 (React Native + Expo)

* React Native 0.79.4, Expo SDK 53  
* React Navigation 7, Redux Toolkit  
* Expo Location, Camera, Sensors (가속도계/자이로)  
* AsyncStorage, AuthSession  

### 🌐 웹 플랫폼 (React + Vite)

* React 19, Vite 5  
* React Router DOM, Styled Components  
* Framer Motion, Lucide React, QRCode.react  

### 🖥️ 백엔드 (Spring Boot + Docker)

* Java 17, Spring Boot Framework  
* Gradle 기반 프로젝트 구성  
* RESTful API 설계 및 예외 처리  
* DB 모델링 (MySQL 또는 PostgreSQL 예정)  
* Docker 기반 컨테이너화 및 실행  
* API 테스트 및 디버깅 (Postman 등 사용)  
* GitHub Actions 기반 CI/CD 환경 구축 예정

### ⚙️ 개발 환경

* Yarn, ESLint, GitHub + Actions  
* 배포: Expo EAS (앱), Vercel (웹)

## 📁 파일 구조

```
2025_CHALLKATHON_DUAL_FE/
├── app/          # React Native 앱
│   ├── screens/  # 화면 (auth, patient, caregiver, settings 등)
│   ├── components/  # 공통/설정 UI
│   ├── navigation/  # 네비게이터 구조
│   ├── api/      # API 요청 모듈
│   ├── store/    # Redux 슬라이스
│   ├── assets/   # 이미지, 애니메이션
│   └── constants/  # 색상, 폰트, 간격
├── web/          # React + Vite 웹
│   ├── pages/    # 데스크탑/모바일 페이지
│   ├── components/  # 공통/모바일/데스크탑 컴포넌트
│   ├── router/   # 라우터
│   ├── styles/   # 글로벌 스타일, 테마
│   └── public/   # 정적 파일
└── README.md
```

## 🔍 기능별 소개

### 👨‍⚕️ 환자용 앱 기능

* **메인페이지**  
  * 오늘의 요약: 이름, 걸음 수, 운동 시간, 거리, 통증 수준  
  * 주간 걸음 수: 요일별 통계 및 총합  

* **실내운동**  
  * 진행 상황: 필수 운동 완료 개수, 완료율, 총 운동 시간  
  * 운동 목록:  
    * 필수: 가벼운 걷기, 다리 스트레칭, 걷기 보조  
    * 추천: 서서하기, 앉아서 다리 운동, 균형 운동  
  * 운동 상세:  
    * 걷기: 걸음 수, 거리, 칼로리, 시간, 페이스  
    * 기타 운동: 운동 시간  
    * 운동 후 통증 평가 (5부위, 0~3점), 상세 메모  

* **실외운동**  
  * 운동 전: 날씨 표시 (백엔드 미구현)  
  * 운동 중: 최고 거리, 전날 기록 (시간, 거리)  
  * 운동 후: 통증 평가 동일  

* **통증 기록**  
  * 기록: 5부위 통증 입력 + 메모  
  * 히스토리: 날짜, 시간, 수동/자동, 총점(15점), 메모  

* **운동 기록**  
  * 필터: 전체 / 실내 / 실외  
  * 통계: 날짜, 시간, 이름, 소요시간, 걸음 수, 거리, 칼로리  

* **설정**  
  * 보호자 연동  

### 🧓 보호자용 웹 기능

* **메인페이지**  
  * 프로필: 보호자 이름  
  * 담당 환자 정보: 이름, 나이, 질환, 연락처  
  * 위치 보기: 실시간 위치 지도  
  * 오늘의 현황: 걸음 수 / 운동 시간 / 통증 점수 요약  
  * 긴급 알림: 통증 증가 시간 및 점수, 환자에게 연락 버튼  

* **환자 정보 페이지**  
  * 환자 프로필  
  * 연락처 정보  
  * 이번주 진행 상황: 요일별 걸음 수 + 합계  
