// 홈 화면의 루틴 체크박스들
const checkboxes = document.querySelectorAll('.routine-item input[type="checkbox"]');
// 진행률 표시 영역
const progressText = document.getElementById('progressText');
const progressFill = document.querySelector('.progress-fill');
const progressCount = document.querySelector('.progress-count');
// 루틴 카드 전체 요소들
const routineItems = document.querySelectorAll('.routine-item');

// 체크된 루틴 수를 계산하여 진행률을 갱신하는 함수
function updateProgress() {
  const total = checkboxes.length;
  const checked = document.querySelectorAll('.routine-item input[type="checkbox"]:checked').length;

  const percent = total === 0 ? 0 : Math.round((checked / total) * 100);

  progressText.textContent = percent + '%';

  if (percent < 40) {
    progressText.style.color = '#d86b6b';
  }
  else if (percent < 80) {
    progressText.style.color = '#d9943f';
  }
  else {
    progressText.style.color = '#5e9c7a';
  }

  progressFill.style.width = percent + '%';

  if (percent < 40) {
    progressFill.style.backgroundColor = '#f7a8a8';
  }
  else if (percent < 80) {
    progressFill.style.backgroundColor = '#f8c58b';
  }
  else {
    progressFill.style.backgroundColor = '#9fd8b5';
  }

  if (percent === 100 && !wasComplete) {
    wasComplete = true;

    // 달성률 카드 애니메이션
    const progressCard = document.querySelector('.progress-card');

    progressCard.classList.remove('achievement-complete');
    void progressCard.offsetWidth;
    progressCard.classList.add('achievement-complete');

    // 토스트 메시지
    const toast = document.getElementById('achievementToast');

    toast.textContent = '🌸 今日のルーティンをすべて達成しました！';
    toast.classList.add('show');

    setTimeout(() => {
      toast.classList.remove('show');
    }, 3000);
  }

  if (percent < 100) {
    wasComplete = false;
  }

  progressCount.textContent = checked + ' / ' + total + ' 件 完了';

  routineItems.forEach((item) => {
    const checkbox = item.querySelector('input[type="checkbox"]');

    if (checkbox.checked) {
      item.classList.add('completed');
    } else {
      item.classList.remove('completed');
    }
  });
}

// 루틴 완료 체크 이벤트
checkboxes.forEach((checkbox) => {
  checkbox.addEventListener('change', () => {

    const routineId = checkbox.dataset.id;
    const completed = checkbox.checked;

    // fetch API를 사용하여 Spring Boot Controller에 완료 상태 전달
    fetch(`/routines/${routineId}/complete`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: `completed=${completed}`
    });

    updateProgress();
  });
});

updateProgress();

// 루틴 추가/편집 모달 관련 요소들
const openRoutineModalButton = document.getElementById('openRoutineModal');
const closeRoutineModalButton = document.getElementById('closeRoutineModal');
const cancelRoutineModalButton = document.getElementById('cancelRoutineModal');
const routineModal = document.getElementById('routineModal');

// 모달 form 안의 입력 요소들
const routineForm = document.getElementById('routineForm');
const routineIdInput = document.getElementById('routineId');
const titleInput = document.getElementById('title');
const descriptionInput = document.getElementById('description');
const modalTitle = document.getElementById('modalTitle');
const submitButton = document.getElementById('submitButton');

// 수정/삭제용 버튼
const deleteRoutineButton = document.getElementById('deleteRoutineButton');

// 반복 요일 체크박스와 요일 선택/수정 대상 요소들
const weekdayCheckboxes = document.querySelectorAll('.weekday input[type="checkbox"]');
const routineTitles = document.querySelectorAll('.routine-title'); // 루틴 제목 요소들 (편집 모달 열 때 데이터 전달용)
const repeatDaysValidator = document.getElementById('repeatDaysValidator');

