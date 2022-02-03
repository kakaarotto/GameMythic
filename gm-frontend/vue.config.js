module.exports = {
  lintOnSave: false,
  devServer: {
    port: "9999",
  },

  pluginOptions: {
    "style-resources-loader": {
      preProcessor: "less",
      patterns: [],
    },
  },
};
