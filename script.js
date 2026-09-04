document.addEventListener("DOMContentLoaded", () => {
  const monthsContainer = document.querySelector(".months");
  const datesContainer = document.querySelector(".dates");
  const monthSelector = document.querySelector(".month-selector");
  const monthPicker = document.querySelector(".month-picker");
  const dateSelector = document.querySelector(".date-selector");
  const datePicker = document.querySelector(".date-picker");

  if (
    !monthsContainer ||
    !datesContainer ||
    !monthSelector ||
    !monthPicker ||
    !dateSelector ||
    !datePicker
  ) {
    console.error("Faltan elementos del calendario en el HTML.");
    return;
  }

  const today = new Date();
  today.setHours(0, 0, 0, 0);

  let selectedDate = new Date(today);
  const visibleMonth = new Date(today.getFullYear(), today.getMonth(), 1);

  const monthFormatter = new Intl.DateTimeFormat("es-ES", {
    month: "short"
  });

  const weekdayFormatter = new Intl.DateTimeFormat("es-ES", {
    weekday: "short"
  });

  function formatMonth(date) {
    return monthFormatter.format(date).replace(".", "").toUpperCase();
  }

  function formatWeekday(date) {
    return weekdayFormatter.format(date).replace(".", "");
  }

  function sameDate(date1, date2) {
    return (
      date1.getFullYear() === date2.getFullYear() &&
      date1.getMonth() === date2.getMonth() &&
      date1.getDate() === date2.getDate()
    );
  }

  function sameMonth(date1, date2) {
    return (
      date1.getFullYear() === date2.getFullYear() &&
      date1.getMonth() === date2.getMonth()
    );
  }

  function dateAsIso(date) {
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")}`;
  }

  function renderMonths() {
    monthsContainer.innerHTML = "";

    for (let offset = 0; offset < 12; offset++) {
      const monthDate = new Date(
        visibleMonth.getFullYear(),
        visibleMonth.getMonth() + offset,
        1
      );

      const button = document.createElement("button");
      button.type = "button";
      button.className = "month-btn";
      button.textContent = formatMonth(monthDate);

      if (sameMonth(monthDate, selectedDate)) {
        button.classList.add("active");
      }

      button.addEventListener("click", () => {
        selectedDate = sameMonth(monthDate, today)
          ? new Date(today)
          : monthDate;

        monthSelector.innerHTML =
          `${formatMonth(monthDate)} <span>⌄</span>`;

        dateSelector.innerHTML =
          `${formatWeekday(selectedDate)} ${selectedDate.getDate()} <span>⌄</span>`;

        monthPicker.classList.remove("open");
        renderMonths();
        renderDates();
        window.selectedAppointmentDate = dateAsIso(selectedDate);
      });

      monthsContainer.appendChild(button);
    }
  }

  function renderDates() {
    datesContainer.innerHTML = "";

    const year = selectedDate.getFullYear();
    const month = selectedDate.getMonth();
    const daysInMonth = new Date(year, month + 1, 0).getDate();

    for (let day = 1; day <= daysInMonth; day++) {
      const date = new Date(year, month, day);
      const button = document.createElement("button");

      button.type = "button";
      button.className = "date-btn";
      button.innerHTML = `${formatWeekday(date)}<br>${day}`;

      if (date < today) {
        button.disabled = true;
        button.classList.add("past");
      }

      if (sameDate(date, selectedDate)) {
        button.classList.add("active");
      }

      button.addEventListener("click", () => {
        selectedDate = date;

        dateSelector.innerHTML =
          `${formatWeekday(date)} ${day} <span>⌄</span>`;

        datePicker.classList.remove("open");
        renderDates();
        window.selectedAppointmentDate = dateAsIso(selectedDate);
      });

      datesContainer.appendChild(button);
    }
  }

  monthSelector.addEventListener("click", () => {
    monthPicker.classList.toggle("open");
    datePicker.classList.remove("open");
  });

  dateSelector.addEventListener("click", () => {
    datePicker.classList.toggle("open");
    monthPicker.classList.remove("open");
  });

  document.addEventListener("click", (event) => {
    if (!monthPicker.contains(event.target)) {
      monthPicker.classList.remove("open");
    }

    if (!datePicker.contains(event.target)) {
      datePicker.classList.remove("open");
    }
  });

  monthSelector.innerHTML = `${formatMonth(today)} <span>⌄</span>`;
  dateSelector.innerHTML =
    `${formatWeekday(today)} ${today.getDate()} <span>⌄</span>`;
  window.selectedAppointmentDate = dateAsIso(selectedDate);

  renderMonths();
  renderDates();
});

const openBooking = document.querySelector("#open-booking");
const closeBooking = document.querySelector("#close-booking");
const bookingModal = document.querySelector("#booking-modal");
const bookingForm = document.querySelector("#booking-form");
const whatsappNumber = "5493834698135";
const apiReservasUrl = "http://localhost:8080/api/reservas";

function abrirWhatsAppCita({ nombre, telefono, fecha, hora }) {
  const mensaje = `Hola, quiero confirmar mi cita.\nNombre: ${nombre}\nTeléfono: ${telefono}\nFecha: ${fecha}\nHora: ${hora}`;
  const url = `https://wa.me/${whatsappNumber}?text=${encodeURIComponent(mensaje)}`;
  window.open(url, "_blank", "noopener,noreferrer");
}

async function parsearRespuestaApi(response) {
  const texto = await response.text();

  if (!texto) {
    return {};
  }

  try {
    return JSON.parse(texto);
  } catch {
    return { error: texto || "La respuesta del servidor no es válida." };
  }
}

openBooking?.addEventListener("click", () => {
    bookingModal.classList.add("open");
});

closeBooking?.addEventListener("click", () => {
    bookingModal.classList.remove("open");
});

bookingModal?.addEventListener("click", (event) => {
    if (event.target === bookingModal) {
        bookingModal.classList.remove("open");
    }
});

bookingForm?.addEventListener("submit", async (event) => {
    event.preventDefault();

    const nombre = document.querySelector("#client-name").value.trim();
    const telefono = document.querySelector("#client-phone").value.trim();
  const horarioTexto = document.querySelector(".time-btn.active")?.textContent.trim();
  const match = horarioTexto?.match(/^(\d{1,2}):(\d{2})\s*(AM|PM)$/i);
  if (!window.selectedAppointmentDate || !match) {
    alert("Selecciona una fecha y un horario válidos.");
    return;
  }
  let hora = Number(match[1]);
  if (match[3].toUpperCase() === "PM" && hora !== 12) hora += 12;
  if (match[3].toUpperCase() === "AM" && hora === 12) hora = 0;
  try {
    const response = await fetch(apiReservasUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ fecha: window.selectedAppointmentDate, hora: `${String(hora).padStart(2, "0")}:${match[2]}`, nombre, telefono })
    });
    const result = await parsearRespuestaApi(response);
    if (!response.ok) throw new Error(result.error || `No se pudo crear la reserva (HTTP ${response.status}).`);

    const fechaConfirmada = window.selectedAppointmentDate;
    const horaConfirmada = `${String(hora).padStart(2, "0")}:${match[2]}`;

    alert("Reserva creada correctamente.");
    abrirWhatsAppCita({
      nombre,
      telefono,
      fecha: fechaConfirmada,
      hora: horaConfirmada
    });

    bookingForm.reset();
    bookingModal.classList.remove("open");
  } catch (error) {
    alert(error.message);
  }
});

