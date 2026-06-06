const editButtons = document.querySelectorAll('.edit-routine-button');

const editRoutineModal = document.getElementById('editRoutineModal');
const editRoutineForm = document.getElementById('editRoutineForm');

const editTitle = document.getElementById('editTitle');
const editDescription = document.getElementById('editDescription');

const closeEditRoutineModal = document.getElementById('closeEditRoutineModal');
const cancelEditRoutineModal = document.getElementById('cancelEditRoutineModal');

const editWeekdayCheckboxes =
    document.querySelectorAll('#editRoutineModal .weekday input[type="checkbox"]');

editButtons.forEach((button) => {
    button.addEventListener('click', () => {
        const routineId = button.dataset.id;
        const title = button.dataset.title;
        const description = button.dataset.description || '';
        const repeatDays = button.dataset.repeatDays || '';

        editRoutineForm.action = `/mypage/routines/${routineId}/edit`;

        editTitle.value = title;
        editDescription.value = description;

        editWeekdayCheckboxes.forEach((checkbox) => {
            checkbox.checked = repeatDays.split(',').includes(checkbox.value);
        });

        editRoutineModal.classList.add('active');
    });
});

function closeEditModal() {
    editRoutineModal.classList.remove('active');
}

closeEditRoutineModal.addEventListener('click', closeEditModal);
cancelEditRoutineModal.addEventListener('click', closeEditModal);

editRoutineModal.addEventListener('click', (event) => {
    if (event.target === editRoutineModal) {
        closeEditModal();
    }
});

// Toast 메시지 자동 제거
const toast = document.getElementById('toast');

if (toast) {
    setTimeout(() => {
        toast.remove();
    }, 3000);
}

