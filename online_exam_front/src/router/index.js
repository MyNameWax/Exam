import { createRouter, createWebHistory } from "vue-router";
import Login from "../views/Login.vue";
import Home from "../views/Home.vue";
import Exam from "../views/Exam.vue";

const routes = [
  { path: "/", redirect: "/login" },
  { path: "/login", component: Login, meta: { public: true } },
  {
    path: "/",
    component: () => import("../components/AppLayout.vue"),
    children: [
      { path: "home", component: Home },
      { path: "exam", component: Exam },
    ],
    meta: { requiresAuth: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token");
  if (!to.meta.public && !token) {
    next("/login");
  } else if (to.path === "/login" && token) {
    next("/home");
  } else {
    next();
  }
});

export default router;
