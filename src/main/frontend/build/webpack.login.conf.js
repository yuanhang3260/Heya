"use strict"

const path = require("path");
const webpack = require("webpack");
const merge = require("webpack-merge");
const baseWebpackConfig = require("./webpack.base.conf");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const CopyWebpackPlugin = require("copy-webpack-plugin")
const prodBase = path.resolve(__dirname, "../../webapp/");
const loginBase = path.resolve(__dirname, "../login");

module.exports = merge(baseWebpackConfig, {
  context: loginBase,

  entry: {
    login: "./js/login_main.js",
  },

  output: {
    path: path.resolve(prodBase, "dist/"),
    filename: "js/[name].dist.js",
    publicPath: "dist/",
  },

  devtool: "source-map",

  devServer: {
    contentBase: loginBase,
    historyApiFallback: true,
    inline: true,
    progress: true,
    host: "localhost",
    port: 9090,
  },
});
