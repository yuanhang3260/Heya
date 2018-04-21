<template>

<div class="hy-load-animation" :style="getContainerCss()">
  <span :style="getSpanCss()"></span>
  <span :style="getSpanCss()"></span>
  <span :style="getSpanCss()"></span>
  <span :style="getSpanCss()"></span>
  <span :style="getSpanCss()"></span>
  <span :style="getSpanCss()"></span>
  <span :style="getSpanCss()"></span>
  <span :style="getSpanCss()"></span>
</div>

</template>


<script>
export default {
  name: "load-animation",
  props: {
    width: {
      type: Number,
      default: 100,
    },
    height: {
      type: Number,
      default: 100,
    },
    color: {
      type: String,
      default: null,
    },
  },
  data () {
    return {}
  },
  methods: {
    getContainerCss: function() {
      return {
        width: this.width + "px",
        height: this.height + "px",
      }
    },

    getSpanCss: function() {
      return {
        width: (this.width * 0.2) + "px",
        height: (this.height * 0.2) + "px",
        background: this.color,
      }
    },
  },
};
</script>


<style scoped lang="scss">

div.hy-load-animation{
  position: relative;
  margin: 0 auto;
}

div.hy-load-animation span{
  display: inline-block;
  border-radius: 50%;
  position: absolute;
  transform: scale(0.3);
  -webkit-animation: load 1.04s ease infinite;
}

@-webkit-keyframes load{
  0%{
    -webkit-transform: scale(1.0);
    opacity: 1;
  }
  100%{
    -webkit-transform: scale(0.3);
    opacity: 0.5;
  }
}

div.hy-load-animation span:nth-child(1){
  left: 0;
  top: 40%;
  -webkit-animation-delay:0.0s;
}
div.hy-load-animation span:nth-child(2){
  left: 14.6%;
  top: 14.6%;
  -webkit-animation-delay:0.13s;
}
div.hy-load-animation span:nth-child(3){
  left: 40%;
  top: 0;
  -webkit-animation-delay:0.26s;
}
div.hy-load-animation span:nth-child(4){
  top: 14.6%;
  right:14.6%;
  -webkit-animation-delay:0.39s;
}
div.hy-load-animation span:nth-child(5){
  right: 0;
  top: 40%;
  -webkit-animation-delay:0.52s;
}
div.hy-load-animation span:nth-child(6){
  right: 14.6%;
  bottom:14.6%;
  -webkit-animation-delay:0.65s;
}
div.hy-load-animation span:nth-child(7){
  bottom: 0;
  left: 40%;
  -webkit-animation-delay:0.78s;
}
div.hy-load-animation span:nth-child(8){
  bottom: 14.6%;
  left: 14.6%;
  -webkit-animation-delay:0.91s;
}

</style>