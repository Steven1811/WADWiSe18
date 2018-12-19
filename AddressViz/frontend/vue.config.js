// vue.config.js
const { WebpackWarPlugin } = require('webpack-war-plugin');

module.exports = {
  // proxy all webpack dev-server requests starting with /api
  // to our Spring Boot backend (localhost:8088) using http-proxy-middleware
  // see https://cli.vuejs.org/config/#devserver-proxy
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8088',
        ws: true,
        changeOrigin: true
      }
    }
  },

  // Change build paths to make them Maven compatible
  // see https://cli.vuejs.org/config/
  outputDir: 'target/dist',

  assetsDir: 'static',
  baseUrl: '/advi/',

  configureWebpack: config => {
    if (process.env.NODE_ENV === 'production') {
      config.plugins.push(
        new WebpackWarPlugin({
          archiveName: "advi",
          webInf: "./WEB-INF"
        }))
    }
  },

  lintOnSave: undefined
}