// 루틴 추가 모달 열기
function openAddModal() {
  routineForm.action = '/routines';
  routineIdInput.value = '';
  titleInput.value = '';
  descriptionInput.value = '';
  deleteRoutineButton.style.display = 'none';

  weekdayCheckboxes.forEach((checkbox) => {
    checkbox.checked = false;
  });

  validateRepeatDays();
  modalTitle.textContent = 'ルーティン追加';
  submitButton.textContent = '追加する';

  routineModal.classList.add('active');
}

// 루틴 편집 모달 열기
function openEditModal(routineTitle) {
  const routineId = routineTitle.dataset.id;
  const title = routineTitle.dataset.title;
  const description = routineTitle.dataset.description || '';
  const repeatDays = routineTitle.dataset.repeatDays || '';
  deleteRoutineButton.style.display = 'inline-block';
  deleteRoutineButton.dataset.id = routineId;

  routineForm.action = `/routines/${routineId}/edit`;

  routineIdInput.value = routineId;
  titleInput.value = title;
  descriptionInput.value = description;

  weekdayCheckboxes.forEach((checkbox) => {
    checkbox.checked = repeatDays.split(',').includes(checkbox.value);
  });

  validateRepeatDays();

  modalTitle.textContent = 'ルーティン編集';
  submitButton.textContent = '更新する';

  routineModal.classList.add('active');
}

// 모달 닫기
function closeModal() {
  routineModal.classList.remove('active');
}

// 모달 열기/닫기 이벤트 리스너
openRoutineModalButton.addEventListener('click', openAddModal);
closeRoutineModalButton.addEventListener('click', closeModal);
cancelRoutineModalButton.addEventListener('click', closeModal);

// 루틴 제목 클릭 시 편집 모달 열기
routineTitles.forEach((routineTitle) => {
  routineTitle.addEventListener('click', () => {
    openEditModal(routineTitle);
  });
});

// 모달 바깥 클릭 시 모달 닫기
routineModal.addEventListener('click', (event) => {
  if (event.target === routineModal) {
    closeModal();
  }
});

// 루틴 삭제 버튼 클릭 시 삭제 폼 제출
deleteRoutineButton.addEventListener('click', () => {
  const routineId = deleteRoutineButton.dataset.id;

  if (!confirm('このルーティンを削除しますか？')) {
    return;
  }

  const deleteForm = document.createElement('form');
  deleteForm.method = 'post';
  deleteForm.action = `/routines/${routineId}/delete`;

  document.body.appendChild(deleteForm);
  deleteForm.submit();
});

// 반복 요일 체크박스 유효성 검사
function validateRepeatDays() {
  const checkedWeekdays = document.querySelectorAll(
    '.weekday input[type="checkbox"]:checked'
  );

  if (checkedWeekdays.length > 0) {
    repeatDaysValidator.value = 'selected';
    repeatDaysValidator.setCustomValidity('');
  } else {
    repeatDaysValidator.value = '';
    repeatDaysValidator.setCustomValidity('曜日を1つ以上選択してください。');
  }
}

// 반복 요일 체크박스 변경 시 유효성 검사 실행
weekdayCheckboxes.forEach((checkbox) => {
  checkbox.addEventListener('change', validateRepeatDays);
});

// 폼 제출 시 유효성 검사 실행
routineForm.addEventListener('submit', () => {
  validateRepeatDays();
});

validateRepeatDays();

const sidebarToggle = document.getElementById('sidebarToggle');
const sidebarClose = document.getElementById('sidebarClose');
const sidebarOverlay = document.getElementById('sidebarOverlay');
const sidebar = document.getElementById('sidebar');

function openSidebar() {
  sidebar.classList.add('active');
  sidebarOverlay.classList.add('active');
}

function closeSidebar() {
  sidebar.classList.remove('active');
  sidebarOverlay.classList.remove('active');
}

sidebarToggle.addEventListener('click', openSidebar);
sidebarClose.addEventListener('click', closeSidebar);
sidebarOverlay.addEventListener('click', closeSidebar);
