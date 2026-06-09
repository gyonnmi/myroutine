const calendarDays = document.querySelectorAll('.calendar-day');
const dailyDetailModal = document.getElementById('dailyDetailModal');
const closeDailyDetailModal = document.getElementById('closeDailyDetailModal');

const selectedDateText = document.getElementById('selectedDateText');
const selectedDailyRate = document.getElementById('selectedDailyRate');
const dailyRoutineList = document.getElementById('dailyRoutineList');

calendarDays.forEach((day) => {
    day.addEventListener('click', () => {
        const date = day.dataset.date;
        const rate = day.dataset.rate;

        if (!date) {
            return;
        }

        selectedDateText.textContent = date;
        selectedDailyRate.textContent = rate + '%';

        selectedDailyRate.classList.remove(
            'none-text',
            'low-text',
            'middle-text',
            'high-text'
        );

        if (rate == 0) {
            selectedDailyRate.classList.add('none-text');
        }
        else if (rate <= 40) {
            selectedDailyRate.classList.add('low-text');
        }
        else if (rate < 80) {
            selectedDailyRate.classList.add('middle-text');
        }
        else {
            selectedDailyRate.classList.add('high-text');
        }

        fetch(`/stats/day?date=${date}`)
            .then((response) => response.json())
            .then((routines) => {
                dailyRoutineList.innerHTML = '';

                if (routines.length === 0) {
                    dailyRoutineList.innerHTML = `
            <div class="daily-routine incomplete">
                <span class="status-dot"></span>
                <div>
                    <strong>この日のルーティンはありません。</strong>
                </div>
            </div>
            `;
                    return;
                }

                routines.forEach((routine) => {
                    const routineElement = document.createElement('div');

                    routineElement.className =
                        routine.completed
                            ? 'daily-routine completed'
                            : 'daily-routine incomplete';

                    routineElement.innerHTML = `
            <span class="status-dot"></span>
            <div>
              <strong>${routine.title}</strong>
              <p>${routine.description || ''}</p>
            </div>
          `;

                    dailyRoutineList.appendChild(routineElement);
                });
            });

        dailyDetailModal.classList.add('active');
    });
});

closeDailyDetailModal.addEventListener('click', () => {
    dailyDetailModal.classList.remove('active');
});

dailyDetailModal.addEventListener('click', (event) => {
    if (event.target === dailyDetailModal) {
        dailyDetailModal.classList.remove('active');
    }
});