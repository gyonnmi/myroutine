const checkboxes = document.querySelectorAll('.routine-item input[type="checkbox"]');
const progressText = document.getElementById('progressText');
const progressFill = document.querySelector('.progress-fill');
const progressCount = document.querySelector('.progress-count');
const routineItems = document.querySelectorAll('.routine-item');

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
  checkbox.addEventListener('change', updateProgress);
});

updateProgress();