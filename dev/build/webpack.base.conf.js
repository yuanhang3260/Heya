'use strict'
const path = require('path')
const webpack = require('webpack');

module.exports = {
  module: {
    rules: [
      {
        test: /\.css$/,
        loader: 'style-loader!css-loader',
      },
      {
        test: /\.scss$/,
        loader: 'style-loader!css-loader!sass-loader?sourceMap',
      },
      {
        test: /\.js$/,
        loader: 'babel-loader',
      },
      { test: /\.(png|woff|woff2|eot|ttf|svg)$/,
        loader: 'url-loader?limit=20000'
      },
    ]
  },
};