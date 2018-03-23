function range(start, end) {
  let range = [];
  for (let i = start; i <= end; i++) {
    range.push(i);
  }
  return range;
}

function yearSelectOptions() {
  return range(1980, (new Date()).getFullYear());
}

function monthSelectOptions() {
  return range(1, 12);
}

function dateSelectOptions() {
  return range(1, 31);
}

function parseBirth(birth) {
  if (!birth) {
    return null;
  }
  return birth.split("-");
}

function formatBirth(year, month, date) {
  if (!year || isNaN(year) ||
      !month || isNaN(month) ||
      !date || isNaN(date)) {
    return null;
  }

  if (date < 10) {
    date = "0" + date;
  }
  if (month < 10) {
    month = "0" + month;
  }
  return year + "-" + month + "-" + date;
}

export default {
  yearSelectOptions: yearSelectOptions,
  monthSelectOptions: monthSelectOptions,
  dateSelectOptions: dateSelectOptions,
  parseBirth: parseBirth,
  formatBirth: formatBirth,
}
