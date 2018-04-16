"use strict"

const path = require("path");
const webpack = require("webpack");
const merge = require("webpack-merge");
const baseWebpackConfig = require("./webpack.base.conf");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const CopyWebpackPlugin = require("copy-webpack-plugin")
const prodBase = path.resolve(__dirname, "../../webapp/");
const profileBase = path.resolve(__dirname, "../profile");

module.exports = merge(baseWebpackConfig, {
  context: profileBase,

  entry: {
    profile: "./js/profile_main.js",
  },

  output: {
    path: path.resolve(prodBase, "dist/"),
    filename: "js/[name].dist.js",
    publicPath: "dist/",
  },

  devtool: "eval-source-map",

  devServer: {
    contentBase: profileBase,
    historyApiFallback: true,
    inline: true,
    progress: true,
    host: "localhost",
    port: 9090,
  },
});
