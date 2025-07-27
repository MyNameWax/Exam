import http from "../../utils/axios";

export default {
  /**
   * 用户登录
   * @param {*} data
   * @returns
   */
  userLogin(data) {
    return http({
      url: `/sys/auth/v1/login`,
      method: "post",
      data,
    });
  },
};
