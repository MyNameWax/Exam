import { createRouter, createWebHashHistory } from "vue-router";
const routes = [
  {
    path: "/",
    component: () => import("../page/index.vue"),
  },
  {
    path: "/exam",
    component: () => import("../page/exam.vue"),
  },
  {
    path: "/result",
    component: () => import("../page/result.vue")
  }
];
const router = createRouter({
  history: createWebHashHistory(),
  routes,
});
export default router;
