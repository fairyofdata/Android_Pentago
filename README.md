# Pentago

Pentago는 오목 규칙을 바탕으로 확장된 멘사 셀렉트 게임으로, 6x6 보드 판이 3x3 크기의 4개 작은 판으로 구성되어 있으며, 각 판을 회전시키는 점이 독특한 특징입니다. 게임은 Android Studio에서 구현되었으며, 자이로센서를 이용해 돌을 놓고 판을 회전시키는 방식으로 진행됩니다.

**팀원:** 조민제, 조항민, 백지헌, 이충안

---

## 📌 게임 규칙 (Rules)

- 기본 보드 크기: 6x6, 3x3 크기의 작은 판 4개로 구성
- 돌의 수: 흑돌 18개, 백돌 18개
- 흑돌이 선공하며, 각자 돌을 놓은 후, 4개 작은 판 중 하나를 선택해 90도로 회전
- 같은 색의 돌이 가로, 세로, 대각선으로 5줄을 이루면 승리
- 게임 진행 중 5줄이 완성되더라도 반드시 판을 한 번 회전시켜야 게임이 종료됨

[How to play Pentago](https://youtu.be/y0kBs4-DNe8?si=3p1jdFBd5fvtXh9S)

---

## 📱 Android Studio 구현

- **GridView**를 사용하여 6x6 보드 크기를 구성하였으며, 하나의 휴대폰으로 플레이 가능하도록 설계
- 플레이어가 돌을 놓고 판을 회전시키는 과정을 자이로센서를 통해 구현
- 모든 코드는 Java로 작성되었습니다.

---

## 💻 코드 로직 설명

### 1. 돌 놓기 (Place Stone)
- **PlaceStone**: 돌을 놓는 위치를 결정하는 함수로, 사용자가 돌을 놓고 회전 버튼(on/off)을 선택할 수 있습니다.
  - `onRotationButton`: 90도 오른쪽 회전
  - `offRotationButton`: 90도 왼쪽 회전

### 2. 판 회전 (Rotation)
- 4개 작은 판 중 하나를 선택해 90도로 회전
  - `rotateRight90InGame()`: 90도 오른쪽 회전
  - `rotateLeft90InGame()`: 90도 왼쪽 회전

### 3. 승자 확인 (Check Winner)
- `checkWinnerInGame()`: 가로, 세로, 대각선으로 5개의 돌이 같은 색으로 연속될 경우 승리
  - `checkHorizontalWinner()`: 가로 승리 확인
  - `checkVerticalWinner()`: 세로 승리 확인
  - `checkLeftDiagonalWinner()`: 왼쪽 대각선 승리 확인
  - `checkRightDiagonalWinner()`: 오른쪽 대각선 승리 확인

---

## 📐 세부 코드 로직 (Method별 설명)

### 0. 좌표 클릭 (onItemClick)
- **onItemClick()**: 좌표를 클릭해 위치 업데이트
  - `updateSelectedPosition()`: 현재 위치 업데이트
  - `updateSelectedArea()`: 선택된 영역 업데이트

### 1. Place Stone (돌 놓기)
- **placeStoneInGame()**: 돌을 놓고 보드판 업데이트
  - `updateBoardState()`: 현재 보드 상태 업데이트
  - `adapter.placeStone()`: 화면 위치 갱신
  - `addRotationButtons()`: 회전 버튼 추가

### 2. Rotate Function (회전 기능)
- **rotateRight90InGame() / rotateLeft90InGame()**: 90도 회전 기능 수행
  - `rotateStateRight90() / rotateStateLeft90()`: 회전된 상태 생성
  - `applyChangedState()`: 현재 보드에 변경된 상태 적용

### 3. Check Winner (승자 확인)
- **checkWinner()**: 가로, 세로, 대각선 방향으로 승리 조건 확인
  - `checkHorizontalWinner()`, `checkVerticalWinner()`, `checkLeftDiagonalWinner()`, `checkRightDiagonalWinner()`
  - 승자가 있을 경우 `alertWinner()` 호출

---

## 🎥 시연 영상 (Demonstration Video)

<img src="./Video/Pentago_Demonstration_Video.gif" width="360" height="640"/>

---

## 🛠️ 개발 환경

- **Android Studio Version**: Giraffe 2022.3.1
