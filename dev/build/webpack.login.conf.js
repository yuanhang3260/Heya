"use strict"

const path = require("path");
const webpack = require("webpack");
const merge = require("webpack-merge");
const baseWebpackConfig = require("./webpack.base.conf");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const CopyWebpackPlugin = require("copy-webpack-plugin")
const prodBase = path.resolve(__dirname, "../../");
const loginBase = path.resolve(__dirname, "../login");

module.exports = merge(baseWebpackConfig, {
  context: loginBase,

  entry: {
    login: "./js/login_main.js",
  },

  output: {
    path: prodBase,
    filename: "js/[name].dist.js",
    publicPath: "/",
  },

  devtool: "source-map",

  devServer: {
    contentBase: prodBase,
    historyApiFallback: true,
    inline: true,
    progress: true,
    host: "localhost",
    port: 9090,
  },

  plugins: [
    new HtmlWebpackPlugin({
      template: "index.html",
      filename: "index.html",
      inject: true,
    }),
  ],
});
