<template>
  <div class="placeholder-container" :style="getContainerCss()">
    <div v-if="hasHeader()" class="header-box" :style="getHeaderBoxCss()">
      <div v-if="image" class="image-box" :style="getImageBoxCss()">
        <div class="profile-image animated-background" :style="getImageCss()"></div>
      </div>
      <div v-if="title" class="title-box" :style="getTitleBoxCss()">
        <div class="title-leading-line" :style="getTitleLeadingLineCss()"></div>
        <div v-for="i of titleLines" class="title-line animated-background" :style="getTitleLineCss()"></div>
      </div>
    </div>
    <div class="content-box" :style="getContentBoxCss()">
      <div class="content-leading-line" :style="getContentLeadingLineCss()"></div>
      <div v-for="i of textLines" class="content-line animated-background" :style="getContentLineCss()"></div>
    </div>
  </div>
</template>

<script>
import * as design from './design.js';

export default {
  name: 'load-animation',
  props: {
    width: {
      type: Number,
      default: 200,
    },
    height: {
      type: Number,
      default: 100,
    },
    // Has fake title?
    title: {
      type: Boolean,
      default: false,
    },
    // Has fake profile image?
    image: {
      type: Boolean,
      default: false,
    },
    circleImage: {
      type: Boolean,
      default:false,
    }
  },
  data () {
    return design.designLayout(this.width, this.height, this.title, this.image);
  },
  methods: {
    hasHeader: function() {
      return this.title || this.image;
    },

    // Total container.
    getContainerCss: function() {
      return {
        width: this.toPixel(this.width),
        height: this.toPixel(this.height),
      }
    },

    // Header section, including profile image and title.
    getHeaderBoxCss: function() {
      return {
        width: this.toPixel(this.width - 2 * this.titleLineSpace),
        height: this.toPixel(this.headerHeight),
      }
    },

    getImageBoxCss: function() {
      return {
        height: this.toPixel(this.headerHeight),
        width: this.toPixel(this.headerHeight),
      }
    },

    getImageCss: function() {
      let style = {
        height: this.toPixel(this.imageSize),
        width: this.toPixel(this.imageSize),
      }
      if (this.circleImage) {
        style["border-radius"] = "50%";
      }
      return style;
    },

    getTitleBoxCss: function() {
      return {
        height: this.toPixel(this.headerHeight),
      }
    },
    getTitleLineCss: function() {
      return {
        width: this.toPixel(this.titleLineWidth * design.randomRange(0.2, 0.7)),
        'margin-bottom': this.toPixel(this.titleLineSpace),
      }
    },
    getTitleLeadingLineCss: function() {
      return {
        height: this.toPixel(this.titleLineSpace),
        width: this.toPixel(this.titleLineWidth),
      }
    },

    // Content section.
    getContentBoxCss: function() {
      return {
        width: this.toPixel(this.width - 2 * this.textLineSpace),
        height: this.toPixel(this.contentHeight),
      }
    },
    getContentLineCss: function() {
      return {
        width: this.toPixel(this.textLineWidth * design.randomRange(0.6, 1)),
        'margin-bottom': this.toPixel(this.textLineSpace),
      }
    },
    getContentLeadingLineCss: function() {
      return {
        height: this.toPixel(this.textLineSpace),
        width: this.toPixel(this.textLineWidth),
      }
    },

    toPixel : design.toPixel,
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.placeholder-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.header-box {
  display: flex;
  flex-direction: row;
}

.image-box {
  /* Image should be vertically center-aligned inside the box. */
  display: flex;
  align-items: center;
}

.profile-image {
  background: linear-gradient(to right, #eae9ea 25%, #e1e5e1 40%, #eae9ea 50%);
}

.title-box {
  /* Attributes as flex item */ 
  /* title box cover the remaining line space after image box. */
  flex-grow: 1;

  /* Attributes as flex container */
  display: flex;
  flex-direction: column;
}

.title-line {
  flex-grow: 1;
  margin: 0;
  padding: 0;
  background: linear-gradient(to right, #eae9ea 25%, #e1e5e1 40%, #eae9ea 50%);
}

.content-box {
  display: flex;
  flex-direction: column;
}

@keyframes flowAnimation {
  from {
    background-position: 100% 0
  }
  to {
    background-position: 0% 0
  }
}

.content-line {
  flex-grow: 1;
  margin: 0;
  padding: 0;
  background: linear-gradient(to right, #eae9ea 25%, #e1e5e1 40%, #eae9ea 50%);
}

.animated-background {
  animation-duration: 1.2s;
  animation-fill-mode: forwards;
  animation-iteration-count: infinite;
  animation-name: flowAnimation;
  animation-timing-function: linear;
  background-size: 500% 100%;
}

</style>