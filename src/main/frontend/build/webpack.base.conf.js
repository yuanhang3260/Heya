"use strict"
const path = require("path")
const webpack = require("webpack")
const vueLoaderConfig = require("./vue-loader.conf")
const prodBase = path.resolve(__dirname, "../../")

module.exports = {
  resolve: {
    extensions: [".js", ".vue", ".css", ".scss"],
    alias: {
      "vue$": "vue/dist/vue.esm.js",
      "heya": path.resolve(__dirname, "../"),
    }
  },
  module: {
    rules: [
      {
        test: /\.css$/,
        loader: "style-loader!css-loader",
      },
      {
        test: /\.scss$/,
        loader: "style-loader!css-loader!sass-loader?sourceMap",
      },
      {
        test: /\.js$/,
        loader: "babel-loader",
      },
      { test: /\.(woff|woff2|eot|ttf|svg)$/,
        loader: "url-loader",
        options: {
          limit: 20000,
          // This is relative to output.path/output.publichPath: "dist/"
          outputPath: "fonts/",
          // publicPath: "/dist/fonts/",
        }
      },
      {
        test: /\.vue$/,
        loader: "vue-loader",
        options: vueLoaderConfig
      },
    ]
  },
};
