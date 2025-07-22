const req = import.meta.glob('./modules/*.js', { eager: true })
const api = {}
for (const key in req) {
    const name = key.replace(/^\.\/modules\/(.*)\.\w+$/, '$1')
    const value = req[key] // 取文件的内容
    api[name] = value.default // 赋值
}
const API = api

export default API
