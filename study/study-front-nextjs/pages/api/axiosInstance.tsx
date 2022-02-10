import axios, { AxiosRequestConfig, AxiosRequestHeaders, AxiosResponse } from 'axios';
import store from '../../redux/store';
import {setAccessToken} from '../../redux/token';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:10000',
    timeout: 2000,
});

axiosInstance.interceptors.request.use(
    function (config: AxiosRequestConfig) : AxiosRequestConfig{
        const headers : AxiosRequestHeaders = {
            ...config.headers,
            'Content-Type': 'application/json; charset=utf-8',
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Credentials': 'true',
            'Content-Authorization': store.getState().token.accessToken,
        }
        config.headers = headers;
        // console.log('store.getState().token', store.getState().token)

        return config;
    }
)

axiosInstance.interceptors.response.use(
    function (response: AxiosResponse) : AxiosResponse{
        // console.log('response.headers', response.headers)
        if(response.headers["authorization"] !== null &&
            response.headers["authorization"] !== undefined &&
            response.headers["authorization"].length > 7){
            console.log('response.headers["authorization"]', response.headers["authorization"])
            store.dispatch(setAccessToken(response.headers["authorization"]));
        }
        return response;
    }
)

export default axiosInstance;