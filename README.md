## Pentago

오목 규칙을 바탕으로 확장된 멘사 셀렉트 게임
보드 판이 4등분되어 회전시키는 점이 특징이다.

6X6 보드판이 기본이고 이는 3X3 보드판 4개로 구성되어 있다. 

돌의 갯수는 흑돌 18개, 백돌 18개

### Rule
흑돌이 선공이다. 

회전할 수 있는 4개의 판 위에 각자 돌을 놓고 4영역의 판 중 무조건 하나를 선택하여 90도로 돌린다. 

돌이 5개가 되면 게임이 끝난다. 
(현 게임에서는 5줄이 완성이 되고 판을 하나 무조건 돌려야 게임이 끝이남.)

How to play : [link](https://youtu.be/y0kBs4-DNe8?si=3p1jdFBd5fvtXh9S)



## In Android studio
Grid view를 사용한 $6 \times 6$사이즈의 게임. 

하나의 휴대폰으로 플레이하고, 플레이어가 수를 선택하고, 화면을 회전하는 방식을 휴대폰 자이로센서로 실행하도록 함.

전체적인 코드는 java로 작성되었음. 

## Explain Code Logic (briefly)

### 0. 6X6 좌표 클릭 
#### 1. PlaceStone 
돌을 놓는 걸 확정 짓는 함수
버튼 2개 추가 (on, off Rotation) // 현재는 오른쪽, 왼쪽 90도 회전 버튼

#### 2. on, off Rotation 
보드 판의 사분면 하나를 90도로 회전

#### 3. check Winner
가로, 세로, 대각선으로 승자가 있는지 확인
그 이후 버튼을 2개 제거하고
플레이어 턴 변경 

## Explain Code Logic (In detail, through methods)

### 0. onItemClick() // 좌표 클릭
updateSeletedPosition() // 업데이트 좌표 
updatedSelectedArea() //업데이트 영역 좌표

### 1. Place Stone onClick() // Place Stone 버튼 클릭

#### 1-1. placeStoneInGame() // 게임 내에서 Place Stone 관련 기능 수행
updateBoarState() // 현재 보드판 업데이트 (0, 1추가)
adapter.placeStone() // 어뎁터에게 위치 전달 (화면 변경)
addRotationButtons() // 버튼 2개 추가 
#### 1-2. toggleButtonActive() //토글 버튼 액티브 버튼, 안내문을 발생시키기 위함


### 2. Rotate Function 
#### if onRotationSensorButton Click() //센서 on 버튼, 현재 오른쪽 90도 회전버튼

##### 2-1. rotateRight90InGame() // 게임 내에서 rotate90 기능 수행
###### 2-1-1 rotateStateRight90() // 영역 좌표로 QuarterState불러오고, 회전한 RotateState생성
###### 2-1-2 applyChangedState() // 변경된 상태를 현재 boardState에 적용

##### 2-2. checkWinnerInGame() // 게임 내에서 승자 검사, 3에서 자세히 설명

#### if offRotationSensorButton() Click() // 센서 off 버튼, 현재 왼쪽 90도 회전버튼

##### 2-1. rotateLeft90InGame() // 게임 내에서 rotate90 기능 수행
###### 2-1-1 rotateStateLeft90() // 영역 좌표로 QuarterState불러오고, 회전한 RotateState생성
###### 2-1-2 applyChangedState() // 변경된 상태를 현재 boardState에 적용

##### 2-2. checkWinnerInGame() // 게임 내에서 승자 검사, 3에서 자세히 설명

### 3. checkWinnerInGame() 
#### 3-1. checkWinner() // 승자 검사하는 메서드
##### 3-1-1.
checkHorizontalWinner() // 가로 검사
alertWinner() // 승자 있다면 승자 호출
##### 3-1-2.
checkVertical() // 세로 검사
alertWinner() // 승자 있다면 승자 호출
##### 3-1-3. 
checkLeftDiagonal // 왼쪽 대각선 검사
alertWinner() // 승자 있다면 승자 호출
##### 3-1-4. 
checkRightDiagonal // 오른쪽 대각선 검사
alertWinner() // 승자 있다면 승자 호출
#### 3-2. toggleButtonActive() //토글 버튼 액티브 버튼, 안내문을 발생시키기 위함
#### 3-3. togglePlayer() // 플레이어 변경
#### 3-4. removeRotationButtons() // 추가한 2개 버튼 제거 

## Demonstration video
![Demonstration video](./Video/Pentago_Demonstration_Video.gif)

## Version
Android Studio Giraffe 2022.3.1
