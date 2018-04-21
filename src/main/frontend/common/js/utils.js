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

function googleSearchURL(target) {
  return "https://www.google.com/search?q=" + target;
}

function googleMapURL(place) {
  return "https://www.google.com/maps/search/" + place;
}

function friendlyFileSize(byteSize) {
  if (byteSize < 1024) {
    return byteSize + " B";
  } else if (byteSize < 1048576) {
    return (byteSize / 1024).toFixed(1) + " KB";
  } else {
    return (byteSize / 1048576).toFixed(1) + " MB";
  }
}

export default {
  sortByYearDesc: sortByYearDesc,
  googleSearchURL: googleSearchURL,
  googleMapURL: googleMapURL,
  friendlyFileSize: friendlyFileSize,
}
