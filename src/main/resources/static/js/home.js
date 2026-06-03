const checkboxes = document.querySelectorAll('.routine-item input[type="checkbox"]');
const progressText = document.getElementById('progressText');
const progressFill = document.querySelector('.progress-fill');
const progressCount = document.querySelector('.progress-count');
const routineItems = document.querySelectorAll('.routine-item');
const deleteRoutineButton = document.getElementById('deleteRoutineButton');

function updateProgress() {
  const total = checkboxes.length;
  const checked = document.querySelectorAll('.routine-item input[type="checkbox"]:checked').length;

  const percent = total === 0 ? 0 : Math.round((checked / total) * 100);

  progressText.textContent = percent + '%';
  progressFill.style.width = percent + '%';
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

checkboxes.forEach((checkbox) => {
  checkbox.addEventListener('change', () => {

    const routineId = checkbox.dataset.id;
    const completed = checkbox.checked;

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

// ルーティン追加モーダルの表示
const openRoutineModalButton = document.getElementById('openRoutineModal');
const closeRoutineModalButton = document.getElementById('closeRoutineModal');
const cancelRoutineModalButton = document.getElementById('cancelRoutineModal');
const routineModal = document.getElementById('routineModal');

const routineForm = document.getElementById('routineForm');
const routineIdInput = document.getElementById('routineId');
const titleInput = document.getElementById('title');
const descriptionInput = document.getElementById('description');
const modalTitle = document.getElementById('modalTitle');
const submitButton = document.getElementById('submitButton');

const weekdayCheckboxes = document.querySelectorAll('.weekday input[type="checkbox"]');
const routineTitles = document.querySelectorAll('.routine-title');

function openAddModal() {
  routineForm.action = '/routines';
  routineIdInput.value = '';
  titleInput.value = '';
  descriptionInput.value = '';
  deleteRoutineButton.style.display = 'none';

  weekdayCheckboxes.forEach((checkbox) => {
    checkbox.checked = false;
  });

  modalTitle.textContent = 'ルーティン追加';
  submitButton.textContent = '追加する';

  routineModal.classList.add('active');
}

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

  modalTitle.textContent = 'ルーティン編集';
  submitButton.textContent = '更新する';

  routineModal.classList.add('active');
}

function closeModal() {
  routineModal.classList.remove('active');
}

openRoutineModalButton.addEventListener('click', openAddModal);
closeRoutineModalButton.addEventListener('click', closeModal);
cancelRoutineModalButton.addEventListener('click', closeModal);

routineTitles.forEach((routineTitle) => {
  routineTitle.addEventListener('click', () => {
    openEditModal(routineTitle);
  });
});

routineModal.addEventListener('click', (event) => {
  if (event.target === routineModal) {
    closeModal();
  }
});

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