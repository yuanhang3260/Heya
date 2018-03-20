const kFullContentLines = 5;
const kContentLinesWithHeader = 3;
const kTitleLines = 2;

const kHeaderHeightFactor = 0.45;
const kImageSizeFactor = 0.65;

const kLineHeightFactor = 0.4;

function designLayout(width, height, title, image) {
  const header = title || image;
  const H = Math.min(width, height);

  const headerHeight = Math.floor(H * kHeaderHeightFactor);
  var [titleLineHeight, titleLineSpace] =
      computeLineSize(headerHeight, kTitleLines);
  if (image) {
    var titleLineWidth = width - headerHeight - 2 * titleLineSpace;
    var imageSize = Math.floor(headerHeight * kImageSizeFactor);
  } else {
    var titleLineWidth = width - 2 * titleLineSpace;
  }

  if (header) {
    var contentHeight = H - headerHeight;
    var textLines = kContentLinesWithHeader;
  } else {
    var contentHeight = H;
    var textLines = kFullContentLines;
  }

  const [textLineHeight, textLineSpace] =
      computeLineSize(contentHeight, textLines);
 
  const textLineWidth = width - 2 * textLineSpace;

  titleLineHeight = textLineHeight;
  titleLineSpace = Math.ceil(
      (headerHeight - kTitleLines * titleLineHeight) / (kTitleLines + 1));

  return {
    totalHeight: H,
    contentHeight: contentHeight,
    textLines: textLines,
    textLineHeight: textLineHeight,
    textLineSpace: textLineSpace,
    textLineWidth: textLineWidth,
    headerHeight: headerHeight,
    titleLines: kTitleLines,
    titleLineHeight: titleLineHeight,
    titleLineSpace: titleLineSpace,
    titleLineWidth: titleLineWidth,
    imageSize: imageSize,
  }
}

function toPixel(num) {
  return num + 'px';
}

function randomRange(min, max) {
  return Math.random() * (max - min) + min;
}

function computeLineSize(totalHeight, lines) {
  const lineHeight = Math.floor(
      totalHeight / (lines + kLineHeightFactor) * kLineHeightFactor);
  const lineSpace = Math.floor(
      totalHeight / (lines + kLineHeightFactor) - lineHeight);
  return [
    lineHeight,
    lineSpace,
  ];
}

export {
  designLayout,
  toPixel,
  randomRange,
}
