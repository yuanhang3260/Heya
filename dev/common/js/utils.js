function sortByYearDesc(x, y) {
  let x_end = 0, y_end = 0;
  if (x.year && x.year.end && !isNaN(x.year.end)) {
    x_end = x.year.end;
  }
  if (y.year && y.year.end && !isNaN(y.year.end)) {
    y_end = y.year.end;
  }
  return y_end - x_end;
}

export default {
  sortByYearDesc: sortByYearDesc,
}
