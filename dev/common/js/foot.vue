<template>
<div class="bottom-bar">
  <div class="bottom-container">
    <div class="bottom-logo-container">
      <b>Heya</b></br><i class="fa fa-leaf"></i>
    </div>
    <ul class="bottom-ul">
      <li style="font-size: 15px; color: #444"><b>Share</b></li>
      <li>Facebook</li>
      <li>Weibo</a></li>
    </ul>
    <ul class="bottom-ul">
      <li style="font-size: 15px; color: #444"><b>Contact</b></li>
      <li>Author</li>
      <li><a href="https://github.com/yuanhang3260/Heya" target="_blank">Github</a></li>
    </ul>
    <ul class="bottom-horizontal-placer-holder"></ul>
  </div>
</div>
</template>


<script>
export default {
  name: "foot",
  props: {},
  data () {
    return {}
  },
  methods: {},
};
</script>


<style scoped lang="scss">
@import "common/css/foot.scss";
</style>