import axiosInstance from "./axiosInstance";

const authApi  = {
    signIn: (bodyData: object) => axiosInstance.post('/api/auth/sign-in', bodyData),
    signUp: (bodyData: object) => axiosInstance.post('/api/auth/sign-up', bodyData),
    // oauth2Test: bodyData => axiosInstance.post('/api/auth/oauth2/sign-up', bodyData),
    oauth2SignIn: (bodyData: object) => axiosInstance.post('/api/auth/oauth2/sign-in', bodyData),
};

export default authApi;