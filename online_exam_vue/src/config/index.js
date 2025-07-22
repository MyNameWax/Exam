import Vue from 'vue'
Vue.prototype.API_BASE_URL = process.env.VUE_APP_API_BASE_URL
window._CONFIG['domianURL'] = Vue.prototype.API_BASE_URL
