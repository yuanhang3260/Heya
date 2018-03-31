function init () {
  for (let place of this.placesInfo) {
    if (place.current) {
      this.current = place;
    }
    if (place.hometown) {
      this.hometown = place;
    }
  }
}

export default {
  mounted: init,
}
